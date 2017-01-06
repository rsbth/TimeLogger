package com.mprtcz.timeloggerdesktop.controller;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.mprtcz.timeloggerdesktop.customfxelements.ConfirmationPopup;
import com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor;
import com.mprtcz.timeloggerdesktop.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.dao.DatabaseCustomDao;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.DataRepresentation;
import com.mprtcz.timeloggerdesktop.model.LabelsModel;
import com.mprtcz.timeloggerdesktop.service.Service;
import com.mprtcz.timeloggerdesktop.utilities.StringConverter;
import com.mprtcz.timeloggerdesktop.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.validators.ValidationResult;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    private JFXButton deleteActivityButton;
    @FXML
    private JFXButton addActivityButton;
    @FXML
    private JFXListView<Activity> activityNamesList;
//    @FXML
//    private Label programNameLabel;
    @FXML
    private Label summaryLabel;
    @FXML
    private Label startRecordLabel;
    @FXML
    private Label endRecordLabel;
    @FXML
    private JFXSnackbar activityDetailSnackbar;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox dataInsertionVBox;
    @FXML
    private StackPane topStackPane;
    @FXML
    private Canvas canvas;
    @FXML
    VBox startTimeVBox;
    @FXML
    VBox endTimeVBox;


    private ConfirmationPopup confirmationPopup;
    private JFXDialog addActivityDialog;

    private Service service;
    private String newActivityName;
    private String newActivityDescription;
    private static final int SNACKBAR_DURATION = 5000; //[ms]
    private static final String BACKGROUND_COLOR = "#F4F4F4";
    private static final String PRIMARY_COLOR = "rgb(33, 150, 243)";
    private static final String SECONDARY_COLOR = "#FF5252";
    private static final int LIST_VIEW_ROW_HEIGHT = 26;
    private static final int LIST_VIEW_ROW_PADDING = 20;


    @FXML
    void onAddRecordButtonClicked() {
        if (activityNamesList.getSelectionModel().selectedItemProperty().getValue() == null) {
            showSnackbar("Choose an activity");
            return;
        }
        try {
            Task<ValidationResult> task = new Task<ValidationResult>() {
                @Override
                protected ValidationResult call() throws Exception {
                    return Controller.this.service.addNewRecord(startTimePicker.getTime(), endTimePicker.getTime(),
                            startDatePicker.getValue(), endDatePicker.getValue(),
                            activityNamesList.getSelectionModel().selectedItemProperty().getValue());
                }
            };
            task.setOnSucceeded(event -> {
                Controller.this.displayValidationResult(task.getValue());
            });
            this.addTaskExceptionListener(task);
            new Thread(task).start();
            this.getTableData();
        } catch (Exception e) {
            e.printStackTrace();
            showSnackbar(e.getMessage());
        }
    }

    @FXML
    void onAddActivityButtonCLicked() {
        this.loadDialog();
    }

    @FXML
    void onEndTimeChanged() {
        System.out.println(this.endTimePicker.getTime());
    }

    @FXML
    void onDeleteActivityButtonClicked() {
        this.initActivityRemoveConfirmationPopup();
        this.confirmationPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 0, 0);
    }

    @FXML
    private void initialize() {
        System.out.println("initialized");
        this.setLabels();
        this.initializeService();
        this.populateListView();
        this.setUpListViewListener();
        this.initializeDateTimeControls();
        this.setListViewFactory();
        this.drawOnCanvas();
        this.deleteActivityButton.setStyle(getBackgroundStyle(SECONDARY_COLOR));
        this.deleteActivityButton.setVisible(false);
        this.addActivityButton.setShape(new Circle(8));
        this.dataInsertionVBox.setVisible(false);
        getTableData();
        setDepths();
        System.out.println("this.canvas.getParent() = " + this.canvas.getParent().getParent());
        this.canvas.widthProperty().bind(this.dataInsertionVBox.widthProperty());
        BorderPane borderPane = (BorderPane) this.canvas.getParent().getParent();
        System.out.println("borderPane.getWidth() = " + borderPane.getWidth());
    }

    private void initializeService() {
        try {
            CustomDao daoProvider = new DatabaseCustomDao();
            this.service = new Service(new ActivityValidator(), new RecordValidator(), daoProvider);
        } catch (Exception e) {
            e.printStackTrace();
            displayException(e);
        }
    }

    private void setLabels() {
        this.addRecordButton.setText(LabelsModel.SAVE_RECORD_BUTTON);
        this.startDatePicker.setPromptText(LabelsModel.START_DATE_LABEL);
        this.endDatePicker.setPromptText(LabelsModel.END_DATE_LABEL);
        this.startTimePicker.setPromptText(LabelsModel.START_HOUR_LABEL);
        this.endTimePicker.setPromptText(LabelsModel.END_HOUR_LABEL);
//        this.programNameLabel.setText(LabelsModel.PROGRAM_NAME_LABEL);
        this.startRecordLabel.setText(LabelsModel.START_RECORD_LABEL);
        this.endRecordLabel.setText(LabelsModel.END_RECORD_LABEL);
    }

    private void setDepths() {
//        this.programNameLabel.setBackground(DialogElementsConstructor.getBackgroundOfColor("white"));
//        JFXDepthManager.setDepth(this.programNameLabel, 1);
        JFXDepthManager.setDepth(this.startTimeVBox, 1);
        JFXDepthManager.setDepth(this.endTimeVBox, 1);
        JFXDepthManager.setDepth(this.summaryLabel, 1);
        JFXDepthManager.setDepth(this.canvas, 1);
    }

    private void populateListView() {
        try {
            Task<List<Activity>> task = new Task<List<Activity>>() {
                @Override
                protected List<Activity> call() throws Exception {
                    return Controller.this.service.getActivities();
                }
            };
            task.setOnSucceeded(event -> Controller.this.setActivityListItems(task.getValue()));
            task.setOnFailed(event -> System.out.println(task.exceptionProperty().toString()));
            this.addTaskExceptionListener(task);
            new Thread(task).start();
        } catch (Exception e) {
            e.printStackTrace();
            displayException(e);
        }
    }

    private void addNewActivity(String name, String description) {
        try {
            Task<ValidationResult> task = new Task<ValidationResult>() {
                @Override
                protected ValidationResult call() throws Exception {
                    return Controller.this.service.addActivity(name, description);
                }
            };
            task.setOnSucceeded(event -> {
                Controller.this.displayValidationResult(task.getValue());
                Controller.this.populateListView();
            });
            this.addTaskExceptionListener(task);
            new Thread(task).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSaveActivity(JFXTextField newActivityNameTextField, JFXTextField newActivityDescriptionTextField,
                                     boolean withActivitySave, MouseEvent event) {
        if (newActivityNameTextField.getText().equals("") && withActivitySave) {
            return;
        }
        if (withActivitySave) {
            if (newActivityDescriptionTextField.getText().equals("")) {
                this.newActivityName = newActivityNameTextField.getText();
                this.newActivityDescription = newActivityDescriptionTextField.getText();
                this.initEmptyDescriptionConfirmationPopup();
                System.out.println("event.getX() = " + event.getX());
                System.out.println("event.getY() = " + event.getY());
                this.confirmationPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,
                        event.getX(), event.getY());
            } else {
                this.addNewActivity(newActivityNameTextField.getText(), newActivityDescriptionTextField.getText());
            }
        }
        newActivityNameTextField.setText("");
        newActivityDescriptionTextField.setText("");
        this.addActivityDialog.close();
    }

    private void initEmptyDescriptionConfirmationPopup() {
        this.confirmationPopup = new ConfirmationPopup(LabelsModel.EMPTY_DESCRIPTION_CONFIRM_LABEL, this.addActivityDialog);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            this.addNewActivity(this.newActivityName, this.newActivityDescription);
            this.confirmationPopup.close();
        });
    }

    private void initActivityRemoveConfirmationPopup() {
        this.confirmationPopup = new ConfirmationPopup(LabelsModel.REMOVE_ACTIVITYPOPUP_LABEL, this.deleteActivityButton);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            //TODO remove activity
            System.out.println("Remove Activity clicked");
            this.confirmationPopup.close();
        });
    }

    private void setUpListViewListener() {
        this.activityNamesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Controller.this.showSnackbar(newValue.getDescription());
                Controller.this.deleteActivityButton.setVisible(true);
                this.dataInsertionVBox.setVisible(true);
                this.summaryLabel.setVisible(true);
                Controller.this.updateSummary();
            } else {
                Controller.this.deleteActivityButton.setVisible(false);
                this.dataInsertionVBox.setVisible(false);
                this.summaryLabel.setVisible(false);
            }
        });
    }

    private void showSnackbar(String value) {
        if (value == null || Objects.equals(value, "")) {
            return;
        }
        closeSnackBarIfExists();
        closeDialogIfExists();
        this.activityDetailSnackbar = new JFXSnackbar(borderPane);
        EventHandler<Event> eventHandler = event ->
                Controller.this.activityDetailSnackbar.unregisterSnackbarContainer(borderPane);
        this.activityDetailSnackbar.show(StringConverter.insertLineSeparator(value, 50),
                "X", SNACKBAR_DURATION, eventHandler);
    }

    private void initializeDateTimeControls() {
        this.startDatePicker.setValue(LocalDate.now());
        this.startDatePicker.setOnAction(getSummaryEventHandler());
        this.endDatePicker.setValue(LocalDate.now());
        this.endDatePicker.setOnAction(getSummaryEventHandler());
        this.startTimePicker.setTime(LocalTime.of(0, 0));
        this.startTimePicker.setOnMouseClicked(getSummaryEventHandler());
        this.startTimePicker.setOnHiding(getSummaryEventHandler());
        this.endTimePicker.setTime(LocalTime.of(0, 0));
        this.endTimePicker.setOnMouseClicked(getSummaryEventHandler());
        this.endTimePicker.setOnHiding(getSummaryEventHandler());
        this.summaryLabel.setVisible(false);
        Locale.setDefault(Locale.ENGLISH);
    }

    private void setListViewFactory() {
        this.activityNamesList.setCellFactory(new Callback<ListView<Activity>, ListCell<Activity>>() {
            @Override
            public ListCell<Activity> call(ListView<Activity> param) {
                return new JFXListCell<Activity>() {
                    @Override
                    public void updateItem(Activity item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setGraphic(DialogElementsConstructor.createListViewCellLayout(item));
                        }
                    }
                };
            }
        });
    }

    private void displayException(Exception e) {
        showSnackbar(e.getMessage());
    }

    private void setActivityListItems(List<Activity> activities) {
        this.activityNamesList.setItems(FXCollections.observableList(activities));
        this.activityNamesList.setExpanded(true);
        this.activityNamesList.depthProperty().set(1);
        this.activityNamesList.setPrefHeight
                (activities.size() * LIST_VIEW_ROW_HEIGHT + 2 + activities.size() * LIST_VIEW_ROW_PADDING);
    }

    private void displayValidationResult(ValidationResult validationResult) {
        showSnackbar(validationResult.getAllMessages());
    }

    private void drawOnCanvas() {
//        this.dataRepresentationCanvas.getGraphicsContext2D().fillRect(
//                0, 0, this.dataRepresentationCanvas.getWidth(), this.dataRepresentationCanvas.getHeight());
    }

    private String getBackgroundStyle(String color) {
        return "-fx-background-color: " + color + ";";
    }

    private void loadDialog() {
        closeSnackBarIfExists();
        DialogElementsConstructor dialogElementsConstructor = new DialogElementsConstructor();
        dialogElementsConstructor.getConfirmAddButton().setOnMouseClicked(event ->
                Controller.this.processSaveActivity(dialogElementsConstructor.getNewActivityNameTextField(),
                        dialogElementsConstructor.getNewActivityDescriptionTextField(), true, event));
        dialogElementsConstructor.getCancelAddButton().setOnMouseClicked(event ->
                Controller.this.processSaveActivity(dialogElementsConstructor.getNewActivityNameTextField(),
                        dialogElementsConstructor.getNewActivityDescriptionTextField(), false, event));
        VBox overlayVBox = dialogElementsConstructor.generateContent();
        this.addActivityDialog = new JFXDialog(topStackPane, overlayVBox, JFXDialog.DialogTransition.BOTTOM);

        this.addActivityDialog.show();
    }

    private void addTaskExceptionListener(Task<?> task) {
        task.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            Exception exception = (Exception) newValue;
            exception.printStackTrace();
            showSnackbar(exception.getMessage());
        });
    }

    private void closeSnackBarIfExists() {
        if (this.activityDetailSnackbar.getPopupContainer() != null) {
            this.activityDetailSnackbar.unregisterSnackbarContainer(borderPane);
        }
    }

    private void closeDialogIfExists() {
        if (this.addActivityDialog != null) {
            this.addActivityDialog.close();
        }
    }

    private void getTableData() {
        DataRepresentation dataRepresentation;
        Task<DataRepresentation> task = new Task<DataRepresentation>() {
            @Override
            protected DataRepresentation call() throws Exception {
                return Controller.this.service.getHours();
            }
        };
        task.setOnSucceeded(event -> {
            Controller.this.drawDataOnCanvas(task.getValue());
        });
        this.addTaskExceptionListener(task);
        new Thread(task).start();
    }

    private void drawDataOnCanvas(DataRepresentation dataRepresentation) {
        dataRepresentation.drawOnCanvas(this.canvas);
    }

    private void updateSummary() {
        System.out.println("UPDATED!");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.activityNamesList.getSelectionModel().selectedItemProperty().getValue().getName()).append("\n");
        stringBuilder.append(this.startDatePicker.getValue()).append(" ").append(this.startTimePicker.getTime()).append("\n");
        stringBuilder.append("- \n");
        stringBuilder.append(this.endDatePicker.getValue()).append(" ").append(this.endTimePicker.getTime()).append("\n");
        this.summaryLabel.setText(stringBuilder.toString());
    }

    private <T extends Event> EventHandler<T> getSummaryEventHandler() {
        return event -> Controller.this.updateSummary();
    }
}
