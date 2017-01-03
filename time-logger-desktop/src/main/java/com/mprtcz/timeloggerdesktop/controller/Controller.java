package com.mprtcz.timeloggerdesktop.controller;

import com.jfoenix.controls.*;
import com.mprtcz.timeloggerdesktop.handlers.ErrorHandler;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.LabelsModel;
import com.mprtcz.timeloggerdesktop.service.LoggingService;
import com.mprtcz.timeloggerdesktop.utilities.StringConverter;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class Controller {

    @FXML
    private JFXDatePicker startDatePicker;
    @FXML
    private JFXDatePicker endTimePicker;
    @FXML
    private JFXDatePicker startTimePicker;
    @FXML
    private JFXDatePicker endDatePicker;
    @FXML
    private JFXButton addRecordButton;
    @FXML
    private JFXButton addActivityButton;
    @FXML
    private JFXListView<Activity> activityNamesList;
    @FXML
    private Label programNameLabel;
    @FXML
    private JFXSnackbar activityDetailSnackbar;
    @FXML
    private BorderPane borderPane;

    private JFXPopup addNewActivityPopup;
    private JFXPopup emptyDescConfPopup;

    private LoggingService loggingService;
    private String newActivityName;
    private String newActivityDescription;
    private static final int SNACKBAR_DURATION = 5000; //[ms]

    @FXML
    void onAddRecordButtonClicked() {
    }

    @FXML
    void onMouseClickedListView(MouseEvent mouseEvent) {

    }

    @FXML
    void onAddActivityButtonCLicked() {
        this.addNewActivityPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT,
                addActivityButton.getLayoutX(), addActivityButton.getLayoutY());

    }

    @FXML
    void onEndTimeChanged() {
        System.out.println(this.endTimePicker.getTime());
    }

    @FXML
    private void initialize() {
        System.out.println("initialized");
        this.loggingService = new LoggingService();
        this.setLabels();
        populateListView();
        initAddActivityPopup();
        initEmptyDescriptionConfPopup();
        setUpListViewListener();
    }


    private void setLabels() {
        this.addRecordButton.setText(LabelsModel.SAVE_RECORD_BUTTON);
        this.startDatePicker.setPromptText(LabelsModel.START_DATE_LABEL);
        this.endDatePicker.setPromptText(LabelsModel.END_DATE_LABEL);
        this.startTimePicker.setPromptText(LabelsModel.START_HOUR_LABEL);
        this.endTimePicker.setPromptText(LabelsModel.END_HOUR_LABEL);
        this.programNameLabel.setText(LabelsModel.PROGRAM_NAME_LABEL);
    }

    private void populateListView() {
        List<Activity> list = this.loggingService.getActivities();
        this.activityNamesList.setItems(FXCollections.observableList(list));
        this.activityNamesList.setExpanded(true);
        this.activityNamesList.depthProperty().set(1);
    }

    private void initAddActivityPopup() {
        this.addNewActivityPopup = new JFXPopup();
        VBox vBox = generatePopupContent();
        this.setUpPopupProperties(this.addNewActivityPopup, vBox, this.addActivityButton);
    }

    private void addNewActivity(String name, String description) {
        ErrorHandler e = this.loggingService.addActivity(name, description);
        System.out.println("e = " + e);
        this.populateListView();
    }

    private VBox generatePopupContent() {
        JFXButton confirmAddButton = new JFXButton(LabelsModel.ADD_ACTIVITY_CONFIRM_BUTTON);
        JFXButton cancelAddButton = new JFXButton(LabelsModel.ADD_ACTIVITY_CANCEL_BUTTON);
        JFXTextField newActivityNameTextField = new JFXTextField();
        JFXTextField newActivityDescriptionTextField = new JFXTextField();
        confirmAddButton.setOnAction(e -> this.processSaveActivity(newActivityNameTextField,
                newActivityDescriptionTextField, true));
        cancelAddButton.setOnAction(e -> this.processSaveActivity(newActivityNameTextField,
                newActivityDescriptionTextField, false));
        setPopupContentsStyles(confirmAddButton, cancelAddButton, newActivityNameTextField, newActivityDescriptionTextField);
        HBox hBox = new HBox(confirmAddButton, cancelAddButton);
        VBox vBox = new VBox(newActivityNameTextField, newActivityDescriptionTextField, hBox);
        vBox.setBackground(this.getBackground());
        return vBox;
    }

    private void processSaveActivity(JFXTextField newActivityNameTextField, JFXTextField newActivityDescriptionTextField,
                                     boolean withActivitySave) {
        if (newActivityNameTextField.getText().equals("") && withActivitySave) {
            return;
        }
        if (withActivitySave) {
            if (newActivityDescriptionTextField.getText().equals("")) {
                this.newActivityName = newActivityNameTextField.getText();
                this.newActivityDescription = newActivityDescriptionTextField.getText();
                this.emptyDescConfPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT,
                        addActivityButton.getLayoutX(), addActivityButton.getLayoutY());
            } else {
                this.addNewActivity(newActivityNameTextField.getText(), newActivityDescriptionTextField.getText());
            }
        }
        newActivityNameTextField.setText("");
        newActivityDescriptionTextField.setText("");
        this.addNewActivityPopup.close();
    }

    private void setPopupContentsStyles(JFXButton confirmAddButton, JFXButton cancelAddButton,
                                        JFXTextField newActivityNameTextField, JFXTextField newActivityDescriptionTextField) {
        newActivityNameTextField.setPromptText(LabelsModel.ADD_ACTIVITY_TEXT_FIELD);
        newActivityDescriptionTextField.setPromptText(LabelsModel.ADD_ACTIVITY_DESCRIPTION_TEXT_FIELD);
        this.setStyleOfConfirmCancelButtons(confirmAddButton, cancelAddButton);
        newActivityNameTextField.setPadding(new Insets(10));
        newActivityDescriptionTextField.setPadding(new Insets(10));
    }

    private void initEmptyDescriptionConfPopup() {
        this.emptyDescConfPopup = new JFXPopup();
        JFXButton confirmAddButton = new JFXButton(LabelsModel.EMPTY_DESCRIPTION_CONFIRM_BUTTON);
        JFXButton cancelAddButton = new JFXButton(LabelsModel.EMPTY_DESCRIPTION_CANCEL_BUTTON);
        Label label = new Label(LabelsModel.EMPTY_DESCRIPTION_CONFIRM_LABEL);
        this.setStyleOfConfirmCancelButtons(confirmAddButton, cancelAddButton);
        label.setPadding(new Insets(10));
        confirmAddButton.setOnAction(e -> {
            this.addNewActivity(this.newActivityName, this.newActivityDescription);
            this.emptyDescConfPopup.close();
        });
        cancelAddButton.setOnAction(e -> this.emptyDescConfPopup.close());
        HBox hBox = new HBox(confirmAddButton, cancelAddButton);
        VBox vBox = new VBox(label, hBox);
        vBox.setBackground(this.getBackground());
        this.setUpPopupProperties(this.emptyDescConfPopup, vBox, this.addActivityButton);
    }

    private Background getBackground() {
        return new Background(new BackgroundFill(Color.web("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY));
    }

    private void setStyleOfConfirmCancelButtons(JFXButton confirmButton, JFXButton cancelButton) {
        confirmButton.setPadding(new Insets(10));
        cancelButton.setPadding(new Insets(10));
        confirmButton.setRipplerFill(Paint.valueOf("#40ff00"));
        cancelButton.setRipplerFill(Paint.valueOf("#ff84bd"));
    }

    private void setUpPopupProperties(JFXPopup popup, Pane pane, Control source) {
        popup.setContent(pane);
        popup.setSource(source);
        popup.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            popup.close();
        });
    }

    private void setUpListViewListener() {
        this.activityNamesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("ListView selection changed from oldValue = "
                    + oldValue + " to newValue = " + newValue);
            if (newValue != null) {
                Controller.this.showSnackbar(newValue.getDescription());
            }
        });
    }

    private void showSnackbar(String value) {
        if(this.activityDetailSnackbar.getPopupContainer() != null) {
            this.activityDetailSnackbar.unregisterSnackbarContainer(borderPane);
        }
        System.out.println("is Visible " + this.activityDetailSnackbar.isHover());
        this.activityDetailSnackbar = new JFXSnackbar(borderPane);

        EventHandler eventHandler = event -> Controller.this.activityDetailSnackbar.unregisterSnackbarContainer(borderPane);

        System.out.println("Controller.showSnackbar");
        this.activityDetailSnackbar.show(StringConverter.insertLineSeparator(value, 50), "X", SNACKBAR_DURATION, eventHandler);
    }


}
