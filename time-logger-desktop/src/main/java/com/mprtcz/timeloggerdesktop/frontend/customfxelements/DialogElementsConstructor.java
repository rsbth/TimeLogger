package com.mprtcz.timeloggerdesktop.frontend.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.utilities.StringConverter;
import com.mprtcz.timeloggerdesktop.frontend.utils.CustomColor;
import com.mprtcz.timeloggerdesktop.frontend.utils.MessageType;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * Created by mprtcz on 2017-01-05.
 */
@Getter
public class DialogElementsConstructor {
    private Logger logger = LoggerFactory.getLogger(DialogElementsConstructor.class);

    private JFXButton confirmButton;
    private JFXButton cancelButton;
    private JFXButton exportDataButton;
    private JFXTextField newActivityNameTextField;
    private JFXTextField newActivityDescriptionTextField;
    private Label titleLabel;
    private Label exportDataLabel;
    private ResourceBundle messages;
    private Label colorLabel;
    private JFXColorPicker colorPicker;
    private Activity updatedActivity;

    public DialogElementsConstructor(ResourceBundle messages) {
        this.messages = messages;
        this.cancelButton = new JFXButton(messages.getString("cancel_button"));
        this.confirmButton = new JFXButton(messages.getString("add_activity_confirm_button"));
        this.colorLabel = new Label(messages.getString("choose_color"));
        this.colorPicker = new JFXColorPicker();
        this.colorPicker.setValue(Color.web(CustomColor.getRandomColor().getColorCode()));
        this.confirmButton.prefWidthProperty().bind(this.cancelButton.widthProperty());
        this.titleLabel = new Label(messages.getString("enter_activity_label"));
        this.newActivityNameTextField = new JFXTextField();
        this.newActivityDescriptionTextField = new JFXTextField();
    }

    public DialogElementsConstructor(ResourceBundle messages, Activity activity) {
        this(messages);
        this.titleLabel.setText(messages.getString("edit_activity_label"));
        logger.info("activity to update = {}", activity.toString());
        this.updatedActivity = activity;
        this.newActivityNameTextField.setText(activity.getName());
        this.newActivityDescriptionTextField.setText(activity.getDescription());
        this.colorPicker.setValue(Color.web(activity.getColor()));
    }

    public Activity getNewActivity() {
        Activity activity = new Activity();
        activity.setName(newActivityNameTextField.getText());
        activity.setDescription(newActivityDescriptionTextField.getText());
        activity.setColor(convertColorToString(colorPicker.getValue()));
        logger.info("activity after update = {}", activity);
        return activity;
    }

    public Activity getUpdatedActivity() {
        this.updatedActivity.setName(newActivityNameTextField.getText());
        this.updatedActivity.setDescription(newActivityDescriptionTextField.getText());
        this.updatedActivity.setColor(convertColorToString(colorPicker.getValue()));
        return this.updatedActivity;
    }

    private static String convertColorToString(Color c) {
        int red = (int) (c.getRed() * 255);
        int green = (int) (c.getGreen() * 255);
        int blue = (int) (c.getBlue() * 255);
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    public Region getContent() {
        setPopupContentsStyles();
        return createLayout();
    }

    public Region createLayout() {
        Insets insets = new Insets(5);
        VBox buttonsVBox = new VBox(cancelButton, confirmButton);
        VBox colorVBox = new VBox(this.colorLabel, this.colorPicker);
        VBox textFieldVBox = new VBox(newActivityNameTextField, newActivityDescriptionTextField);
        textFieldVBox.setMinWidth(200);
        VBox.setMargin(confirmButton, insets);
        VBox.setMargin(cancelButton, insets);
        VBox.setMargin(colorLabel, insets);
        VBox.setMargin(colorPicker, insets);
        VBox.setMargin(newActivityNameTextField, insets);
        VBox.setMargin(newActivityDescriptionTextField, insets);
        HBox hBox = new HBox(textFieldVBox, colorVBox, buttonsVBox);
        VBox overlayVBox = new VBox(titleLabel, hBox);
        overlayVBox.setStyle(" -fx-alignment: center");
        VBox.setMargin(titleLabel, new Insets(10));
        this.setHeightBindings();
        return overlayVBox;
    }

    private void setHeightBindings() {
        this.colorLabel.prefHeightProperty().bind(this.cancelButton.heightProperty());
        this.colorPicker.prefHeightProperty().bind(this.confirmButton.heightProperty());
    }

    void setPopupContentsStyles() {
        this.newActivityNameTextField.setPromptText(messages.getString("add_activity_text_field"));
        this.newActivityDescriptionTextField.setPromptText(messages.getString("add_activity_description_text_field"));
        setStyleOfConfirmCancelButtons(confirmButton, cancelButton);
        this.newActivityNameTextField.setPadding(new Insets(10));
        this.newActivityDescriptionTextField.setPadding(new Insets(10));
    }

    static void setStyleOfConfirmCancelButtons(JFXButton confirmButton, JFXButton cancelButton) {
        confirmButton.setPadding(new Insets(10));
        cancelButton.setPadding(new Insets(10));
        confirmButton.setRipplerFill(Paint.valueOf("darkgreen"));
        cancelButton.setRipplerFill(Paint.valueOf("red"));
        cancelButton.setStyle(getBackgroundStyle(StyleSetter.GRAY_COLOR));
        confirmButton.setStyle(getBackgroundStyle(StyleSetter.ACCENT_COLOR));
    }

    public static String getBackgroundStyle(String color) {
        return "-fx-background-color: " + color + ";";
    }

    public static Background getBackgroundOfColor(String color) {
        return new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY));
    }

    public static Region createListViewCellLayout(Activity item) {
        HBox container = new HBox();
        container.setMouseTransparent(true);
        Label coloredLabel = new Label("      ");
        Label marginLabel = new Label("      ");
        Label label = new Label(item.getName());
        coloredLabel.setBackground(getBackgroundOfColor(item.getColor()));
        coloredLabel.setStyle("-fx-border-insets: 5px;");
        container.setStyle(" -fx-alignment: center-left");
        container.getChildren().add(coloredLabel);
        container.getChildren().add(marginLabel);
        container.getChildren().add(label);
        return container;
    }

    public static Region getTextLayout(String value, MessageType type) {
        Label label = new Label(StringConverter.insertLineSeparator(value, 45));
        label.setBackground(getBackgroundOfColor("white"));
        if (type == MessageType.ALERT) {
            label.setTextFill(Paint.valueOf("red"));
        }
        label.setPadding(new Insets(10));
        HBox hBox = new HBox(label);
        HBox.setMargin(label, new Insets(10));
        return hBox;
    }
}
