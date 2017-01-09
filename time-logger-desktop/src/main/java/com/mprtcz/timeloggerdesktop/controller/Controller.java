package com.mprtcz.timeloggerdesktop.controller;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import com.mprtcz.timeloggerdesktop.customfxelements.*;
import com.mprtcz.timeloggerdesktop.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.dao.InMemoryCustomDao;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class Controller {

    @FXML
    private JFXListView<Activity> activityNamesList;
    @FXML
    private BorderPane borderPane;
    @FXML
    private StackPane bottomStackPane;
    @FXML
    private Canvas canvas;
    @FXML
    private HBox bottomHBox;
    @FXML
    private JFXButton addRecordButton;
    @FXML
    private JFXButton removeActivityButton;
    @FXML
    private JFXButton changeColorButton;
    @FXML
    private JFXButton addActivityButton;
    @FXML
    private HBox addActivityHbox;

    private ConfirmationPopup confirmationPopup;
    private JFXPopup detailsPopup;
    private JFXDialog bottomDialog;
    private Service service;
    private String newActivityName;
    private String newActivityDescription;
    private AddRecordPopup addRecordPopup;
    private Map<String, JFXButton> bottomButtons;

    private static final int LIST_VIEW_ROW_HEIGHT = 26;
    private static final int LIST_VIEW_ROW_PADDING = 20;


    @FXML
    private void initialize() {
        System.out.println("initialized");
        this.initializeService();
        this.populateListView();
        this.setUpListViewListener();
        this.setListViewFactory();
        this.collectBottomButtons();
        this.getTableData();
        this.setAdditionalStyles();
    }

    @FXML
    public void onAddRecordButtonClicked() {
        this.showAddRecordPopup();
    }

    @FXML
    public void onRemoveActivityButtonClicked() {
        this.initActivityRemoveConfirmationPopup();
    }

    @FXML
    public void onChangeColorButtonClicked() {
        this.loadColorDialog();
    }

    @FXML
    public void onAddActivityButtonClicked() {
        this.loadAddDialog();
    }

    private void initializeService() {
        try {
            CustomDao daoProvider = new InMemoryCustomDao();
            this.service = new Service(new ActivityValidator(), new RecordValidator(), daoProvider);
        } catch (Exception e) {
            e.printStackTrace();
            displayException(e);
        }
    }

    private void collectBottomButtons() {
        this.bottomButtons = new HashMap<>();
        this.bottomButtons.put("addRecord", this.addRecordButton);
        this.bottomButtons.put("removeActivity", this.removeActivityButton);
        this.bottomButtons.put("changeColor", this.changeColorButton);
    }

    private void setAdditionalStyles() {
        JFXDepthManager.setDepth(this.canvas, 1);
        StyleSetter.setBottomPanelStyle(this.bottomHBox);
        JFXDepthManager.setDepth(this.bottomHBox, 5);
        this.bottomHBox.setVisible(false);
        StyleSetter.setBottomButtonIcons(this.bottomButtons);
        StyleSetter.stylizeButton(this.addActivityButton, new ImageView(StyleSetter.ADD_ICON));
        this.addActivityHbox.maxWidthProperty().bind(this.activityNamesList.widthProperty());
    }

    private void updateChangedActivityAndUI(Activity activity) {
        if (activity == null) {
            return;
        }
        new Thread(() -> {
            try {
                Controller.this.service.updateActivity(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        populateListView();
        getTableData();
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
                this.confirmationPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,
                        event.getX(), event.getY());
            } else {
                this.addNewActivity(newActivityNameTextField.getText(), newActivityDescriptionTextField.getText());
            }
        }
        newActivityNameTextField.setText("");
        newActivityDescriptionTextField.setText("");
        this.bottomDialog.close();
    }

    private void initEmptyDescriptionConfirmationPopup() {
        this.confirmationPopup = new ConfirmationPopup(LabelsModel.EMPTY_DESCRIPTION_CONFIRM_LABEL, this.bottomDialog);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            this.addNewActivity(this.newActivityName, this.newActivityDescription);
            this.confirmationPopup.close();
        });
    }

    private void initActivityRemoveConfirmationPopup() {
        this.confirmationPopup = new ConfirmationPopup(LabelsModel.REMOVE_ACTIVITYPOPUP_LABEL, this.bottomStackPane);
        this.confirmationPopup.getConfirmButton().setOnAction(e -> {
            //TODO remove activity
            System.out.println("Remove Activity clicked");
            this.confirmationPopup.close();
        });
        this.confirmationPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 10, -10);
    }

    private void setUpListViewListener() {
        this.activityNamesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                closeAddRecordPopupIfExists();
                this.bottomHBox.setVisible(true);
                Controller.this.onListViewItemClicked(newValue);
            } else {
//                this.styleSetter.setVisibility(false);
                this.bottomHBox.setVisible(false);
            }
        });
    }

    private void onListViewItemClicked(Activity item) {
        this.showInfoPopup(item.getDescription());
    }

    private void showAddRecordPopup() {
        this.addRecordPopup = new AddRecordPopup(this.activityNamesList.getSelectionModel().getSelectedItem());
        this.addRecordPopup.getOkButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.this.addRecordPopup.close();
                saveRecord(Controller.this.addRecordPopup.getObjectToValidate());
            }
        });
        this.addRecordPopup.setSource(this.activityNamesList);
        this.addRecordPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, 50, 10);
    }

    private void closeAddRecordPopupIfExists() {
        if (this.addRecordPopup != null) {
            this.addRecordPopup.close();
        }
    }

    private void saveRecord(RecordValidator.ValidationObject validationObject) {
        try {
            Task<ValidationResult> task = new Task<ValidationResult>() {
                @Override
                protected ValidationResult call() throws Exception {
                    return Controller.this.service.addNewRecord(validationObject);
                }
            };
            task.setOnSucceeded(event -> {
                Controller.this.displayValidationResult(task.getValue());
                Controller.this.getTableData();
            });
            this.addTaskExceptionListener(task);
            new Thread(task).start();
        } catch (Exception e) {
            e.printStackTrace();
            this.showAlertPopup(e.getMessage());
        }
    }

    private void showInfoPopup(String value) {
        this.showPopup(value, false);
    }

    private void showPopup(String value, boolean isAlert) {
        if(value == null || Objects.equals(value, "")) {return;}
        if(this.detailsPopup != null) {
            this.detailsPopup.close();
        }
        this.detailsPopup = ConfirmationPopup.getTextPopup(StringConverter.insertLineSeparator(value, 50), this.bottomHBox, isAlert);
        this.detailsPopup.show(JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.RIGHT);
        this.detailsPopup.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Controller.this.detailsPopup.close();
            }
        });
    }

    private void showAlertPopup(String value) {
        this.showPopup(value, true);
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
        this.showAlertPopup(e.getMessage());
    }

    private void setActivityListItems(List<Activity> activities) {
        this.activityNamesList.setItems(FXCollections.observableList(activities));
        this.activityNamesList.setExpanded(true);
        this.activityNamesList.depthProperty().set(1);
        this.activityNamesList.setPrefHeight
                (activities.size() * LIST_VIEW_ROW_HEIGHT + 2 + activities.size() * LIST_VIEW_ROW_PADDING);
    }

    private void displayValidationResult(ValidationResult validationResult) {
        this.showAlertPopup(validationResult.getAllMessages());
    }

    private void loadAddDialog() {
        closeDialogIfExists();
        DialogElementsConstructor dialogElementsConstructor = new DialogElementsConstructor();
        dialogElementsConstructor.getConfirmButton().setOnMouseClicked(event ->
                Controller.this.processSaveActivity(dialogElementsConstructor.getNewActivityNameTextField(),
                        dialogElementsConstructor.getNewActivityDescriptionTextField(), true, event));
        dialogElementsConstructor.getCancelButton().setOnMouseClicked(event ->
                Controller.this.processSaveActivity(dialogElementsConstructor.getNewActivityNameTextField(),
                        dialogElementsConstructor.getNewActivityDescriptionTextField(), false, event));
        VBox overlayVBox = (VBox) dialogElementsConstructor.generateContent();
        this.bottomDialog = new JFXDialog(bottomStackPane, overlayVBox, JFXDialog.DialogTransition.BOTTOM);

        this.bottomDialog.show();
    }

    private void loadColorDialog() {
        closeDialogIfExists();
        Activity selectedActivity = this.activityNamesList.getSelectionModel().getSelectedItem();
        if (selectedActivity == null) {
            return;
        }
        ColorDialog colorDialog = new ColorDialog(selectedActivity);
        colorDialog.getCancelButton().setOnMouseClicked(event -> Controller.this.bottomDialog.close());
        colorDialog.getConfirmButton().setOnMouseClicked(
                event -> {
                    Controller.this.updateChangedActivityAndUI(colorDialog.getUpdatedColorActivity());
                    Controller.this.bottomDialog.close();
                });
        this.bottomDialog = new JFXDialog(bottomStackPane, colorDialog.createLayout(), JFXDialog.DialogTransition.BOTTOM);

        this.bottomDialog.show();
    }

    private void addTaskExceptionListener(Task<?> task) {
        task.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            Exception exception = (Exception) newValue;
            exception.printStackTrace();
            Controller.this.showAlertPopup(exception.getMessage());
        });
    }

    private void closeDialogIfExists() {
        if (this.bottomDialog != null) {
            this.bottomDialog.close();
        }
    }

    private void getTableData() {
        Task<DataRepresentation> task = new Task<DataRepresentation>() {
            @Override
            protected DataRepresentation call() throws Exception {
                return Controller.this.service.getHours();
            }
        };
        task.setOnSucceeded(event -> Controller.this.drawDataOnCanvas(task.getValue()));
        this.addTaskExceptionListener(task);
        new Thread(task).start();
    }

    private void drawDataOnCanvas(DataRepresentation dataRepresentation) {
        System.out.println("Controller.drawDataOnCanvas");
        dataRepresentation.drawOnCanvas(this.canvas);
    }
}
