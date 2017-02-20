package com.mprtcz.timeloggerweb.client.uielements;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTextBox;

/**
 * Created by mprtcz on 2017-02-17.
 */
public class StaticUiElementsCreator {

    static CellPanel getTaskLinksPanel() {
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
}
