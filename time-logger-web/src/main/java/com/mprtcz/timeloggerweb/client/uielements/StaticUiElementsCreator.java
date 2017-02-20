package com.mprtcz.timeloggerweb.client.uielements;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

/**
 * Created by mprtcz on 2017-02-17.
 */
public class StaticUiElementsCreator {

    public static CellPanel getTaskLinksPanel() {
        CellPanel vp = new HorizontalPanel();
        MaterialLink editButton = new MaterialLink();
        editButton.setIconType(IconType.EDIT);
        editButton.setWaves(WavesType.GREEN);
        editButton.setMarginRight(15);
        MaterialLink deleteButton = new MaterialLink();
        deleteButton.setIconType(IconType.DELETE);
        deleteButton.setWaves(WavesType.RED);
        deleteButton.setIconColor(Color.RED);
        vp.add(editButton);
        vp.add(deleteButton);
        return vp;
    }

    public static CellPanel getAddRecordPanel(DateTimePicker startDateTimePicker, DateTimePicker endDateTimePicker) {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(startDateTimePicker.getCompletePanel());
        verticalPanel.add(endDateTimePicker.getCompletePanel());
        verticalPanel.setVisible(false);
        return verticalPanel;
    }

    public static MaterialButton getButtonStub(String text, ClickHandler handler) {
        MaterialButton materialButton = new MaterialButton(text);
        materialButton.addClickHandler(handler);
        materialButton.setMargin(15);
        return materialButton;
    }

    public static MaterialAnimation getAnimationInstance(Transition transition) {
        MaterialAnimation animation = new MaterialAnimation();
        animation.setTransition(transition);
        animation.setDelayMillis(0);
        animation.setDurationMillis(1000);
        animation.setInfinite(false);
        return animation;
    }
}
