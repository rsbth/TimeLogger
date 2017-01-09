package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
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
import java.util.ResourceBundle;

import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundOfColor;
import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundStyle;

/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
public class StyleSetter {
    public static final String BACKGROUND_COLOR = "#F4F4F4";
    public static final String PRIMARY_COLOR = "#3f51b5";
    public static final String ACCENT_COLOR = "#f50057";
    public static final String GRAY_COLOR = "#BDBDBD";
    public static final String ADD_ICON = "/icons/ic_add_white_18pt.png";
    public static final String REMOVE_ICON = "/icons/ic_clear_white_18pt.png";
    public static final String COLOR_ICON = "/icons/ic_color_lens_white_18pt.png";
    public static final String LANG_ICON = "/icons/ic_language_white_18pt.png";
    public static final String SETTINGS_ICON = "/icons/ic_settings_white_18pt.png";

    private List<Region> listViewControlsDependants = new ArrayList<>();
    private List<Button> buttonsList = new ArrayList<>();

    public void setVisibility(boolean value) {
        for (Region c : this.listViewControlsDependants) {
            c.setVisible(value);
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

    public static void setBottomButtonContent(Map<String, JFXButton> buttons, ResourceBundle messages) {
        for (Map.Entry entry : buttons.entrySet()) {
            if(entry.getKey().equals("addRecord")) {
                setButtonContent(entry.getValue(), ADD_ICON, messages.getString("add_record_button"));
            }
            if(entry.getKey().equals("removeActivity")) {
                setButtonContent(entry.getValue(), REMOVE_ICON, messages.getString("remove_activity_button"));

            }
            if(entry.getKey().equals("changeColor")) {
                setButtonContent(entry.getValue(), COLOR_ICON, messages.getString("color_record_button"));
            }
            if(entry.getKey().equals("settings")) {
                setButtonContent(entry.getValue(), SETTINGS_ICON, messages.getString("settings_button"));
            }
        }

    }

    private static void setButtonContent(Object button, String imagePath, String text) {
        if(button instanceof JFXButton) {
            ((JFXButton)button).setText(text);
            ((JFXButton)button).setGraphic(new ImageView(imagePath));
            ((JFXButton)button).setContentDisplay(ContentDisplay.TOP);
            ((JFXButton)button).setStyle(" -fx-text-fill: white; -fx-font: 12 Roboto; -fx-button-type: FLAT;");
        }
    }

    public static JFXButton stylizeButton(JFXButton button, ImageView icon) {
        button.setGraphic(icon);
        button.setShape(new Circle(40));
        button.setMinSize(40, 40);
        button.setStyle(getBackgroundStyle(StyleSetter.ACCENT_COLOR));
        return button;
    }

}