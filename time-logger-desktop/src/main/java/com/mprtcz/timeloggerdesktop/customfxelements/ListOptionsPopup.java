package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;

import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundOfColor;
import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundStyle;

/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
public class ListOptionsPopup extends JFXPopup {
    private static final String BACKGROUND_COLOR = "#F4F4F4";
    private static final String PRIMARY_COLOR = "#2196f3";
    private static final String SECONDARY_COLOR = "#FF5252";

    public JFXButton addButton;
    public JFXButton removeButton;
    public JFXButton changeColorButton;
    public JFXButton addRecordButton;

    public ListOptionsPopup() {
        this.setContent(generateContent());
    }

    private Region generateContent() {
        Insets insets = new Insets(10);
        this.addButton = getStylizedButton(new ImageView("/icons/ic_library_add_18pt.png"));
        HBox addHBox = getStylizedHBox(addButton, "Add Activity");
        this.addRecordButton = getStylizedButton(new ImageView("/icons/ic_queue_18pt.png"));
        HBox addRecordHBox = getStylizedHBox(addRecordButton, "Add Record");
        this.removeButton = getStylizedButton(new ImageView("/icons/ic_delete_18pt.png"));
        HBox removeHBox =  getStylizedHBox(removeButton, "Remove Activity");
        this.changeColorButton = getStylizedButton(new ImageView("/icons/ic_fingerprint_18pt.png"));
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
        button.setShape(new Circle(40));
        button.setMinSize(40, 40);
        button.setStyle(getBackgroundStyle(PRIMARY_COLOR));
        return button;
    }

    private HBox getStylizedHBox(Button button, String labelString) {
        Label label = new Label(labelString);
        label.setBackground(getBackgroundOfColor("black"));
        label.setTextFill(Color.web("lightgray"));
        label.setPadding(new Insets(1));
        HBox hBox = new HBox(button, label);
        hBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(label, new Insets(10));
        return hBox;
    }
}
