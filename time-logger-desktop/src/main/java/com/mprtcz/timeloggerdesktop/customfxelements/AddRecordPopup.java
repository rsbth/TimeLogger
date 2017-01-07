package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.effects.JFXDepthManager;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.validators.RecordValidator;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import lombok.Getter;

import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundOfColor;

/**
 * Created by mprtcz on 2017-01-07.
 */
@Getter
public class AddRecordPopup extends JFXPopup {

    private JFXDatePicker startDatePicker;
    private JFXDatePicker endDatePicker;
    private JFXDatePicker startTimePicker;
    private JFXDatePicker endTimePicker;
    private Label startTimeLabel;
    private Label endTimeLabel;
    private JFXButton okButton;
    private JFXButton closeButton;
    private Label summaryLabel;
    private Activity activity;

    public AddRecordPopup(Activity activity) {
        this.activity = activity;
        this.initializeElements();
        this.addListeners();
        initializeTimeElements();
        this.setContent(this.createLayout());
    }

    private VBox createLayout() {
        Insets defaultInsets = new Insets(10);
        VBox startVBox = generateCardLayout(this.startTimePicker, this.startDatePicker, this.startTimeLabel);
        VBox endVBox = generateCardLayout(this.endTimePicker, this.endDatePicker, this.endTimeLabel);
        startVBox.setBackground(getBackgroundOfColor("white"));
        endVBox.setBackground(getBackgroundOfColor("white"));
        startVBox.setAlignment(Pos.CENTER);
        endVBox.setAlignment(Pos.CENTER);
        JFXDepthManager.setDepth(startVBox, 1);
        JFXDepthManager.setDepth(endVBox, 1);
        JFXDepthManager.setDepth(this.summaryLabel, 1);
        JFXDepthManager.setDepth(this.okButton, 1);
        JFXDepthManager.setDepth(this.closeButton, 1);
        HBox buttonsHBox = new HBox(this.okButton, this.closeButton);
        buttonsHBox.setAlignment(Pos.CENTER);
        HBox.setMargin(okButton, defaultInsets);
        HBox.setMargin(closeButton, defaultInsets);
        VBox vBox = new VBox(startVBox, endVBox, this.summaryLabel, buttonsHBox);
        VBox.setMargin(startVBox, defaultInsets);
        VBox.setMargin(endVBox, defaultInsets);
        VBox.setMargin(summaryLabel, defaultInsets);
        vBox.setBackground(getBackgroundOfColor(StyleSetter.BACKGROUND_COLOR));
        JFXDepthManager.setDepth(this, 1);
        return vBox;
    }

    private static VBox generateCardLayout(JFXDatePicker timePicker, JFXDatePicker datepicker, Label label) {
        VBox vBox = new VBox(label, timePicker, datepicker);
        VBox.setMargin(label, new Insets(10));
        VBox.setMargin(timePicker, new Insets(10));
        VBox.setMargin(datepicker, new Insets(10));
        return vBox;
    }

    private void initializeElements() {
        this.startDatePicker = new JFXDatePicker();
        this.endDatePicker = new JFXDatePicker();
        this.startTimePicker = new JFXDatePicker();
        this.startTimePicker.setShowTime(true);
        this.endTimePicker = new JFXDatePicker();
        this.endTimePicker.setShowTime(true);
        this.okButton = new JFXButton("Add");
        this.closeButton = new JFXButton("Close");
        this.startTimeLabel = new Label("Pick start time");
        this.endTimeLabel = new Label("Pick End Time");
        this.summaryLabel = new Label();
        this.summaryLabel.setVisible(false);
        setStyles();
    }

    private void setStyles() {
        this.okButton.setBackground(getBackgroundOfColor(StyleSetter.PRIMARY_COLOR));
        this.closeButton.setBackground(getBackgroundOfColor(StyleSetter.SECONDARY_COLOR));
        this.startTimePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.endTimePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.startDatePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.endDatePicker.setDefaultColor(Paint.valueOf(StyleSetter.PRIMARY_COLOR));
        this.summaryLabel.setBackground(getBackgroundOfColor(StyleSetter.BACKGROUND_COLOR));
        this.summaryLabel.setStyle(" -fx-padding: 10");
    }

    private void addListeners() {
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    AddRecordPopup.this.close();
                }
            }
        });
        this.closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddRecordPopup.this.close();
            }
        });
    }

    private void initializeTimeElements() {
        DateTimeInitializer dateTimeInitializer = new DateTimeInitializer();
        dateTimeInitializer.getItemsList().add(startDatePicker);
        dateTimeInitializer.getItemsList().add(endDatePicker);
        dateTimeInitializer.getItemsList().add(startTimePicker);
        dateTimeInitializer.getItemsList().add(endTimePicker);

        dateTimeInitializer.initializeElements(getSummaryEventHandler());
    }

    private <T extends Event> EventHandler<T> getSummaryEventHandler() {
        return new EventHandler<T>() {
            @Override
            public void handle(T event) {
                AddRecordPopup.this.updateSummary();
            }
        };
    }

    private void updateSummary() {
        this.summaryLabel.setVisible(true);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.activity.getName()).append("\n");
        stringBuilder.append(this.startDatePicker.getValue()).append(" ").append(this.startTimePicker.getTime()).append("\n");
        stringBuilder.append("- \n");
        stringBuilder.append(this.endDatePicker.getValue()).append(" ").append(this.endTimePicker.getTime()).append("\n");
        this.summaryLabel.setText(stringBuilder.toString());
    }

    public RecordValidator.ValidationObject getObjectToValidate() {
        return new RecordValidator.ValidationObject(
                this.startTimePicker.getTime(),
                this.endTimePicker.getTime(),
                this.startDatePicker.getValue(),
                this.endDatePicker.getValue(),
                this.activity
        );
    }


}