package com.mprtcz.timeloggerweb.client.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mprtcz.timeloggerweb.client.model.TaskArray;
import com.mprtcz.timeloggerweb.client.model.TaskOverlay;
import gwt.material.design.addins.client.cutout.MaterialCutOut;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

/**
 * Created by mprtcz on 2017-02-17.
 */
public class UiElementsCreator {


    public static MaterialCollapsibleItem createCollapsibleListItem(TaskOverlay taskOverlay,
                                                                    MaterialCutOut cutout) {
        MaterialCollapsibleItem mci = new MaterialCollapsibleItem();
        MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
        MaterialLink nameLink = new MaterialLink();
        addLinkListener(nameLink, cutout, taskOverlay);
        nameLink.setIconType(IconType.ADD);
        nameLink.getElement().getStyle().setColor(taskOverlay.getColor());
        nameLink.setText(taskOverlay.getName());
        MaterialCollectionSecondary taskOptionsSecondary = new MaterialCollectionSecondary();
        taskOptionsSecondary.add(getTaskLinksPanel());
        header.add(nameLink);
        header.add(taskOptionsSecondary);
        MaterialCollapsibleBody body = new MaterialCollapsibleBody();
        MaterialLabel descriptionLabel = new MaterialLabel(taskOverlay.getDescription());
        body.add(descriptionLabel);
        mci.add(header);
        mci.add(body);
        return mci;
    }


    static void addLinkListener(MaterialLink materialLink, MaterialCutOut cutout, TaskOverlay taskOverlay) {
        materialLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Button clicked = " + event.getSource());
                showModalDialog(materialLink, taskOverlay, cutout);
            }
        });
    }

    public static void showModalDialog(MaterialLink materialLink, TaskOverlay taskOverlay, MaterialCutOut cutout) {
        cutout.clear();
        cutout.setTarget(materialLink);
        cutout.getElement().getStyle().setColor(taskOverlay.getColor());
        MaterialButton materialButton = new MaterialButton("Close");
        materialButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                cutout.close();
            }
        });
        cutout.add(getNewTaskPanel());
        cutout.add(materialButton);
        cutout.open();
    }

    private static CellPanel getTaskLinksPanel() {
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

    public static void populateTasksTable(MaterialCutOut cutout,
                                          TaskArray<TaskOverlay> taskOverlays,
                                          MaterialRow tasksMaterialRow,
                                          MaterialCollapsible taskCollapsible) {
        for (int i = 0; i < taskOverlays.length(); i++) {
            MaterialCollapsibleItem mcoli = createCollapsibleListItem(taskOverlays.get(i), cutout);
            taskCollapsible.add(mcoli);
        }
        MaterialAnimation animation = new MaterialAnimation();
        animation.setTransition(Transition.SLIDEINUP);
        animation.setDelayMillis(0);
        animation.setDurationMillis(1000);
        animation.setInfinite(false);
        tasksMaterialRow.setVisible(true);
        animation.animate(tasksMaterialRow);
    }

    private static CellPanel getNewTaskPanel() {
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
