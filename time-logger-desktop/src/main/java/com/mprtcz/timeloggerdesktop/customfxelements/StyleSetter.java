package com.mprtcz.timeloggerdesktop.customfxelements;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.mprtcz.timeloggerdesktop.customfxelements.DialogElementsConstructor.getBackgroundStyle;

/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
public class StyleSetter {
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

}
