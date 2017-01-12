package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;
import com.mprtcz.timeloggerdesktop.backend.activity.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.dao.DatabaseCustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.activity.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.backend.record.service.RecordService;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.settings.dao.DatabaseSettingsDao;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.model.Language;
import com.mprtcz.timeloggerdesktop.backend.settings.service.SettingsService;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.AddRecordPopup;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.DialogElementsConstructor;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.StyleSetter;
import com.mprtcz.timeloggerdesktop.frontend.utils.MessageType;
import com.mprtcz.timeloggerdesktop.frontend.utils.ResultEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class AppController {
    private Logger logger = LoggerFactory.getLogger(AppController.class);

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
    private JFXButton settingsButton;
    @FXML
    private HBox addActivityHbox;

    private static boolean isFirstRun = true;

    private ActivityListController activityListController;
    private ActivityController activityController;
    private RecordController recordController;
    private SettingsController settingsController;
    private CanvasController canvasController;
    private SettingsService settingsService;

    private JFXDialog bottomDialog;
    private ActivityService activityService;
    private RecordService recordService;
    private AddRecordPopup addRecordPopup;
    private Map<String, JFXButton> bottomButtons;
    private LocalDateTime latestRecord;
    private ResourceBundle messages;
    private StyleSetter styleSetter;

    private Language language;


    @FXML
    private void initialize() {
        this.initializeServices();
        this.initializeSettingsService();
        this.initializeLanguage();
        this.getSettingsAndApply(true);
        this.initializeListViewController();
        this.initializeActivityController();
        this.initializeRecordController();
        this.initializeSettingsController();
        this.initializeCanvasController();
        this.collectBottomButtons();
        this.getTableData();
        this.setAdditionalStyles();
    }

    private void initializeListViewController() {
        this.activityListController = new ActivityListController(this.activityNamesList, this.activityService,
                getTaskExceptionListener(), getListViewChangeListener());
        this.activityListController.populateListView();
        this.activityListController.setUpListViewListener();
        this.activityListController.setListViewFactory();
    }

    private void initializeActivityController() {
        ActivityController.ActivityControllerBuilder activityControllerBuilder =
                new ActivityController.ActivityControllerBuilder(this.activityService, this.messages);
        activityControllerBuilder.exceptionListener(getTaskExceptionListener())
                .onFailedTaskEventHandler(getOnFailedTaskEventHandler())
                .onSucceededActivityAddEventHandler(getOnTaskSucceedEventHandler());
        this.activityController = activityControllerBuilder.getInstance();
    }

    private void initializeSettingsService() {
        try {
            this.settingsService = new SettingsService(new DatabaseSettingsDao());
        } catch (SQLException e) {
            logger.info("database setting exception = {}", e.toString());
        }
    }

    private void initializeRecordController() {
        RecordController.RecordControllerBuilder recordControllerBuilder =
                new RecordController.RecordControllerBuilder(this.recordService, this.addRecordPopup);
        recordControllerBuilder.exceptionListener(getTaskExceptionListener())
                .onFailedTaskEventHandler(getOnFailedTaskEventHandler())
                .onSucceededRecordAddEventHandler(getOnTaskSucceedEventHandler());
        this.recordController = recordControllerBuilder.getInstance();
    }

    private void initializeSettingsController() {
        this.settingsController = new SettingsController(this.messages, this.settingsService, getTaskExceptionListener());
    }

    private void initializeCanvasController() {
        this.canvasController = new CanvasController();
    }

    @FXML
    public void onAddRecordButtonClicked() {
        try {
            this.recordController.showAddRecordPopup(this.activityNamesList, this.latestRecord, this.messages);
        } catch (Exception e) {
            e.printStackTrace();
            this.displayException(e);
        }
    }

    @FXML
    public void onRemoveActivityButtonClicked() {
        this.closeDialogIfExists();
        this.activityController.initActivityRemoveConfirmationPopup(this.bottomStackPane);
    }

    @FXML
    public void onChangeColorButtonClicked() {
        this.closeDialogIfExists();
        this.activityController.showColorDialog(this.activityNamesList, this.bottomStackPane);
    }

    @FXML
    public void onAddActivityButtonClicked() {
        this.closeDialogIfExists();
        this.activityController.loadAddDialog(this.bottomStackPane);
    }

    @FXML
    public void onSettingsButtonClicked() {
        this.closeDialogIfExists();
        this.settingsController.initializeSettingsMenu(this.activityNamesList, getApplySettingsEventHandler());
    }

    private ResultEventHandler<WorkerStateEvent> getApplySettingsEventHandler() {
        return new ResultEventHandler<WorkerStateEvent>() {
            ValidationResult result;

            @Override
            public void setResult(ValidationResult result) {
                this.result = result;
            }

            @Override
            public ValidationResult getResult() {
                return result;
            }

            @Override
            public void handle(Event event) {
                AppController.this.displayValidationResult(result);
                AppController.this.getSettingsAndApply(false);
            }
        };
    }

    private void getSettingsAndApply(boolean isInitial) {
        logger.info("applyingSettings ");
        Task<AppSettings> task = new Task<AppSettings>() {
            @Override
            protected AppSettings call() throws Exception {
                return AppController.this.settingsService.getSettings();
            }
        };
        task.setOnSucceeded(event -> this.applySettings(task.getValue(), isInitial));
        task.setOnFailed(event -> this.getOnFailedTaskEventHandler());
        addTaskExceptionListener(task);
        new Thread(task).start();
    }

    private void applySettings(AppSettings settings, boolean isInitializing) {
        logger.info("applying settings = {}", settings.toString());
        logger.info("isInitializing = {}", isInitializing);
        logger.info("this.isFirstRun = {}", isFirstRun);

        this.canvas.setVisible(settings.isGraphicVisible());
        this.canvasController.setVisibleDays(settings.getNumberOfVisibleDays());
        this.canvasController.setHeadersVisibility(settings.isHeadersVisible());
        this.getTableData();


        if (settings.getLanguage() != this.language) {
            logger.info("settings.getLanguageEnum().getName() = {}", settings.getLanguage().getName());
            logger.info("this.languageEnum = {}", this.language);
            this.language = settings.getLanguage();
            logger.info("this.languageEnum = {}", this.language);
            Scene scene = borderPane.getScene();
            Locale.setDefault(this.language.getLocale());
            ValidationResult.messages = ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
            if (isInitializing && !isFirstRun) {
                logger.info("(isInitializing && !isFirstRun) = {}", (isInitializing && !isFirstRun));
                logger.info("breaking the loop");
                return; //breaking the language initialization loop
            }
            try {
                logger.info("setting the new Root ");
                scene.setRoot(FXMLLoader.load(AppController.this.getClass().getResource("/fxmls/mainMenu.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
                AppController.this.displayException(e);
            }
            this.language = settings.getLanguage();
            isFirstRun = false;
        }
    }

    private void initializeLanguage() {
        Locale locale = Locale.getDefault();
        this.messages = ResourceBundle.getBundle("MessagesBundle", locale);
    }

    private void initializeServices() {
        try {
            CustomDao daoProvider = new DatabaseCustomDao();
            this.activityService = new ActivityService(new ActivityValidator(), daoProvider);
            this.recordService = new RecordService(new RecordValidator(), this.activityService);
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
        this.bottomButtons.put("settings", this.settingsButton);
        this.styleSetter = new StyleSetter();
        styleSetter.getListViewControlsDependants().add(this.addRecordButton);
        styleSetter.getListViewControlsDependants().add(this.removeActivityButton);
        styleSetter.getListViewControlsDependants().add(this.changeColorButton);
    }

    private void setAdditionalStyles() {
        JFXDepthManager.setDepth(this.canvas, 1);
        StyleSetter.setBottomPanelStyle(this.bottomHBox);
        JFXDepthManager.setDepth(this.bottomHBox, 5);
        this.styleSetter.setVisibility(false);
        StyleSetter.setBottomButtonContent(this.bottomButtons, this.messages);
        StyleSetter.stylizeButton(this.addActivityButton, new ImageView(StyleSetter.ADD_ICON));
        this.addActivityHbox.maxWidthProperty().bind(this.activityNamesList.widthProperty());
    }


    private ResultEventHandler getOnTaskSucceedEventHandler() {
        return new ResultEventHandler() {
            ValidationResult result;

            @Override
            public void setResult(ValidationResult result) {
                this.result = result;
            }

            @Override
            public ValidationResult getResult() {
                return this.result;
            }

            @Override
            public void handle(Event event) {
                AppController.this.displayValidationResult(result);
                AppController.this.activityListController.populateListView();
                AppController.this.getTableData();
            }
        };
    }

    private void displayValidationResult(ValidationResult result) {
        if (result != null) {
            if (result.isErrorFree()) {
                System.out.println("result.getCustomErrorEnumList() = " + result.getCustomMessage());
                this.showInfoPopup(result.getCustomErrorEnum().getValue());
            } else {
                this.showAlertPopup(result.getEnumMessage());
            }
        }
    }

    private void displayException(Throwable e) {
        this.showAlertPopup(e.getMessage());
    }

    private void displayException(String e) {
        this.showAlertPopup(e);
    }

    private void showAlertPopup(String value) {
        this.showPopup(value, MessageType.ALERT);
    }

    private void showInfoPopup(String value) {
        this.showPopup(value, MessageType.INFO);
    }

    private void showPopup(String value, MessageType type) {
        if (value == null || Objects.equals(value, "")) {
            return;
        }
        this.closeDialogIfExists();
        this.bottomDialog = new JFXDialog(this.bottomStackPane, DialogElementsConstructor.getTextLayout(value, type),
                JFXDialog.DialogTransition.BOTTOM);
        this.bottomDialog.show();
    }

    private void closeAddRecordPopupIfExists() {
        if (this.addRecordPopup != null) {
            this.addRecordPopup.close();
        }
    }

    private EventHandler getOnFailedTaskEventHandler() {
        return event -> {
            logger.info("event.toString() = {}", event.toString());
            AppController.this.showAlertPopup(event.toString());
        };
    }

    private void closeDialogIfExists() {
        if (this.bottomDialog != null) {
            this.bottomDialog.close();
        }
    }

    private void addTaskExceptionListener(Task<?> task) {
        task.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            Exception exception = (Exception) newValue;
            exception.printStackTrace();
            AppController.this.showAlertPopup(exception.getMessage());
        });
    }

    private void getTableData() {
        Task<HoursData> task = new Task<HoursData>() {
            @Override
            protected HoursData call() throws Exception {
                return AppController.this.activityService.getHoursData();
            }
        };
        task.setOnSucceeded(event -> AppController.this.drawDataOnCanvas(task.getValue()));
        this.addTaskExceptionListener(task);
        new Thread(task).start();
    }

    private void drawDataOnCanvas(HoursData hoursData) {
        if (hoursData.getHours().size() > 0) {
            canvasController.drawArrayOnCanvas(hoursData.getHoursArray(), this.canvas);
            this.latestRecord = hoursData.getLatest();
        }
    }

    private ChangeListener getListViewChangeListener() {
        return (ChangeListener<Activity>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
                closeAddRecordPopupIfExists();
                AppController.this.styleSetter.setVisibility(true);
                AppController.this.onListViewItemClicked(newValue);
            } else {
                AppController.this.styleSetter.setVisibility(false);
            }
        };
    }

    private void onListViewItemClicked(Activity item) {
        this.showInfoPopup(item.getDescription());
    }

    private ChangeListener<Throwable> getTaskExceptionListener() {
        return (observable, oldValue, newValue) -> {
            Exception exception = (Exception) newValue;
            exception.printStackTrace();
            AppController.this.showAlertPopup(exception.getMessage());
        };
    }
}
