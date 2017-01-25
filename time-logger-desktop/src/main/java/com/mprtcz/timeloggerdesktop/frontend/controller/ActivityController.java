package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.ConfirmationPopup;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.DialogElementsConstructor;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Transform;
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
    private ResourceBundle messages;
    private ConfirmationPopup confirmationPopup;
    private JFXDialog bottomDialog;
    private ExecutorService executorService;
    private MainController mainController;

    public ActivityController(MainController mainController, ActivityService activityService,
                               ResourceBundle messages, ExecutorService executorService) {
        this.mainController = mainController;
        this.activityService = activityService;
        this.messages = messages;
        this.executorService = executorService;
    }

    public void initActivityRemoveConfirmationPopup(Region source, Activity activity) {
        logger.info("ActivityController.initActivityRemoveConfirmationPopup");
        this.confirmationPopup = new ConfirmationPopup(messages.getString("remove_activitypopup_label"), source, this.messages);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            this.removeActivity(activity);
            this.confirmationPopup.close();
        });
        this.confirmationPopup.setPrefWidth(CONFIRMATION_POPUP_PREF_WIDTH);
        this.confirmationPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, getXCoordinate(source), -20);
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
        logger.info("processUpdateActivity(), activity = {} ", activity.toString());
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

    private Task<ValidationResult> updateActivityTask;

    public void updateChangedActivityAndUI(Activity activity) {
        logger.info(" updateChangedActivityAndUI activity = {}", activity);
        if (activity == null) {
            return;
        }
        this.updateActivityTask = new Task<ValidationResult>() {
            @Override
            protected ValidationResult call() throws Exception {
                return ActivityController.this.activityService.updateActivity(activity);
            }
        };
        updateActivityTask.setOnSucceeded(event ->
                this.mainController.displayValidationResult(this.updateActivityTask.getValue()));

        updateActivityTask.exceptionProperty().addListener(this.mainController.getTaskExceptionListener());
        updateActivityTask.setOnFailed(event ->
                ActivityController.this.mainController.showAlertDialog(event.toString()));
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
        task.setOnSucceeded(event -> this.mainController.displayValidationResult(
                new ValidationResult(ValidationResult.CustomErrorEnum.ACTIVITY_REMOVED)));
        task.exceptionProperty().addListener(this.mainController.getTaskExceptionListener());
        task.setOnFailed(event -> this.mainController.showAlertDialog(event.toString()));
        this.executorService.execute(task);
    }


    public void closeDialogIfExists() {
        if (this.bottomDialog != null) {
            this.bottomDialog.close();
        }
    }

    private Activity createdActivity;
    public void loadAddDialog(Region rootPane, StackPane bottomStackPane) {
        closeDialogIfExists();
        DialogElementsConstructor dialogElementsConstructor = new DialogElementsConstructor(messages);
        dialogElementsConstructor.getConfirmButton().setOnMouseClicked(event ->
                ActivityController.this.processSaveActivity(dialogElementsConstructor.getNewActivity(), rootPane));
        dialogElementsConstructor.getCancelButton().setOnMouseClicked(event ->
                ActivityController.this.bottomDialog.close());
        VBox overlayVBox = (VBox) dialogElementsConstructor.getContent();
        this.bottomDialog = new JFXDialog(bottomStackPane, overlayVBox, JFXDialog.DialogTransition.BOTTOM);
        this.bottomDialog.show();
    }

    private double getXCoordinate(Region rootPane) {
        Transform transform = rootPane.getLocalToSceneTransform();
        logger.debug("transform.getTy() = " +transform.getTy());
        return (rootPane.getWidth() / 2) - (CONFIRMATION_POPUP_PREF_WIDTH/2) - transform.getTx();
    }

    private static final double CONFIRMATION_POPUP_PREF_WIDTH = 150;
    public void processSaveActivity(Activity createdActivity, Region rootPane) {
        if (createdActivity.getName().equals("") || createdActivity.getName() == null) {
            return;
        }
        if (createdActivity.getDescription().equals("")) {
            this.createdActivity = createdActivity;
            this.initEmptyDescriptionConfirmationPopup(ActivityMethodType.CREATE);
            this.confirmationPopup.setPrefWidth(CONFIRMATION_POPUP_PREF_WIDTH);
            this.confirmationPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,
                    getXCoordinate(rootPane), 50);
        } else {
            ActivityController.this.addNewActivity(createdActivity);
        }
        this.bottomDialog.close();
    }

    public void initEmptyDescriptionConfirmationPopup(ActivityMethodType methodType) {
        this.confirmationPopup = new ConfirmationPopup(messages.getString("empty_description_confirm_label"),
                this.bottomDialog, this.messages);
        if (methodType == ActivityMethodType.CREATE) {
            this.confirmationPopup.getConfirmButton().setOnAction(e -> {
                this.addNewActivity(this.createdActivity);
                this.confirmationPopup.close();
            });
        } else if (methodType == ActivityMethodType.UPDATE) {
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
            addActivityTask.setOnSucceeded(event ->
                    this.mainController.displayValidationResult(this.addActivityTask.getValue()));
            addActivityTask.exceptionProperty().addListener(this.mainController.getTaskExceptionListener());
            this.executorService.execute(addActivityTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}