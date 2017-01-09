package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.ToString;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundOfColor;

/**
 * Created by mprtcz on 2017-01-09.
 */
@Getter
public class SettingsPopup extends JFXPopup {

    private Settings settings;

    private ResourceBundle messages;

    public SettingsPopup(Region source, ResourceBundle messages) {
        this.settings = new Settings();
        this.messages = messages;
        this.setContent(generateContent());
        this.setSource(source);
    }

    private VBox generateContent() {
        Region languageContent = getLanguageContent();
        Region sliderContent = getSliderContent();
        Region buttonsContent = getButtonsContent();
        VBox layoutVBox = new VBox(languageContent, sliderContent, buttonsContent);
        layoutVBox.setBackground(getBackgroundOfColor(StyleSetter.BACKGROUND_COLOR));
        VBox.setMargin(languageContent, new Insets(20, 30, 5, 30));
        VBox.setMargin(sliderContent, new Insets(5, 30, 5, 30));
        VBox.setMargin(buttonsContent, new Insets(5, 30, 20, 30));
        return layoutVBox;
    }

    JFXComboBox<String> comboBox;
    private Region getLanguageContent() {
        this.comboBox = new JFXComboBox<>();
        setLanguagesInChoiceBox();
        Label label = new Label(this.messages.getString("choose_language_label"));
        VBox languageVBox = new VBox(label, comboBox);
        languageVBox.setBackground(getBackgroundOfColor("white"));
        VBox.setMargin(comboBox, new Insets(2, 10, 10, 10));
        VBox.setMargin(label, new Insets(10, 10, 2, 10));
        JFXDepthManager.setDepth(languageVBox, 1);
        return languageVBox;
    }

    JFXSlider slider;
    JFXCheckBox checkBox;
    private Region getSliderContent() {
        this.slider = new JFXSlider();
        Label label = new Label(this.messages.getString("days_visible_label"));
        this.checkBox = new JFXCheckBox();
        checkBox.setText(this.messages.getString("is_canvas_visible_checkbox"));
        slider.setMax(15);
        slider.setMin(5);
        checkBox.setOnAction(event -> slider.setDisable(!checkBox.isSelected()));
        VBox languageVBox = new VBox(label, checkBox, slider);
        languageVBox.setBackground(getBackgroundOfColor("white"));
        VBox.setMargin(slider, new Insets(5, 10, 10, 10));
        VBox.setMargin(checkBox, new Insets(5, 10, 5, 10));
        VBox.setMargin(label, new Insets(10, 10, 5, 10));
        languageVBox.setAlignment(Pos.CENTER);
        JFXDepthManager.setDepth(languageVBox, 1);
        return languageVBox;
    }

    private JFXButton confirmButton;
    private Region getButtonsContent() {
        this.confirmButton = new JFXButton(this.messages.getString("save_settings_button"));
        JFXButton cancelButton = new JFXButton(this.messages.getString("cancel_settings_button"));
        cancelButton.setOnAction(event -> {
            SettingsPopup.this.close();
            System.out.println(this.settings.toString());
        });
        DialogElementsConstructor.setStyleOfConfirmCancelButtons(confirmButton, cancelButton);
        HBox buttonsHBox = new HBox(confirmButton, cancelButton);
        HBox.setMargin(confirmButton, new Insets(10));
        HBox.setMargin(cancelButton, new Insets(10));
        JFXDepthManager.setDepth(buttonsHBox, 1);
        buttonsHBox.setBackground(getBackgroundOfColor("white"));
        return buttonsHBox;
    }

    private void setLanguagesInChoiceBox() {
        Map<String, Locale> languageVersions = LanguagePopup.availableLocales;
        for(Map.Entry entry : languageVersions.entrySet()) {
            this.comboBox.getItems().add((String) entry.getKey());
        }
    }

    public Settings getSettingsObject() {
        this.settings.language = this.comboBox.getValue();
        this.settings.howManyDaysVisible = (int) this.slider.getValue();
        this.settings.isCanvasVisible = this.checkBox.isSelected();
        return this.settings;
    }

    @Getter
    @ToString
    public class Settings {
        String language;
        boolean isCanvasVisible;
        int howManyDaysVisible;
    }
}
