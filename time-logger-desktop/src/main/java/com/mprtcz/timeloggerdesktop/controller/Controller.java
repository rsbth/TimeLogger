package com.mprtcz.timeloggerdesktop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.mprtcz.timeloggerdesktop.model.LabelsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class Controller {

    @FXML
    private JFXDatePicker startDatePicker;
    @FXML
    private JFXDatePicker endTimePicker;
    @FXML
    private JFXDatePicker endDatePicker;
    @FXML
    private JFXButton addRecordButton;
    @FXML
    private JFXListView<?> activityNamesList;
    @FXML
    private Label programNameLabel;



    @FXML
    void onAddRecordButtonClicked() {

    }

    @FXML
    void onEndTimeChanged() {
        System.out.println(this.endTimePicker.getTime());
    }

    @FXML
    private void initialize() {
        System.out.println("initialized");
        this.setLabels();
    }


    private void setLabels() {
        this.addRecordButton.setText(LabelsModel.buttonLabel);
        this.startDatePicker.setPromptText(LabelsModel.startDateLabel);
        this.endDatePicker.setPromptText(LabelsModel.endDateLabel);
    }
}
