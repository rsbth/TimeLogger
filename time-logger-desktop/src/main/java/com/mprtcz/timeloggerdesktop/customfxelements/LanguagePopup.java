package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.*;

import static com.mprtcz.timeloggerdesktop.customfxelements.ConfirmationPopup.setUpPopupProperties;
import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundOfColor;

/**
 * Created by mprtcz on 2017-01-09.
 */
public class LanguagePopup extends JFXPopup {

    List<JFXButton> buttons = new ArrayList<>();
    Locale chosenLocale;

    public static final Map<String, Locale> availableLocales = new HashMap<>();
    static {
        availableLocales.put("POLISH", new Locale("pl", "PL"));
        availableLocales.put("ENGLISH", new Locale("en", "US"));
    }

    public LanguagePopup(Region source, EventHandler handler) {
        Pane layout = initLayout(handler);
        setUpPopupProperties(this, layout, source);
    }

    private Pane initLayout(EventHandler handler) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(100);
        for(Map.Entry entry : availableLocales.entrySet()) {
            JFXButton button = getStylizedButton(entry.getKey(), handler);
            button.prefWidthProperty().bind(vBox.widthProperty());
            vBox.getChildren().add(button);
        }
        return vBox;
    }

    private static JFXButton getStylizedButton(Object text, EventHandler handler) {
        JFXButton button = new JFXButton((String) text);
        button.setPadding(new Insets(10));
        button.setBackground(getBackgroundOfColor("white"));
        button.setAlignment(Pos.CENTER);
        button.setOnAction(handler);
        return button;
    }
}
