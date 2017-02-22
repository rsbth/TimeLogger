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
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;

/**
 * Created by mprtcz on 2017-02-17.
 */
public class StaticUiElementsCreator {

    public static CellPanel getTaskLinksPanel(ClickHandler editClickHandler, ClickHandler deleteClickHandler) {
        CellPanel cellPanel = new HorizontalPanel();
        MaterialLink editButton = new MaterialLink();
        editButton.setIconType(IconType.EDIT);
        editButton.setWaves(WavesType.GREEN);
        editButton.setMarginRight(15);
        editButton.addClickHandler(editClickHandler);
        MaterialLink deleteButton = new MaterialLink();
        deleteButton.setIconType(IconType.DELETE);
        deleteButton.setWaves(WavesType.RED);
        deleteButton.setIconColor(Color.RED);
        deleteButton.addClickHandler(deleteClickHandler);
        cellPanel.add(editButton);
        cellPanel.add(deleteButton);
        return cellPanel;
    }

    public static CellPanel getAddRecordPanel(DateTimePicker startDateTimePicker, DateTimePicker endDateTimePicker) {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(startDateTimePicker.getCompletePanel());
        verticalPanel.add(endDateTimePicker.getCompletePanel());
        return verticalPanel;
    }

    public static MaterialButton getButtonStub(String text, ClickHandler handler) {
        MaterialButton materialButton = new MaterialButton(text);
        materialButton.addClickHandler(handler);
        materialButton.setMargin(15);
        return materialButton;
    }

    public static MaterialRow getSummaryRow(MaterialTextArea summaryTextArea) {
        MaterialRow materialRow = new MaterialRow();
        materialRow.setStyleName("box-shadow");
        materialRow.setMargin(10);
        materialRow.setMarginTop(20);
        materialRow.setMarginBottom(20);
        summaryTextArea.setReadOnly(true);
        materialRow.setPadding(0);
        summaryTextArea.setPadding(0);
        summaryTextArea.setMarginLeft(10);
        materialRow.add(summaryTextArea);
        materialRow.setVisible(false);
        return materialRow;
    }
}
