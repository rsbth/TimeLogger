package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.service.RecordService;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.AddRecordPopup;
import com.mprtcz.timeloggerdesktop.frontend.utils.ResultEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class RecordController {
    private Logger logger = LoggerFactory.getLogger(RecordController.class);

    private RecordService recordService;
    private AddRecordPopup addRecordPopup;
    private ChangeListener<Throwable> exceptionListener;
    private EventHandler<WorkerStateEvent> onFailedTaskEventHandler;
    private ResultEventHandler<Event> onSucceededRecordAddEventHandler;

    private RecordController(RecordService recordService, AddRecordPopup addRecordPopup,
                             ChangeListener<Throwable> exceptionListener,
                             EventHandler<WorkerStateEvent> onFailedTaskEventHandler,
                             ResultEventHandler<Event> onSucceededRecordAddEventHandler) {
        this.recordService = recordService;
        this.addRecordPopup = addRecordPopup;
        this.exceptionListener = exceptionListener;
        this.onFailedTaskEventHandler = onFailedTaskEventHandler;
        this.onSucceededRecordAddEventHandler = onSucceededRecordAddEventHandler;
    }

    public void showAddRecordPopup(JFXListView<Activity> activityNamesList,
                                   LocalDateTime latestRecord, ResourceBundle messages) throws Exception {
        logger.info("showAddRecordPopup()");
        this.addRecordPopup = new AddRecordPopup(activityNamesList.getSelectionModel().getSelectedItem(),
                latestRecord, messages);
        this.addRecordPopup.getOkButton().setOnAction(event -> {
            RecordController.this.addRecordPopup.close();
            saveRecord(RecordController.this.addRecordPopup.getObjectToValidate());
        });
        this.addRecordPopup.setSource(activityNamesList);
        this.addRecordPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 50, 10);
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
        saveRecordTask.setOnSucceeded(this.addRecordSucceeded());
        saveRecordTask.setOnFailed(this.onFailedTaskEventHandler);
        saveRecordTask.exceptionProperty().addListener(this.exceptionListener);
        new Thread(saveRecordTask).start();
    }

    private EventHandler<WorkerStateEvent> addRecordSucceeded() {
        return (WorkerStateEvent event) -> {
            RecordController.this.onSucceededRecordAddEventHandler.setResult(this.saveRecordTask.getValue());
            RecordController.this.onSucceededRecordAddEventHandler.handle(event);
        };
    }

    public static class RecordControllerBuilder {
        private RecordService recordService;
        private AddRecordPopup addRecordPopup;
        private ChangeListener<Throwable> exceptionListener;
        private EventHandler<WorkerStateEvent> onFailedTaskEventHandler;
        private ResultEventHandler<Event> onSucceededRecordAddEventHandler;

        public RecordControllerBuilder(RecordService recordService, AddRecordPopup addRecordPopup) {
            this.recordService = recordService;
            this.addRecordPopup = addRecordPopup;
        }

        public RecordControllerBuilder exceptionListener(ChangeListener<Throwable> exceptionListener) {
            this.exceptionListener = exceptionListener;
            return this;
        }

        public RecordControllerBuilder onFailedTaskEventHandler(EventHandler<WorkerStateEvent> onFailedTaskEventHandler) {
            this.onFailedTaskEventHandler = onFailedTaskEventHandler;
            return this;
        }

        public RecordControllerBuilder onSucceededRecordAddEventHandler(
                ResultEventHandler<Event> onSucceededRecordAddEventHandler) {
            this.onSucceededRecordAddEventHandler = onSucceededRecordAddEventHandler;
            return this;
        }

        public RecordController getInstance() {
            return new RecordController(
                    this.recordService,
                    this.addRecordPopup,
                    this.exceptionListener,
                    this.onFailedTaskEventHandler,
                    this.onSucceededRecordAddEventHandler
            );
        }
    }
}
