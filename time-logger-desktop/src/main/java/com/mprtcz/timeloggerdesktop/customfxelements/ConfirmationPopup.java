package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.mprtcz.timeloggerdesktop.model.LabelsModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mprtcz on 2017-01-05.
 */
@Getter
@Setter
public class ConfirmationPopup extends JFXPopup {
    private JFXButton confirmButton;
    private JFXButton cancelButton;
    private Label label;

    public ConfirmationPopup(String labelString, Region source) {
        this.initElements(labelString);
        DialogElementsConstructor.setStyleOfConfirmCancelButtons(this.confirmButton, this.cancelButton);
        this.label.setPadding(new Insets(10));
        this.addCloseHandler();
        this.setUpPopupProperties(this, createLayout(), source);
    }

    private HBox createButtonHBox() {
        HBox hBox = new HBox(confirmButton, cancelButton);
        hBox.setStyle(" -fx-alignment: center");
        HBox.setMargin(confirmButton, new Insets(5));
        HBox.setMargin(cancelButton, new Insets(5));
        return hBox;
    }

    private VBox createLayout() {
        VBox vBox = new VBox(this.label, createButtonHBox());
        vBox.setStyle(" -fx-alignment: center");
        vBox.setBackground(DialogElementsConstructor.getBackgroundOfColor(StyleSetter.BACKGROUND_COLOR));
        return vBox;
    }

    private void setUpPopupProperties(JFXPopup popup, Pane pane, Region source) {
        popup.setContent(pane);
        popup.setSource(source);
        popup.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            popup.close();
        });
    }

    private void addCloseHandler() {
        this.cancelButton.setOnAction(e -> this.close());
    }

    private void initElements(String labelString) {
        this.confirmButton = new JFXButton(LabelsModel.CONFIRMATION_POPUP_YES);
        this.label = new Label(labelString);
        this.cancelButton = new JFXButton(LabelsModel.CONFIRMATION_POPUP_NO);
    }


}