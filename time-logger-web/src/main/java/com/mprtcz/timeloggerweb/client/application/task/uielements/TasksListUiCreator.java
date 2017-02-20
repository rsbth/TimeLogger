package com.mprtcz.timeloggerweb.client.application.task.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.mprtcz.timeloggerweb.client.application.record.controller.RecordController;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskArray;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskOverlay;
import com.mprtcz.timeloggerweb.client.uielements.DateTimePicker;
import gwt.material.design.addins.client.cutout.MaterialCutOut;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

import static com.mprtcz.timeloggerweb.client.application.record.validator.RecordDataValidator.validateRecordData;
import static com.mprtcz.timeloggerweb.client.uielements.StaticUiElementsCreator.*;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class TasksListUiCreator {

    private MaterialCutOut cutout;
    private MaterialRow tasksMaterialRow;
    private MaterialCollapsible taskCollapsible;
    private RecordController recordController;

    public TasksListUiCreator(MaterialCutOut cutout,
                             MaterialRow tasksMaterialRow,
                             MaterialCollapsible taskCollapsible) {
        this.cutout = cutout;
        this.tasksMaterialRow = tasksMaterialRow;
        this.taskCollapsible = taskCollapsible;
        this.recordController = new RecordController();
    }

    public  MaterialCollapsibleItem createCollapsibleListItem(TaskOverlay taskOverlay) {
        MaterialCollapsibleItem mci = new MaterialCollapsibleItem();
        MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
        MaterialLink nameLink = new MaterialLink();
        addLinkListener(nameLink, taskOverlay);
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


    private void addLinkListener(MaterialLink materialLink, TaskOverlay taskOverlay) {
        materialLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                GWT.log("Button clicked = " + event.getSource());
                showModalDialog(materialLink, taskOverlay);
            }
        });
    }

    public  void showModalDialog(MaterialLink materialLink, TaskOverlay taskOverlay) {
        cutout.clear();
        cutout.setTarget(materialLink);
        DateTimePicker startDateTimePicker = new DateTimePicker("Pick a start date and time");
        DateTimePicker endDateTimePicker = new DateTimePicker("Pick an end date and time");
        ClickHandler closeClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                cutout.close();
            }
        };
        ClickHandler acceptClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                validateRecordData(startDateTimePicker, endDateTimePicker, taskOverlay);
            }
        };
        CellPanel addRecordPanel = getAddRecordPanel(startDateTimePicker, endDateTimePicker);
        cutout.add(addRecordPanel);
        cutout.add(getButtonStub("Add record", acceptClickHandler));
        cutout.add(getButtonStub("Cancel", closeClickHandler));
        addRecordPanel.setVisible(true);
        getAnimationInstance(Transition.SLIDEINDOWN).animate(addRecordPanel);
        cutout.open();
    }

    public  void populateTasksTable(TaskArray<TaskOverlay> taskOverlays) {
        for (int i = 0; i < taskOverlays.length(); i++) {
            MaterialCollapsibleItem mcoli = createCollapsibleListItem(taskOverlays.get(i));
            taskCollapsible.add(mcoli);
        }
        tasksMaterialRow.setVisible(true);
        getAnimationInstance(Transition.SLIDEINLEFT).animate(tasksMaterialRow);
        MaterialAnimation gridAnimation = new MaterialAnimation();
        gridAnimation.setTransition(Transition.SHOW_STAGGERED_LIST);
        gridAnimation.animate(taskCollapsible);
    }
}
