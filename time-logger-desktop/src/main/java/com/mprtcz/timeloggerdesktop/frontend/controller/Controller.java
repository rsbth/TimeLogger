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
import com.mprtcz.timeloggerdesktop.backend.settings.dao.FileReadingDao;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.model.LanguageEnum;
import com.mprtcz.timeloggerdesktop.backend.settings.service.SettingsService;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.AddRecordPopup;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.DialogElementsConstructor;
import com.mprtcz.timeloggerdesktop.frontend.customfxelements.StyleSetter;
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
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class Controller {
    private Logger logger = LoggerFactory.getLogger(Controller.class);

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

    private LanguageEnum languageEnum;


    @FXML
    private void initialize() {
        this.initializeServices();
        this.initializeLanguage();
        this.initializeSettingsService();
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
        this.activityController = new ActivityController(this.activityService, getTaskExceptionListener(),
                getOnFailedTaskEventHandler(), getOnTaskSucceedEventHandler(), this.messages);
    }

    private void initializeSettingsService() {
        this.settingsService = new SettingsService(new FileReadingDao());
    }

    private void initializeRecordController() {
        this.recordController = new RecordController(this.recordService, this.addRecordPopup, getTaskExceptionListener(),
                getOnFailedTaskEventHandler(), getOnTaskSucceedEventHandler());
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
        this.activityController.initActivityRemoveConfirmationPopup(this.bottomStackPane);
    }

    @FXML
    public void onChangeColorButtonClicked() {
        this.activityController.showColorDialog(this.activityNamesList, this.bottomStackPane);
    }

    @FXML
    public void onAddActivityButtonClicked() {
        this.activityController.loadAddDialog(this.bottomStackPane);
    }


    @FXML
    public void onSettingsButtonClicked() {
        this.settingsController.initializeSettingsMenu(this.activityNamesList, getApplySettingsEventHandler());
    }

    private EventHandler getApplySettingsEventHandler() {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                Controller.this.getSettingsAndApply(false);
            }
        };
    }

    //will get new settings from backend and apply them to ui
    private void getSettingsAndApply(boolean isInitial) {
        System.out.println("applying settings....");
        logger.debug("applyingSettings ");
        Task<AppSettings> task = new Task<AppSettings>() {
            @Override
            protected AppSettings call() throws Exception {
                return Controller.this.settingsService.getSettings();
            }
        };
        task.setOnSucceeded(event -> this.applySettings(task.getValue(), isInitial));
        task.setOnFailed(event -> this.getOnFailedTaskEventHandler());
        addTaskExceptionListener(task);
        new Thread(task).start();
    }

    private void applySettings(AppSettings settings, boolean isInitializing) {
        logger.info("(settings.getLanguageEnum() != this.languageEnum) = {}", (settings.getLanguageEnum() != this.languageEnum));

        isFirstRun = false;
        if (settings.getLanguageEnum() != this.languageEnum) {
            logger.info("settings.getLanguageEnum().getName() = {}", settings.getLanguageEnum().getName());
            logger.info("this.languageEnum = {}", this.languageEnum);
            this.languageEnum = settings.getLanguageEnum();
            Scene scene = borderPane.getScene();
            Locale.setDefault(this.languageEnum.getLocale());
            if(isInitializing && !isFirstRun) {
                logger.info("breaking the loop");
                return; //breaking the language initialization loop
            }
            try {
                scene.setRoot(FXMLLoader.load(Controller.this.getClass().getResource("/fxmls/mainMenu.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
                Controller.this.displayException(e);
            }
            this.languageEnum = settings.getLanguageEnum();
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


    private EventHandler getOnTaskSucceedEventHandler() {
        return event -> {
            Controller.this.activityListController.populateListView();
            Controller.this.getTableData();
        };
    }

    private void displayException(Throwable e) {
        this.showAlertPopup(e.getMessage());
    }

    private void displayException(String e) {
        this.showAlertPopup(e);
    }

    private EventHandler getExceptionEventHandler() {
        return new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Controller.this.displayException(event.toString());
            }
        };
    }

    private void showAlertPopup(String value) {
        this.showPopup(value, true);
    }

    private void showInfoPopup(String value) {
        this.showPopup(value, false);
    }

    private void showPopup(String value, boolean isAlert) {
        if (value == null || Objects.equals(value, "")) {
            return;
        }
        this.closeDialogIfExists();
        this.bottomDialog = new JFXDialog(bottomStackPane, DialogElementsConstructor.getTextLayout(value, isAlert),
                JFXDialog.DialogTransition.BOTTOM);
        this.bottomDialog.show();
    }

    private void closeAddRecordPopupIfExists() {
        if (this.addRecordPopup != null) {
            this.addRecordPopup.close();
        }
    }

    private void displayValidationResult(ValidationResult validationResult) {
        this.showAlertPopup(validationResult.getAllMessages());
    }

    private EventHandler getOnFailedTaskEventHandler() {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println("Failed"); //TODO
            }
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
            Controller.this.showAlertPopup(exception.getMessage());
        });
    }

    private void getTableData() {
        Task<HoursData> task = new Task<HoursData>() {
            @Override
            protected HoursData call() throws Exception {
                return Controller.this.activityService.getHoursData();
            }
        };
        task.setOnSucceeded(event -> Controller.this.drawDataOnCanvas(task.getValue()));
        this.addTaskExceptionListener(task);
        new Thread(task).start();
    }

    private void drawDataOnCanvas(HoursData hoursData) {
        if (hoursData.getHours().size() > 0) {
            canvasController.calculatePositionsAndDraw(hoursData.getHours(), this.canvas);
            this.latestRecord = hoursData.getLatest();
        }
    }

    private ChangeListener getListViewChangeListener() {
        return (ChangeListener<Activity>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
                closeAddRecordPopupIfExists();
                Controller.this.styleSetter.setVisibility(true);
                Controller.this.onListViewItemClicked(newValue);
            } else {
                Controller.this.styleSetter.setVisibility(false);
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
            Controller.this.showAlertPopup(exception.getMessage());
        };
    }
}
