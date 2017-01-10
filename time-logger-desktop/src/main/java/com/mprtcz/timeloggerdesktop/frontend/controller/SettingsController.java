package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.SettingsPopup;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;

import java.util.ResourceBundle;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class SettingsController {
    private ResourceBundle messages;

    public SettingsController(ResourceBundle messages) {
        this.messages = messages;
    }

    public void loadSettingsMenu(Region popupSource, EventHandler confirmButtonHandler) {
        SettingsPopup settingsPopup = new SettingsPopup(popupSource, this.messages);
        settingsPopup.getConfirmButton().addEventHandler(ActionEvent.ACTION, confirmButtonHandler);
        settingsPopup.getConfirmButton().addEventHandler(ActionEvent.ACTION, getApplySettingsEventHandler());
        settingsPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT);
    }

    private  EventHandler getApplySettingsEventHandler() {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                //backend new settings saving;
            }
        };
    }
}
