package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mprtcz.timeloggerdesktop.model.LabelsModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import lombok.Getter;

/**
 * Created by mprtcz on 2017-01-05.
 */
@Getter
public class DialogElementsConstructor {
    private static final String SECONDARY_COLOR = "#FF5252";

    private JFXButton confirmAddButton;
    private JFXButton cancelAddButton;
    private JFXTextField newActivityNameTextField;
    private JFXTextField newActivityDescriptionTextField;
    private Label titleLabel;

    public DialogElementsConstructor() {
        this.confirmAddButton = new JFXButton(LabelsModel.ADD_ACTIVITY_CONFIRM_BUTTON);
        this.cancelAddButton = new JFXButton(LabelsModel.ADD_ACTIVITY_CANCEL_BUTTON);
        this.titleLabel = new Label(LabelsModel.ENTER_ACTIVITY_LABEL);
        this.newActivityNameTextField = new JFXTextField();
        this.newActivityDescriptionTextField = new JFXTextField();
    }

    public VBox generateContent() {
        setPopupContentsStyles();
        return createLayout();
    }

    private VBox createLayout() {
        VBox buttonsVBox = new VBox(confirmAddButton, cancelAddButton);
        VBox textFieldVBox = new VBox(newActivityNameTextField, newActivityDescriptionTextField);
        textFieldVBox.setMinWidth(200);
        VBox.setMargin(confirmAddButton, new Insets(5));
        VBox.setMargin(cancelAddButton, new Insets(5));
        VBox.setMargin(newActivityNameTextField, new Insets(5));
        VBox.setMargin(newActivityDescriptionTextField, new Insets(5));
        HBox hBox = new HBox(textFieldVBox, buttonsVBox);
        VBox overlayVBox = new VBox(titleLabel, hBox);
        overlayVBox.setStyle(" -fx-alignment: center");
        VBox.setMargin(titleLabel, new Insets(5));
        return overlayVBox;
    }

    private void setPopupContentsStyles() {
        this.newActivityNameTextField.setPromptText(LabelsModel.ADD_ACTIVITY_TEXT_FIELD);
        this.newActivityDescriptionTextField.setPromptText(LabelsModel.ADD_ACTIVITY_DESCRIPTION_TEXT_FIELD);
        setStyleOfConfirmCancelButtons(confirmAddButton, cancelAddButton);
        this.newActivityNameTextField.setPadding(new Insets(10));
        this.newActivityDescriptionTextField.setPadding(new Insets(10));
    }

    static void setStyleOfConfirmCancelButtons(JFXButton confirmButton, JFXButton cancelButton) {
        confirmButton.setPadding(new Insets(10));
        cancelButton.setPadding(new Insets(10));
        confirmButton.setRipplerFill(Paint.valueOf("darkgreen"));
        cancelButton.setRipplerFill(Paint.valueOf("red"));
        cancelButton.setStyle(getBackgroundStyle(SECONDARY_COLOR));
    }

    private static String getBackgroundStyle(String color) {
        return "-fx-background-color: " + color + ";";
    }
}
