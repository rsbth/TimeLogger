package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.service.RecordService;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.AddRecordPopup;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class RecordController {
    private RecordService recordService;
    private AddRecordPopup addRecordPopup;
    private ChangeListener<Throwable> exceptionListener;
    private EventHandler<WorkerStateEvent> onFailedTaskEventHandler;
    private EventHandler<WorkerStateEvent> onSucceededRecordAddEventHandler;

    public RecordController(RecordService recordService, AddRecordPopup addRecordPopup,
                            ChangeListener<Throwable> exceptionListener,
                            EventHandler<WorkerStateEvent> onFailedTaskEventHandler,
                            EventHandler<WorkerStateEvent> onSucceededRecordAddEventHandler) {
        this.recordService = recordService;
        this.addRecordPopup = addRecordPopup;
        this.exceptionListener = exceptionListener;
        this.onFailedTaskEventHandler = onFailedTaskEventHandler;
        this.onSucceededRecordAddEventHandler = onSucceededRecordAddEventHandler;
    }

    public void showAddRecordPopup(JFXListView<Activity> activityNamesList,
                                   LocalDateTime latestRecord, ResourceBundle messages)
            throws Exception {
        this.addRecordPopup = new AddRecordPopup(activityNamesList.getSelectionModel().getSelectedItem(),
                latestRecord, messages);
        this.addRecordPopup.getOkButton().setOnAction(event -> {
            RecordController.this.addRecordPopup.close();
            saveRecord(RecordController.this.addRecordPopup.getObjectToValidate());
        });
        this.addRecordPopup.setSource(activityNamesList);
        this.addRecordPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 50, 10);
    }

    private void saveRecord(RecordValidator.ValidationObject recordToValidate) {
        Task<ValidationResult> task = new Task<ValidationResult>() {
            @Override
            protected ValidationResult call() throws Exception {
                return RecordController.this.recordService.addNewRecord(recordToValidate);
            }
        };
        task.setOnSucceeded(this.onSucceededRecordAddEventHandler);
        task.setOnFailed(this.onFailedTaskEventHandler);
        task.exceptionProperty().addListener(this.exceptionListener);
        new Thread(task).start();
    }
}
