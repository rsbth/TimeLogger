package com.mprtcz.timeloggerdesktop.controller;

import com.jfoenix.controls.*;
import com.mprtcz.timeloggerdesktop.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.dao.DatabaseActivityCustomDao;
import com.mprtcz.timeloggerdesktop.model.Activity;
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
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

    private Service service;
    private String newActivityName;
    private String newActivityDescription;
    private static final int SNACKBAR_DURATION = 5000; //[ms]
    private static final String BACKGROUND_COLOR = "#F4F4F4";


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
            new Thread(task).start();
        } catch (Exception e) {
            e.printStackTrace();
            showSnackbar(e.getMessage());
        }
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
        this.setLabels();
        initializeService();
        populateListView();
        initAddActivityPopup();
        initEmptyDescriptionConfPopup();
        setUpListViewListener();
        initializeDateTimeControls();
        setListViewFactory();
        this.addActivityButton.setShape(new Circle(8));
    }

    private void initializeService() {
        try {
            CustomDao DAO_PROVIDER = new DatabaseActivityCustomDao();
            this.service = new Service(new ActivityValidator(), new RecordValidator(), DAO_PROVIDER);
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
        this.programNameLabel.setText(LabelsModel.PROGRAM_NAME_LABEL);
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
            new Thread(task).start();
        } catch (Exception e) {
            e.printStackTrace();
            displayException(e);
        }
        System.out.println("items" + this.activityNamesList.getChildrenUnmodifiable());
    }

    private void initAddActivityPopup() {
        this.addNewActivityPopup = new JFXPopup();
        VBox vBox = generatePopupContent();
        this.setUpPopupProperties(this.addNewActivityPopup, vBox, this.addActivityButton);
    }

    private void addNewActivity(String name, String description) {
        System.out.println("name = " + name);
        try {
            Task<ValidationResult> task = new Task<ValidationResult>() {
                @Override
                protected ValidationResult call() throws Exception {
                    System.out.println("name = " + name);
                    return Controller.this.service.addActivity(name, description);
                }
            };
            task.setOnSucceeded(event -> {
                Controller.this.displayValidationResult(task.getValue());
                Controller.this.populateListView();
            });
            new Thread(task).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        HBox.setMargin(confirmAddButton, new Insets(5));
        HBox.setMargin(cancelAddButton, new Insets(5));
        HBox.setMargin(newActivityNameTextField, new Insets(5));
        HBox.setMargin(newActivityDescriptionTextField, new Insets(5));
        VBox vBox = new VBox(newActivityNameTextField, newActivityDescriptionTextField, hBox);
        vBox.setBackground(this.getBackgroundOfColor(BACKGROUND_COLOR));
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
        HBox.setMargin(confirmAddButton, new Insets(5));
        HBox.setMargin(cancelAddButton, new Insets(5));
        HBox.setMargin(label, new Insets(5));
        VBox vBox = new VBox(label, hBox);
        vBox.setBackground(this.getBackgroundOfColor(BACKGROUND_COLOR));
        this.setUpPopupProperties(this.emptyDescConfPopup, vBox, this.addActivityButton);
    }

    private Background getBackgroundOfColor(String color) {
        return new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY));
    }

    private void setStyleOfConfirmCancelButtons(JFXButton confirmButton, JFXButton cancelButton) {
        confirmButton.setPadding(new Insets(10));
        cancelButton.setPadding(new Insets(10));
        confirmButton.setRipplerFill(Paint.valueOf("darkgreen"));
        cancelButton.setRipplerFill(Paint.valueOf("red"));
        cancelButton.setStyle("-fx-background-color: hotpink;");
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
            if (newValue != null) {
                Controller.this.showSnackbar(newValue.getDescription());
            }
        });
    }

    private void showSnackbar(String value) {
        if (value == null || Objects.equals(value, "")) {
            return;
        }
        if (this.activityDetailSnackbar.getPopupContainer() != null) {
            this.activityDetailSnackbar.unregisterSnackbarContainer(borderPane);
        }
        System.out.println("is Visible " + this.activityDetailSnackbar.isHover());
        this.activityDetailSnackbar = new JFXSnackbar(borderPane);
        EventHandler<Event> eventHandler = event ->
                Controller.this.activityDetailSnackbar.unregisterSnackbarContainer(borderPane);

        System.out.println("Controller.showSnackbar");
        this.activityDetailSnackbar.show(StringConverter.insertLineSeparator(value, 50),
                "X", SNACKBAR_DURATION, eventHandler);
    }

    private void initializeDateTimeControls() {
        this.startDatePicker.setValue(LocalDate.now());
        this.endDatePicker.setValue(LocalDate.now());
        this.startTimePicker.setTime(LocalTime.of(0, 0));
        this.endTimePicker.setTime(LocalTime.of(0, 0));
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
                            HBox container = new HBox();
                            container.setMouseTransparent(true);
                            Label label = new Label(item.getName());
                            label.setBackground(getBackgroundOfColor(item.getColor()));
                            container.getChildren().add(label);
                            container.setBackground(getBackgroundOfColor(item.getColor()));
                            setGraphic(container);
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
    }

    private void displayValidationResult(ValidationResult validationResult) {
        showSnackbar(validationResult.getAllMessages());
    }
}
