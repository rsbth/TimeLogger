package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.utilities.StringConverter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;

import java.util.ResourceBundle;

/**
 * Created by mprtcz on 2017-01-05.
 */
@Getter
public class DialogElementsConstructor {

    private JFXButton confirmButton;
    private JFXButton cancelButton;
    private JFXTextField newActivityNameTextField;
    private JFXTextField newActivityDescriptionTextField;
    private Label titleLabel;
    private ResourceBundle messages;

    public DialogElementsConstructor(ResourceBundle messages) {
        this.messages = messages;
        this.cancelButton = new JFXButton(messages.getString("cancel_button"));
        this.confirmButton = new JFXButton(messages.getString("add_activity_confirm_button"));
        this.confirmButton.prefWidthProperty().bind(this.cancelButton.widthProperty());
        this.titleLabel = new Label(messages.getString("enter_activity_label"));
        this.newActivityNameTextField = new JFXTextField();
        this.newActivityDescriptionTextField = new JFXTextField();
    }

    public Region getContent() {
        setPopupContentsStyles();
        return createLayout();
    }

    public Region createLayout() {
        VBox buttonsVBox = new VBox(cancelButton, confirmButton);
        VBox textFieldVBox = new VBox(newActivityNameTextField, newActivityDescriptionTextField);
        textFieldVBox.setMinWidth(200);
        VBox.setMargin(confirmButton, new Insets(5));
        VBox.setMargin(cancelButton, new Insets(5));
        VBox.setMargin(newActivityNameTextField, new Insets(5));
        VBox.setMargin(newActivityDescriptionTextField, new Insets(5));
        HBox hBox = new HBox(textFieldVBox, buttonsVBox);
        VBox overlayVBox = new VBox(titleLabel, hBox);
        overlayVBox.setStyle(" -fx-alignment: center");
        VBox.setMargin(titleLabel, new Insets(5));
        return overlayVBox;
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

    public static Region getTextLayout(String value, boolean isAlert) {
        Label label = new Label(StringConverter.insertLineSeparator(value, 45));
        label.setBackground(getBackgroundOfColor("white"));
        if (isAlert) {
            label.setTextFill(Paint.valueOf("red"));
        }
        label.setPadding(new Insets(10));
        HBox hBox = new HBox(label);
        HBox.setMargin(label, new Insets(10));
        return hBox;
    }
}
