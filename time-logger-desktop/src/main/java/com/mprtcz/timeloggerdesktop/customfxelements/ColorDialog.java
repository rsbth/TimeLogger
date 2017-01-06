package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXColorPicker;
import com.mprtcz.timeloggerdesktop.model.Activity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;


/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
public class ColorDialog extends DialogElementsConstructor {

    private JFXColorPicker colorPicker;
    private Activity activity;

    public ColorDialog(Activity activity) {
        super.getTitleLabel().setText("Select a Color");
        this.colorPicker = new JFXColorPicker();
        this.activity = activity;
        this.colorPicker.setValue(Color.web(activity.getColor()));
        setPopupContentsStyles();
    }

    @Override
    public Region createLayout() {
        Insets insets = new Insets(10);
        HBox buttonHBox = new HBox(getConfirmButton(), getCancelButton());
        HBox labelHBox = new HBox(getTitleLabel(), colorPicker);
        buttonHBox.setAlignment(Pos.CENTER_LEFT);
        labelHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(colorPicker, insets);
        HBox.setMargin(getTitleLabel(), insets);
        HBox.setMargin(getConfirmButton(), insets);
        HBox.setMargin(getCancelButton(), insets);

        return new VBox(labelHBox, buttonHBox);
    }

    public Activity getUpdatedColorActivity() {
        Color c = colorPicker.getValue();
        int red = (int) (c.getRed() * 255);
        int green = (int) (c.getGreen() * 255);
        int blue = (int) (c.getBlue() * 255);
        String newColor = String.format("#%02x%02x%02x", red, green, blue);
        this.activity.setColor(newColor);
        return this.activity;
    }
}
