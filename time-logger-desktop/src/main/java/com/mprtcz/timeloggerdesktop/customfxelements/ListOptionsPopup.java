package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.effects.JFXDepthManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import lombok.Getter;

import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundOfColor;
import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundStyle;

/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
public class ListOptionsPopup extends JFXPopup {

    public JFXButton addButton;
    public JFXButton removeButton;
    public JFXButton changeColorButton;
    public JFXButton addRecordButton;

    public ListOptionsPopup() {
        this.setContent(generateContent());
    }

    private Region generateContent() {
        Insets insets = new Insets(10);
        this.addButton = getStylizedButton(new ImageView("/icons/ic_add_white_18pt.png"));
        HBox addHBox = getStylizedHBox(addButton, "Add Activity");
        this.addRecordButton = getStylizedButton(new ImageView("/icons/ic_add_white_18pt.png"));
        HBox addRecordHBox = getStylizedHBox(addRecordButton, "Add Record");
        this.removeButton = getStylizedButton(new ImageView("/icons/ic_clear_white_18pt.png"));
        HBox removeHBox =  getStylizedHBox(removeButton, "Remove Activity");
        this.changeColorButton = getStylizedButton(new ImageView("/icons/ic_color_lens_white_18pt.png"));
        HBox colorHBox = getStylizedHBox(changeColorButton, "Change Color");
        VBox vBox = new VBox(addRecordHBox, removeHBox, colorHBox, addHBox);
        VBox.setMargin(addHBox, insets);
        VBox.setMargin(removeHBox, insets);
        VBox.setMargin(colorHBox, insets);
        VBox.setMargin(addRecordHBox, insets);
        this.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            this.close();
        });
        return vBox;
    }

    private JFXButton getStylizedButton(ImageView icon) {
        JFXButton button = new JFXButton("", icon);
        JFXDepthManager.setDepth(button, 1);
        button.setShape(new Circle(40));
        button.setMinSize(40, 40);
        button.setStyle(getBackgroundStyle(StyleSetter.PRIMARY_COLOR));
        return button;
    }

    private HBox getStylizedHBox(Button button, String labelString) {
        Label label = new Label(labelString);
        label.setBackground(getBackgroundOfColor("white"));
        label.setPadding(new Insets(1));
        HBox hBox = new HBox(button, label);
        hBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(label, new Insets(10));
        return hBox;
    }
}
