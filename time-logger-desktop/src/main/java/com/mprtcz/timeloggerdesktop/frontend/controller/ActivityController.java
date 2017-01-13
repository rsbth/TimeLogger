package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.ColorDialog;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.ConfirmationPopup;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.DialogElementsConstructor;
import com.mprtcz.timeloggerdesktop.frontend.utils.ResultEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class ActivityController {
    private ActivityService activityService;
    private ChangeListener<Throwable> exceptionListener;
    private EventHandler<WorkerStateEvent> onFailedTaskEventHandler;
    private ResultEventHandler<WorkerStateEvent> onSucceededActivityAddEventHandler;
    private ResourceBundle messages;
    private ConfirmationPopup confirmationPopup;
    private JFXDialog bottomDialog;
    private ExecutorService executorService;

    private String newActivityName;
    private String newActivityDescription;

    private ActivityController(ActivityService activityService, ChangeListener<Throwable> exceptionListener,
                              EventHandler<WorkerStateEvent> onFailedTaskEventHandler,
                              ResultEventHandler<WorkerStateEvent> onSucceededActivityAddEventHandler,
                              ResourceBundle messages, ExecutorService executorService) {
        this.activityService = activityService;
        this.exceptionListener = exceptionListener;
        this.onFailedTaskEventHandler = onFailedTaskEventHandler;
        this.onSucceededActivityAddEventHandler = onSucceededActivityAddEventHandler;
        this.messages = messages;
        this.executorService = executorService;
    }

    public void initActivityRemoveConfirmationPopup(Region source) {
        this.confirmationPopup = new ConfirmationPopup(messages.getString("remove_activitypopup_label"), source, this.messages);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            //TODO remove activity
            System.out.println("Remove Activity clicked");
            this.confirmationPopup.close();
        });
        this.confirmationPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 10, -10);
    }


    public void showColorDialog(JFXListView<Activity> activityNamesList, StackPane source) {
        closeDialogIfExists();
        Activity selectedActivity = activityNamesList.getSelectionModel().getSelectedItem();
        if (selectedActivity == null) {
            return;
        }
        ColorDialog colorDialog = new ColorDialog(selectedActivity, this.messages);
        colorDialog.getCancelButton().setOnMouseClicked(event -> ActivityController.this.bottomDialog.close());
        colorDialog.getConfirmButton().setOnMouseClicked(
                event -> {
                    ActivityController.this.updateChangedActivityAndUI(colorDialog.getUpdatedColorActivity());
                    this.bottomDialog.close();
                });
        this.bottomDialog = new JFXDialog(source, colorDialog.createLayout(), JFXDialog.DialogTransition.BOTTOM);
        this.bottomDialog.show();
    }

    public void updateChangedActivityAndUI(Activity activity) {
        if (activity == null) {
            return;
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                ActivityController.this.activityService.updateActivity(activity);
                return null;
            }
        };
        task.setOnSucceeded(this.onSucceededActivityAddEventHandler);
        task.exceptionProperty().addListener(this.exceptionListener);
        task.setOnFailed(this.onFailedTaskEventHandler);
        this.executorService.execute(task);
    }

    public void closeDialogIfExists() {
        if (this.bottomDialog != null) {
            this.bottomDialog.close();
        }
    }

    public void loadAddDialog(StackPane bottomStackPane) {
        closeDialogIfExists();
        DialogElementsConstructor dialogElementsConstructor = new DialogElementsConstructor(messages);
        dialogElementsConstructor.getConfirmButton().setOnMouseClicked(event ->
                ActivityController.this.processSaveActivity(dialogElementsConstructor.getNewActivityNameTextField(),
                        dialogElementsConstructor.getNewActivityDescriptionTextField(), true, event));
        dialogElementsConstructor.getCancelButton().setOnMouseClicked(event ->
                ActivityController.this.processSaveActivity(dialogElementsConstructor.getNewActivityNameTextField(),
                        dialogElementsConstructor.getNewActivityDescriptionTextField(), false, event));
        VBox overlayVBox = (VBox) dialogElementsConstructor.getContent();
        this.bottomDialog = new JFXDialog(bottomStackPane, overlayVBox, JFXDialog.DialogTransition.BOTTOM);
        this.bottomDialog.show();
    }

    public void processSaveActivity(JFXTextField newActivityNameTextField, JFXTextField newActivityDescriptionTextField,
                                     boolean withActivitySave, MouseEvent event) {
        if (newActivityNameTextField.getText().equals("") && withActivitySave) {
            return;
        }
        if (withActivitySave) {
            if (newActivityDescriptionTextField.getText().equals("")) {
                this.newActivityName = newActivityNameTextField.getText();
                this.newActivityDescription = newActivityDescriptionTextField.getText();
                this.initEmptyDescriptionConfirmationPopup();
                this.confirmationPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,
                        event.getX(), event.getY());
            } else {
                ActivityController.this.addNewActivity(newActivityNameTextField.getText(),
                        newActivityDescriptionTextField.getText());
            }
        }
        newActivityNameTextField.setText("");
        newActivityDescriptionTextField.setText("");
        this.bottomDialog.close();
    }

    public void initEmptyDescriptionConfirmationPopup() {
        this.confirmationPopup = new ConfirmationPopup(messages.getString("empty_description_confirm_label"),
                this.bottomDialog, this.messages);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            this.addNewActivity(this.newActivityName, this.newActivityDescription);
            this.confirmationPopup.close();
        });
    }

    private Task<ValidationResult> addActivityTask;
    public void addNewActivity(String name, String description) {
        try {
            this.addActivityTask = new Task<ValidationResult>() {
                @Override
                protected ValidationResult call() throws Exception {
                    return ActivityController.this.activityService.addActivity(name, description);
                }
            };
            addActivityTask.setOnSucceeded(this.addActivitySucceeded());
            addActivityTask.exceptionProperty().addListener(this.exceptionListener);
            this.executorService.execute(addActivityTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EventHandler<WorkerStateEvent> addActivitySucceeded() {
        return (WorkerStateEvent event) -> {
            ActivityController.this.onSucceededActivityAddEventHandler.setResult(this.addActivityTask.getValue());
            ActivityController.this.onSucceededActivityAddEventHandler.handle(event);
        };
    }


    public static class ActivityControllerBuilder {
        private ActivityService activityService;
        private ChangeListener<Throwable> exceptionListener;
        private EventHandler<WorkerStateEvent> onFailedTaskEventHandler;
        private ResultEventHandler<WorkerStateEvent> onSucceededActivityAddEventHandler;
        private ResourceBundle messages;
        private ExecutorService executorService;

        public ActivityControllerBuilder(ActivityService activityService,
                                         ResourceBundle messages,
                                         ExecutorService executorService) {
            this.activityService = activityService;
            this.messages = messages;
            this.executorService = executorService;
        }

        public ActivityControllerBuilder exceptionListener(ChangeListener<Throwable> exceptionListener) {
            this.exceptionListener = exceptionListener;
            return this;
        }
        public ActivityControllerBuilder onFailedTaskEventHandler(EventHandler<WorkerStateEvent> onFailedTaskEventHandler) {
            this.onFailedTaskEventHandler = onFailedTaskEventHandler;
            return this;
        }
        public ActivityControllerBuilder onSucceededActivityAddEventHandler
                (ResultEventHandler<WorkerStateEvent> onSucceededActivityAddEventHandler) {
            this.onSucceededActivityAddEventHandler = onSucceededActivityAddEventHandler;
            return this;
        }

        public ActivityController getInstance() {
            return new ActivityController(
                    this.activityService,
                    this.exceptionListener,
                    this.onFailedTaskEventHandler,
                    this.onSucceededActivityAddEventHandler,
                    this.messages,
                    this.executorService
            );
        }
    }
}