package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.service.RecordService;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.AddRecordPopup;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.scene.transform.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class RecordController {
    private Logger logger = LoggerFactory.getLogger(RecordController.class);

    private RecordService recordService;
    private AddRecordPopup addRecordPopup;
    private ExecutorService executorService;
    private MainController mainController;

    RecordController(MainController mainController,
                     RecordService recordService,
                     AddRecordPopup addRecordPopup,
                     ExecutorService executorService) {
        this.recordService = recordService;
        this.addRecordPopup = addRecordPopup;
        this.mainController = mainController;
        this.executorService = executorService;
    }

    void showAddRecordPopup(JFXListView<Activity> activityNamesList,
                            LocalDateTime latestRecord, ResourceBundle messages, Region rootPane) throws Exception {
        logger.info("showAddRecordPopup()");
        this.closePopupIfExists();
        this.addRecordPopup = new AddRecordPopup(activityNamesList.getSelectionModel().getSelectedItem(),
                latestRecord, messages);
        this.addRecordPopup.getOkButton().setOnAction(event -> {
            RecordController.this.addRecordPopup.close();
            saveRecord(RecordController.this.addRecordPopup.getObjectToValidate());
        });
        this.addRecordPopup.setSource(activityNamesList);
        Transform transform = activityNamesList.getLocalToSceneTransform();
        double xOffset = (rootPane.getWidth() / 2) - (AddRecordPopup.WIDTH / 2) - (transform.getTx());
        this.addRecordPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, xOffset, 10);
    }

    private void closePopupIfExists() {
        if (this.addRecordPopup != null) {
            this.addRecordPopup.close();
        }
    }

    private Task<ValidationResult> saveRecordTask;
    private void saveRecord(RecordValidator.ValidationObject recordToValidate) {
        logger.info("recordToValidate = {}", recordToValidate);
        this.saveRecordTask = new Task<ValidationResult>() {
            @Override
            protected ValidationResult call() throws Exception {
                return RecordController.this.recordService.addNewRecord(recordToValidate);
            }
        };
        saveRecordTask.setOnSucceeded(event ->
                RecordController.this.mainController.displayValidationResult(saveRecordTask.getValue()));
        saveRecordTask.setOnFailed(event -> RecordController.this.mainController.showAlertDialog(event.toString()));
        saveRecordTask.exceptionProperty().addListener(this.mainController.getTaskExceptionListener());
        this.executorService.execute(saveRecordTask);
    }
}