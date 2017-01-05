package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.model.LabelsModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by mprtcz on 2017-01-05.
 */
public class ConfirmationPopup extends JFXPopup {
    private String secondaryColor = "#FF5252";
    private String backgroundColor = "#F4F4F4";

    private JFXButton confirmButton;
    private JFXButton cancelButton;
    private Label label;

    public ConfirmationPopup(String labelString, Region source) {
        this.initElements(labelString);
        this.setStyleOfConfirmCancelButtons();
        this.label.setPadding(new Insets(10));
        this.addCloseHandler();
        HBox hBox = new HBox(confirmButton, cancelButton);
        hBox.setStyle(" -fx-alignment: center");
        HBox.setMargin(confirmButton, new Insets(5));
        HBox.setMargin(cancelButton, new Insets(5));
        HBox.setMargin(label, new Insets(5));
        VBox vBox = new VBox(label, hBox);
        vBox.setStyle(" -fx-alignment: center");
        vBox.setBackground(this.getBackgroundOfColor(backgroundColor));
        this.setUpPopupProperties(this, vBox, source);
    }

    private void setStyleOfConfirmCancelButtons() {
        this.confirmButton.setPadding(new Insets(10));
        this.cancelButton.setPadding(new Insets(10));
        this.confirmButton.setRipplerFill(Paint.valueOf("darkgreen"));
        this.cancelButton.setRipplerFill(Paint.valueOf("red"));
        this.cancelButton.setStyle(getBackgroundStyle(secondaryColor));
    }

    private Background getBackgroundOfColor(String color) {
        return new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY));
    }

    private void setUpPopupProperties(JFXPopup popup, Pane pane, Region source) {
        popup.setContent(pane);
        popup.setSource(source);
        popup.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            popup.close();
        });
    }

    private String getBackgroundStyle(String color) {
        return "-fx-background-color: " + color +";";
    }

    private void addCloseHandler() {
        this.cancelButton.setOnAction(e -> this.close());
    }

    private void initElements(String labelString) {
        this.confirmButton = new JFXButton(LabelsModel.CONFIRMATION_POPUP_YES);
        this.label = new Label(labelString);
        this.cancelButton = new JFXButton(LabelsModel.CONFIRMATION_POPUP_NO);
    }

    public JFXButton getConfirmButton() {
        return confirmButton;
    }
}
