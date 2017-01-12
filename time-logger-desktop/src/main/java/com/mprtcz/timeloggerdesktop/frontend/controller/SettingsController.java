package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.service.SettingsService;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.SettingsPopup;
import com.mprtcz.timeloggerdesktop.frontend.utils.ResultEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class SettingsController {
    private Logger logger = LoggerFactory.getLogger(SettingsController.class);

    private ResourceBundle messages;
    private SettingsService settingsService;
    private ChangeListener<Throwable> exceptionListener;
    private ResultEventHandler<WorkerStateEvent> confirmButtonHandler;

    public SettingsController(ResourceBundle messages,
                              SettingsService settingsService,
                              ChangeListener<Throwable> exceptionListener) {
        this.messages = messages;
        this.settingsService = settingsService;
        this.exceptionListener = exceptionListener;
    }

    public void initializeSettingsMenu(Region popupSource, ResultEventHandler<WorkerStateEvent> confirmButtonHandler) {
        this.confirmButtonHandler = confirmButtonHandler;
        this.getSettings(popupSource);
    }

    Task<ValidationResult> appSettingsTask;
    private  EventHandler getApplySettingsEventHandler() {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    appSettingsTask = new Task<ValidationResult>() {
                        @Override
                        protected ValidationResult call() throws Exception {
                            return SettingsController.this.settingsService.updateSettings(
                                    SettingsController.this.settingsPopup.getSettingsObject());
                        }
                    };
                    appSettingsTask.setOnSucceeded(settingsValidationResult());
                    appSettingsTask.setOnFailed(event1 -> System.out.println(appSettingsTask.exceptionProperty().toString()));
                    appSettingsTask.exceptionProperty().addListener(SettingsController.this.exceptionListener);
                    new Thread(appSettingsTask).start();
                    SettingsController.this.settingsService.updateSettings(SettingsController.this.settingsPopup.getSettingsObject());
                } catch (Exception e) {
                    logger.info("exception while applying settings = {}", e.toString());
                }
            }
        };
    }

    private EventHandler<WorkerStateEvent> settingsValidationResult() {
        return (WorkerStateEvent event) -> {
            SettingsController.this.confirmButtonHandler.setResult(this.appSettingsTask.getValue());
            SettingsController.this.confirmButtonHandler.handle(event);
        };
    }

    public void getSettings(Region popupSource) {
        Task<AppSettings> task = new Task<AppSettings>() {
            @Override
            protected AppSettings call() throws Exception {
                return SettingsController.this.settingsService.getSettings();
            }
        };
        task.setOnSucceeded(event -> SettingsController.this.showSettingsPopup(task.getValue(), popupSource));
        task.setOnFailed(event -> System.out.println(task.exceptionProperty().toString()));
        task.exceptionProperty().addListener(this.exceptionListener);
        new Thread(task).start();
    }

    private SettingsPopup settingsPopup;
    private void showSettingsPopup(AppSettings currentSettings, Region popupSource) {
        this.settingsPopup = new SettingsPopup(popupSource, this.messages, currentSettings);
        this.settingsPopup.getConfirmButton().addEventHandler(ActionEvent.ACTION, getApplySettingsEventHandler());
        this.settingsPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT);
    }


}
