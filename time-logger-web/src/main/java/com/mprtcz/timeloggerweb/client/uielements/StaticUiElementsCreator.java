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
import gwt.material.design.client.ui.MaterialTextBox;

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

    static CellPanel getNewTaskPanel() {
        CellPanel rootPanel = new HorizontalPanel();
        VerticalPanel textFieldsPanel = new VerticalPanel();
        MaterialTextBox nameTextBox = new MaterialTextBox();
        nameTextBox.setBackgroundColor(Color.WHITE);
        MaterialTextBox descriptionTextBox = new MaterialTextBox();
        descriptionTextBox.setBackgroundColor(Color.WHITE);
        textFieldsPanel.add(nameTextBox);
        textFieldsPanel.add(descriptionTextBox);
        rootPanel.add(textFieldsPanel);
        return rootPanel;
    }

    public static CellPanel getAddRecordPanel(DateTimePicker startDateTimePicker, DateTimePicker endDateTimePicker) {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(startDateTimePicker.getCompletePanel());
        verticalPanel.add(endDateTimePicker.getCompletePanel());
        return verticalPanel;
    }

    public static CellPanel getRecordButtonsPanel(ClickHandler acceptClickHandler, ClickHandler closeClickHandler) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setWidth("0");
        MaterialButton addButton = new MaterialButton("Add Record");
        addButton.addClickHandler(acceptClickHandler);
        MaterialButton closeButton = new MaterialButton("Close");
        closeButton.addClickHandler(closeClickHandler);
        horizontalPanel.add(addButton);
        horizontalPanel.add(closeButton);
        return horizontalPanel;
    }

    public static MaterialButton getButtonStub(String text, ClickHandler handler) {
        MaterialButton materialButton = new MaterialButton(text);
        materialButton.addClickHandler(handler);
        materialButton.setMargin(15);
        return materialButton;
    }
}
