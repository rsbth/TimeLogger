package com.mprtcz.timeloggerweb.client.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mprtcz.timeloggerweb.client.model.TaskArray;
import com.mprtcz.timeloggerweb.client.model.TaskOverlay;
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

    public static MaterialCollectionItem createListCell(TaskOverlay taskOverlay) {
        HorizontalPanel labelsHorizontalPanel = new HorizontalPanel();
        VerticalPanel textPanel = new VerticalPanel();
        MaterialLabel nameLabel = new MaterialLabel(taskOverlay.getName());
        nameLabel.addStyleName("task-name-label");
        MaterialLabel descriptionLabel = new MaterialLabel(taskOverlay.getDescription());
        MaterialLabel colorLabel = new MaterialLabel("____");
        colorLabel.setStyleName("task-color-label");
        colorLabel.getElement().getStyle().setBackgroundColor(taskOverlay.getColor());
        colorLabel.getElement().getStyle().setColor(taskOverlay.getColor());
        textPanel.add(nameLabel);
        textPanel.add(descriptionLabel);
        labelsHorizontalPanel.add(colorLabel);
        labelsHorizontalPanel.add(textPanel);
        labelsHorizontalPanel.setWidth("0px");
        MaterialCollectionItem mci = new MaterialCollectionItem();
        mci.setWaves(WavesType.DEFAULT);
        MaterialCollectionSecondary hp = new MaterialCollectionSecondary();
        hp.add(getTaskIconsPanel());
        mci.add(labelsHorizontalPanel);
        mci.add(hp);
        return mci;
    }

    static void addButtonListener(MaterialButton materialButton) {
        materialButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Button clicked = " + event.getSource());
            }
        });
    }

    static void addIconListener(MaterialIcon materialIcon) {
        materialIcon.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Button clicked = " + event.getSource());
            }
        });
    }

    static CellPanel getTaskButtonsPanel() {
        CellPanel vp = new HorizontalPanel();
        MaterialButton editButton = new MaterialButton();
        editButton.setIconType(IconType.EDIT);
        editButton.setWaves(WavesType.LIGHT);
        addButtonListener(editButton);
        MaterialButton deleteButton = new MaterialButton();
        deleteButton.setIconType(IconType.DELETE);
        deleteButton.setWaves(WavesType.LIGHT);
        deleteButton.setBackgroundColor(Color.RED);
        vp.add(editButton);
        vp.add(deleteButton);
        return vp;
    }

    private static CellPanel getTaskIconsPanel() {
        CellPanel vp = new HorizontalPanel();
        MaterialIcon editButton = new MaterialIcon();
        editButton.setIconType(IconType.EDIT);
        editButton.setWaves(WavesType.LIGHT);
        editButton.setMarginRight(15);
        addIconListener(editButton);
        MaterialIcon deleteButton = new MaterialIcon();
        deleteButton.setIconType(IconType.DELETE);
        deleteButton.setWaves(WavesType.LIGHT);
        deleteButton.setIconColor(Color.RED);
        vp.add(editButton);
        vp.add(deleteButton);
        return vp;
    }

    public static void populateTasksTable(MaterialCollection tasksCollection,
                                          TaskArray<TaskOverlay> taskOverlays,
                                          MaterialRow tasksMaterialRow) {
        for (int i = 0; i < taskOverlays.length(); i++) {
            MaterialCollectionItem mci = createListCell(taskOverlays.get(i));
            setMaterialCollectionItemClickListener(tasksCollection, mci);
            tasksCollection.add(mci);
        }
        MaterialAnimation animation = new MaterialAnimation();
        animation.setTransition(Transition.SLIDEINUP);
        animation.setDelayMillis(0);
        animation.setDurationMillis(1000);
        animation.setInfinite(false);
        tasksMaterialRow.setVisible(true);
        animation.animate(tasksMaterialRow);
    }

    static void setMaterialCollectionItemClickListener(MaterialCollection tasksCollection, MaterialCollectionItem mci) {
        mci.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MaterialCollectionItem source = (MaterialCollectionItem) event.getSource();
                tasksCollection.clearActive();
                source.setActive(true);
            }
        });
    }
}
