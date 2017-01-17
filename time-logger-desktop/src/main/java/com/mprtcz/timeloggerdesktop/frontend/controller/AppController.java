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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mprtcz.timeloggerdesktop.frontend.controller.AppController.BottomButton.*;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class AppController implements MainController {
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
    private JFXButton editActivityButton;
    @FXML
    private JFXButton addActivityButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private HBox addActivityHBox;

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
    private Map<BottomButton, JFXButton> bottomButtons;
    private LocalDateTime latestRecord;
    private ResourceBundle messages;
    private StyleSetter styleSetter;

    private Language language;
    private ExecutorService executorService;
    private static final int MAX_THREADS = 12;


    @FXML
    private void initialize() {
        this.initializeExecutor();
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

    private void initializeExecutor() {
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    private void initializeListViewController() {
        this.activityListController = new ActivityListController(this, this.activityNamesList,
                this.activityService, this.executorService);
        this.activityNamesList.prefWidthProperty().bind(this.borderPane.widthProperty());
        this.activityListController.populateListView();
        this.activityListController.setUpListViewListener();
        this.activityListController.setListViewFactory();
    }

    private void initializeActivityController() {
        this.activityController = new ActivityController(this, this.activityService, this.messages, this.executorService);
    }

    private void initializeSettingsService() {
        try {
            this.settingsService = new SettingsService(new DatabaseSettingsDao());
        } catch (SQLException e) {
            logger.info("database setting exception = {}", e.toString());
        }
    }

    private void initializeRecordController() {
        this.recordController = new RecordController(this, this.recordService, this.addRecordPopup, this.executorService);
    }

    private void initializeSettingsController() {
        this.settingsController = new SettingsController(
                this, this.messages, this.settingsService, this.borderPane, this.executorService);
    }

    private void initializeCanvasController() {
        this.canvasController = new CanvasController();
    }

    @FXML
    public void onAddRecordButtonClicked() {
        try {
            this.recordController.showAddRecordPopup(
                    this.activityNamesList, this.latestRecord, this.messages, this.borderPane);
        } catch (Exception e) {
            e.printStackTrace();
            this.displayException(e);
        }
    }

    @FXML
    public void onRemoveActivityButtonClicked() {
        this.closeDialogIfExists();
        Activity selectedActivity = this.activityNamesList.getSelectionModel().getSelectedItem();
        this.activityController.initActivityRemoveConfirmationPopup(this.bottomStackPane, selectedActivity);
        this.updateGUI();
    }

    @FXML
    public void onEditButtonClicked() {
        this.closeDialogIfExists();
        this.activityController.showEditActivityDialog(this.activityNamesList, this.bottomStackPane);
    }

    @FXML
    public void onAddActivityButtonClicked() {
        this.closeDialogIfExists();
        this.activityController.loadAddDialog(this.borderPane, this.bottomStackPane);
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
        this.executorService.execute(task);
    }

    private void applySettings(AppSettings settings, boolean isInitializing) {
        logger.info("applying settings = {}", settings.toString());
        logger.info("isInitializing = {}", isInitializing);
        logger.info("this.isFirstRun = {}", isFirstRun);

        this.canvas.setVisible(settings.isGraphicVisible());
        this.canvasController.setVisibleDays(settings.getNumberOfVisibleDays());
        this.canvasController.setHeadersVisibility(settings.isHeadersVisible());
        this.updateGUI();


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
                this.setOnClose();
                return; //breaking the language initialization loop
            }
            try {
                logger.info("setting the new Root ");
                executorService.shutdownNow();
                scene.setRoot(FXMLLoader.load(AppController.this.getClass().getResource("/fxmls/mainMenu.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
                AppController.this.displayException(e);
            }
            this.language = settings.getLanguage();
            isFirstRun = false;
        }
    }

    private void setOnClose() {
        Stage stage = (Stage) this.borderPane.getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            logger.info("shutting down... ");
            AppController.this.executorService.shutdown();
            try {
                AppController.this.executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.info("Exception on shutdown: " + e.toString());
                e.printStackTrace();
            }
        });
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
        this.bottomButtons.put(ADD_RECORD, this.addRecordButton);
        this.bottomButtons.put(REMOVE_ACTIVITY, this.removeActivityButton);
        this.bottomButtons.put(EDIT_ACTIVITY, this.editActivityButton);
        this.bottomButtons.put(SETTINGS, this.settingsButton);
        this.styleSetter = new StyleSetter();
        styleSetter.getListViewControlsDependants().add(this.addRecordButton);
        styleSetter.getListViewControlsDependants().add(this.removeActivityButton);
        styleSetter.getListViewControlsDependants().add(this.editActivityButton);
    }

    public enum BottomButton {
        ADD_RECORD,
        REMOVE_ACTIVITY,
        EDIT_ACTIVITY,
        SETTINGS
    }

    private void setAdditionalStyles() {
        JFXDepthManager.setDepth(this.canvas, 1);
        StyleSetter.setBottomPanelStyle(this.bottomHBox);
        JFXDepthManager.setDepth(this.bottomHBox, 5);
        this.styleSetter.setVisibility(false);
        StyleSetter.setBottomButtonContent(this.bottomButtons, this.messages);
        StyleSetter.stylizeButton(this.addActivityButton, new ImageView(StyleSetter.ADD_ICON));
        this.addActivityHBox.maxWidthProperty().bind(this.activityNamesList.widthProperty());
    }

    public void displayValidationResult(ValidationResult result) {
        if (result != null) {
            if (result.isErrorFree()) {
                System.out.println("result.getCustomErrorEnumList() = " + result.getCustomMessage());
                this.showInfoDialog(result.getCustomErrorEnum().getValue());
                this.updateGUI();
            } else {
                this.showAlertDialog(result.getEnumMessage());
            }
        }
    }

    private void displayException(Throwable e) {
        this.showAlertDialog(e.getMessage());
    }

    public void showAlertDialog(String value) {
        this.showDialog(value, MessageType.ALERT);
    }

    private void showInfoDialog(String value) {
        this.showDialog(value, MessageType.INFO);
    }

    private void showDialog(String value, MessageType type) {
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
            AppController.this.showAlertDialog(event.toString());
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
            AppController.this.showAlertDialog(exception.getMessage());
        });
    }

    private void updateGUI() {
        this.getTableData();
        this.activityListController.populateListView();
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
        this.executorService.execute(task);
    }

    private void drawDataOnCanvas(HoursData hoursData) {
        if (hoursData.getHours().size() > 0) {
            canvasController.drawArrayOnCanvas(hoursData.getHoursArray(), this.canvas);
            this.latestRecord = hoursData.getLatest();
        }
    }

    public void exportData() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                AppController.this.activityService.exportDataToFile();
                return null;
            }
        };
        task.setOnSucceeded(event -> AppController.this.displayValidationResult(
                new ValidationResult(ValidationResult.CustomErrorEnum.DATA_EXPORTED)));
        this.addTaskExceptionListener(task);
        this.executorService.execute(task);
    }

    public ChangeListener<Activity> getListViewChangeListener() {
        return (observable, oldValue, newValue) -> {
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
        this.showInfoDialog(item.getDescription());
    }

    public ChangeListener<Throwable> getTaskExceptionListener() {
        return (observable, oldValue, newValue) -> {
            Exception exception = (Exception) newValue;
            exception.printStackTrace();
            AppController.this.showAlertDialog(exception.getMessage());
        };
    }

    public void openFileChooser() {
        Stage stage = (Stage) this.borderPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xml files", "*.xml"));
        fileChooser.setInitialDirectory(new File("./"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            processImportedFile(selectedFile);
        }
    }

    private void processImportedFile(File file) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                AppController.this.activityService.importDataFromFile(file);
                return null;
            }
        };
        task.setOnSucceeded(event -> this.updateGUI());
        task.setOnFailed(event -> getOnFailedTaskEventHandler());
        this.addTaskExceptionListener(task);
        this.executorService.execute(task);
    }
}
