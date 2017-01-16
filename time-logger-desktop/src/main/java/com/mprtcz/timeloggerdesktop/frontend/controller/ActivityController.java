package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class ActivityController {
    private Logger logger = LoggerFactory.getLogger(ActivityController.class);

    private ActivityService activityService;
    private ChangeListener<Throwable> exceptionListener;
    private EventHandler<WorkerStateEvent> onFailedTaskEventHandler;
    private ResultEventHandler<WorkerStateEvent> onSucceededActivityAddEventHandler;
    private ResourceBundle messages;
    private ConfirmationPopup confirmationPopup;
    private JFXDialog bottomDialog;
    private ExecutorService executorService;


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

    public void initActivityRemoveConfirmationPopup(Region source, Activity activity) {
        this.confirmationPopup = new ConfirmationPopup(messages.getString("remove_activitypopup_label"), source, this.messages);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            this.removeActivity(activity);
            System.out.println("Remove Activity clicked");
            this.confirmationPopup.close();
        });
        this.confirmationPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 10, -10);
    }

    public void showEditActivityDialog(JFXListView<Activity> activityNamesList, StackPane source) {
        closeDialogIfExists();
        Activity selectedActivity = activityNamesList.getSelectionModel().getSelectedItem();
        logger.info("selectedActivity.toString() = {}", selectedActivity.toString());
        if (selectedActivity == null) {
            return;
        }
        DialogElementsConstructor dialogElementsConstructor = new DialogElementsConstructor(messages, selectedActivity);
        dialogElementsConstructor.getConfirmButton().setOnAction(event ->
                ActivityController.this.processUpdateActivity(dialogElementsConstructor.getUpdatedActivity()));
        dialogElementsConstructor.getCancelButton().setOnMouseClicked(event ->
                ActivityController.this.bottomDialog.close());
        VBox overlayVBox = (VBox) dialogElementsConstructor.getContent();
        this.bottomDialog = new JFXDialog(source, overlayVBox, JFXDialog.DialogTransition.BOTTOM);
        this.bottomDialog.show();
    }

    private Activity activityToUpdate;
    private void processUpdateActivity(Activity activity) {
        logger.info("processUpdateActivity(), activity = {} " , activity.toString());
        if (activity.getName().equals("") || activity.getName() == null) {
            return;
        }
        if (activity.getDescription().equals("")) {
            this.activityToUpdate = activity;
            this.initEmptyDescriptionConfirmationPopup(ActivityMethodType.UPDATE);
            this.confirmationPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT);
        } else {
            ActivityController.this.updateChangedActivityAndUI(activity);
        }
        this.bottomDialog.close();
    }

    private Task updateActivityTask;
    public void updateChangedActivityAndUI(Activity activity) {
        logger.info(" updateChangedActivityAndUI activity = {}", activity);
        if (activity == null) {
            return;
        }
        this.updateActivityTask = new Task() {
            @Override
            protected ValidationResult call() throws Exception {
                return ActivityController.this.activityService.updateActivity(activity);
            }
        };
        updateActivityTask.setOnSucceeded(this.addActivitySucceeded(this.updateActivityTask));
        updateActivityTask.exceptionProperty().addListener(this.exceptionListener);
        updateActivityTask.setOnFailed(this.onFailedTaskEventHandler);
        this.executorService.execute(updateActivityTask);
    }

    private void removeActivity(Activity activity) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                ActivityController.this.activityService.removeActivity(activity);
                return null;
            }
        };
        task.setOnSucceeded(this.getRemoveActivitySucceeded());
        task.exceptionProperty().addListener(this.exceptionListener);
        task.setOnFailed(this.onFailedTaskEventHandler);
        this.executorService.execute(task);
    }

    private EventHandler<WorkerStateEvent> getRemoveActivitySucceeded() {
        return (WorkerStateEvent event) -> {
            ActivityController.this.onSucceededActivityAddEventHandler.setResult(
                    new ValidationResult(ValidationResult.CustomErrorEnum.ACTIVITY_REMOVED));
            ActivityController.this.onSucceededActivityAddEventHandler.handle(event);
        };
    }


    public void closeDialogIfExists() {
        if (this.bottomDialog != null) {
            this.bottomDialog.close();
        }
    }

    private Activity createdActivity;
    public void loadAddDialog(StackPane bottomStackPane) {
        closeDialogIfExists();
        DialogElementsConstructor dialogElementsConstructor = new DialogElementsConstructor(messages);
        dialogElementsConstructor.getConfirmButton().setOnMouseClicked(event ->
                ActivityController.this.processSaveActivity(dialogElementsConstructor.getNewActivity(), event));
        dialogElementsConstructor.getCancelButton().setOnMouseClicked(event ->
                ActivityController.this.bottomDialog.close());
        VBox overlayVBox = (VBox) dialogElementsConstructor.getContent();
        this.bottomDialog = new JFXDialog(bottomStackPane, overlayVBox, JFXDialog.DialogTransition.BOTTOM);
        this.bottomDialog.show();
    }

    public void processSaveActivity(Activity createdActivity,
                                    MouseEvent event) {
        if (createdActivity.getName().equals("") || createdActivity.getName() == null) {
            return;
        }
        if (createdActivity.getDescription().equals("")) {
            this.createdActivity = createdActivity;
            this.initEmptyDescriptionConfirmationPopup(ActivityMethodType.CREATE);
            this.confirmationPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,
                    event.getX(), event.getY());
        } else {
            ActivityController.this.addNewActivity(createdActivity);
        }
        this.bottomDialog.close();
    }

    public void initEmptyDescriptionConfirmationPopup(ActivityMethodType methodType) {
        this.confirmationPopup = new ConfirmationPopup(messages.getString("empty_description_confirm_label"),
                this.bottomDialog, this.messages);
        if(methodType == ActivityMethodType.CREATE) {
            this.confirmationPopup.getConfirmButton().setOnAction(e -> {
                this.addNewActivity(this.createdActivity);
                this.confirmationPopup.close();
            });
        } else if(methodType == ActivityMethodType.UPDATE) {
            this.confirmationPopup.getConfirmButton().setOnAction(e -> {
                this.updateChangedActivityAndUI(activityToUpdate);
                this.confirmationPopup.close();
            });
        }
    }

    private enum ActivityMethodType {
        CREATE,
        UPDATE
    }

    private Task<ValidationResult> addActivityTask;

    public void addNewActivity(Activity createdActivity) {
        try {
            this.addActivityTask = new Task<ValidationResult>() {
                @Override
                protected ValidationResult call() throws Exception {
                    return ActivityController.this.activityService.addActivity(createdActivity);
                }
            };
            addActivityTask.setOnSucceeded(this.addActivitySucceeded(this.addActivityTask));
            addActivityTask.exceptionProperty().addListener(this.exceptionListener);
            this.executorService.execute(addActivityTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EventHandler<WorkerStateEvent> addActivitySucceeded(Task task) {
        return (WorkerStateEvent event) -> {
            ActivityController.this.onSucceededActivityAddEventHandler.setResult((ValidationResult) task.getValue());
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