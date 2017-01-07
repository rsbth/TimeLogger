package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.effects.JFXDepthManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundOfColor;
import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundStyle;

/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
public class StyleSetter {
    public static final String BACKGROUND_COLOR = "#F4F4F4";
    public static final String PRIMARY_COLOR = "#2196f3";
    public static final String SECONDARY_COLOR = "#BDBDBD";
    public static final String ADD_ICON = "/icons/ic_add_white_18pt.png";
    public static final String REMOVE_ICON = "/icons/ic_clear_white_18pt.png";
    public static final String COLOR_ICON = "/icons/ic_color_lens_white_18pt.png";

    private List<Region> listViewControlsDependants = new ArrayList<>();
    private List<Button> buttonsList = new ArrayList<>();

    public void setVisibility(boolean value) {
        for (Region c : this.listViewControlsDependants) {
            c.setVisible(value);
        }
    }

    public void setButtonsColor(String color) {
        for (Button button :
                this.buttonsList) {
            button.setStyle(getBackgroundStyle(color));
        }
    }

    public static void setBottomPanelStyle(HBox hBox) {
        Insets insets = new Insets(1);
        hBox.setBackground(getBackgroundOfColor(PRIMARY_COLOR));
        for(Node n : hBox.getChildren()) {
            HBox.setMargin(n, insets);
            if(n instanceof JFXButton) {
                ((JFXButton) n) .setBackground(getBackgroundOfColor(PRIMARY_COLOR));
            }
        }
    }

    public static void setBottomButtonIcons(Map<String, JFXButton> buttons) {
        for (Map.Entry entry : buttons.entrySet()) {
            if(entry.getKey().equals("addRecord")) {
                setButtonImage(entry.getValue(), ADD_ICON);
            }
            if(entry.getKey().equals("removeActivity")) {
                setButtonImage(entry.getValue(), REMOVE_ICON);

            }
            if(entry.getKey().equals("changeColor")) {
                setButtonImage(entry.getValue(), COLOR_ICON);
            }
        }

    }

    private static void setButtonImage(Object button, String imagePath) {
        if(button instanceof JFXButton) {
            ((JFXButton)button).setGraphic(new ImageView(imagePath));
            ((JFXButton)button).setContentDisplay(ContentDisplay.TOP);
            ((JFXButton)button).setStyle(" -fx-text-fill: white; -fx-font: 12 Roboto");
        }
    }

    public static JFXButton stylizeButton(JFXButton button, ImageView icon) {
        button.setGraphic(icon);
        JFXDepthManager.setDepth(button, 1);
        button.setShape(new Circle(40));
        button.setMinSize(40, 40);
        button.setStyle(getBackgroundStyle(StyleSetter.PRIMARY_COLOR));
        return button;
    }

}