package com.mprtcz.timeloggerweb.client.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mprtcz.timeloggerweb.client.model.TaskArray;
import com.mprtcz.timeloggerweb.client.model.TaskOverlay;
import gwt.material.design.addins.client.cutout.MaterialCutOut;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

import static com.mprtcz.timeloggerweb.client.uielements.StaticUiElementsCreator.getNewTaskPanel;
import static com.mprtcz.timeloggerweb.client.uielements.StaticUiElementsCreator.getTaskLinksPanel;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class TasksListUiCreator {

    private MaterialCutOut cutout;
    private MaterialRow tasksMaterialRow;
    private MaterialCollapsible taskCollapsible;

    public TasksListUiCreator(MaterialCutOut cutout,
                             MaterialRow tasksMaterialRow,
                             MaterialCollapsible taskCollapsible) {
        this.cutout = cutout;
        this.tasksMaterialRow = tasksMaterialRow;
        this.taskCollapsible = taskCollapsible;
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

    public  void populateTasksTable(TaskArray<TaskOverlay> taskOverlays) {
        for (int i = 0; i < taskOverlays.length(); i++) {
            MaterialCollapsibleItem mcoli = createCollapsibleListItem(taskOverlays.get(i));
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
}
