package com.mprtcz.timeloggerweb.client.application.task.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.mprtcz.timeloggerweb.client.application.record.controller.RecordController;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskArray;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskOverlay;
import com.mprtcz.timeloggerweb.client.uielements.DateTimePicker;
import gwt.material.design.addins.client.window.MaterialWindow;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.*;

import java.util.Date;

import static com.mprtcz.timeloggerweb.client.application.record.validator.RecordDataValidator.validateRecordData;
import static com.mprtcz.timeloggerweb.client.uielements.StaticUiElementsCreator.*;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class TasksListUiCreator {
    private static final String ADD_NEW_RECORD_WINDOW_TITLE = "Add new Record";
    private static final String START_DATETIME_PICKER_LABEL = "Pick a start date and time";
    private static final String END_DATETIME_PICKER_LABEL = "Pick an end date and time";
    private static final String ADD_RECORD_CONFIRM_BUTTON_TEXT = "Add record";
    private static final String CANCEL_BUTTON_TEXT = "Cancel";
    private static final String NEW_RECORD_WINDOW_WIDTH = "330px";

    private MaterialRow tasksMaterialRow;
    private MaterialCollapsible taskCollapsible;
    private RecordController recordController;

    public TasksListUiCreator(MaterialRow tasksMaterialRow,
                              MaterialCollapsible taskCollapsible) {
        this.tasksMaterialRow = tasksMaterialRow;
        this.taskCollapsible = taskCollapsible;
        this.recordController = new RecordController();
    }

    public MaterialCollapsibleItem createCollapsibleListItem(TaskOverlay taskOverlay) {
        MaterialCollapsibleItem mci = new MaterialCollapsibleItem();
        MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
        MaterialLink nameLink = new MaterialLink();
        addLinkListener(nameLink, taskOverlay);
        nameLink.setIconType(IconType.ADD);
        nameLink.getElement().getStyle().setColor(taskOverlay.getColor());
        nameLink.setText(taskOverlay.getName());
        MaterialCollectionSecondary taskOptionsSecondary = new MaterialCollectionSecondary();
        taskOptionsSecondary.add(getTaskLinksPanel(getEditTaskClickHandler(taskOverlay), getDeleteTaskClickHandler(taskOverlay)));
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
                showAddRecordWindow(taskOverlay);
            }
        });
    }


    private MaterialWindow newRecordWindow;
    private MaterialTextArea summaryTextArea;
    private MaterialRow summaryRow;
    private void showAddRecordWindow(TaskOverlay taskOverlay) {
        if (newRecordWindow != null) {
            if (newRecordWindow.isOpen()) {
                newRecordWindow.clear();
                newRecordWindow.close();
            }
        }
        newRecordWindow = new MaterialWindow();
        newRecordWindow.setTitle(ADD_NEW_RECORD_WINDOW_TITLE);
        newRecordWindow.getIconMaximize().setVisible(false);
        newRecordWindow.setOverflow(Style.Overflow.VISIBLE);
        MaterialWindow.setOverlay(true);

        DateTimePicker startDateTimePicker = new DateTimePicker(START_DATETIME_PICKER_LABEL);
        DateTimePicker endDateTimePicker = new DateTimePicker(END_DATETIME_PICKER_LABEL);
        setPickersClickListeners(startDateTimePicker, endDateTimePicker, taskOverlay);
        ClickHandler closeClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                newRecordWindow.clear();
                newRecordWindow.close();
            }
        };
        ClickHandler acceptClickHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean result = validateRecordData(startDateTimePicker, endDateTimePicker, taskOverlay);
                if (result) {
                    recordController.createRecordInstance(
                            startDateTimePicker.getSelectedValues(),
                            endDateTimePicker.getSelectedValues(),
                            taskOverlay);
                }
            }
        };
        CellPanel addRecordPanel = getAddRecordPanel(startDateTimePicker, endDateTimePicker);
        MaterialButton confirmButton = getButtonStub(ADD_RECORD_CONFIRM_BUTTON_TEXT, acceptClickHandler);
        MaterialButton cancelButton = getButtonStub(CANCEL_BUTTON_TEXT, closeClickHandler);
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.setWidth("0px");
        buttonsPanel.setStyleName("auto-margin");
        MaterialPanel materialPanel = new MaterialPanel();
        materialPanel.add(addRecordPanel);
        summaryTextArea = new MaterialTextArea();
        summaryRow = getSummaryRow(summaryTextArea);
        materialPanel.add(summaryRow);
        materialPanel.add(buttonsPanel);
        newRecordWindow.add(materialPanel);
        newRecordWindow.setWidth(NEW_RECORD_WINDOW_WIDTH);
        newRecordWindow.open();
    }

    public void populateTasksTable(TaskArray<TaskOverlay> taskOverlays) {
        for (int i = 0; i < taskOverlays.length(); i++) {
            MaterialCollapsibleItem mcoli = createCollapsibleListItem(taskOverlays.get(i));
            taskCollapsible.add(mcoli);
        }
        tasksMaterialRow.setVisible(true);
    }

    private ClickHandler getEditTaskClickHandler(TaskOverlay taskOverlay) {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                GWT.log("Clicked edit button for task = " + taskOverlay.getName());
            }
        };
    }

    private ClickHandler getDeleteTaskClickHandler(TaskOverlay taskOverlay) {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                GWT.log("Clicked delete button for task = " + taskOverlay.getName());
            }
        };
    }

    private void setPickersClickListeners(DateTimePicker startDateTimePicker,
                                          DateTimePicker endDateTimePicker,
                                          TaskOverlay taskOverlay) {

        CloseHandler closeHandler = new CloseHandler() {
            @Override
            public void onClose(CloseEvent event) {
                if(startDateTimePicker.getSelectedValues() != null || endDateTimePicker.getSelectedValues() != null) {
                    populateSummaryString(startDateTimePicker, endDateTimePicker, taskOverlay);
                }
            }
        };

        startDateTimePicker.addDateTimeCloseHandler(closeHandler);
        endDateTimePicker.addDateTimeCloseHandler(closeHandler);
    }

    private void populateSummaryString(DateTimePicker startDateTimePicker,
                                       DateTimePicker endDateTimePicker,
                                       TaskOverlay taskOverlay) {
        String stringBuilder = taskOverlay.getName() +
                "\nFrom: " +
                formatDate(startDateTimePicker.getSelectedValues()) +
                "\nTo: " +
                formatDate(endDateTimePicker.getSelectedValues());
        summaryTextArea.setText(stringBuilder);
        summaryRow.setVisible(true);
    }

    private String formatDate(Date date) {
        if(date == null) {
            return "";
        } else {
            return DateTimeFormat.getFormat("dd.MM.yyyy HH:mm ").format(date);
        }
    }


}
