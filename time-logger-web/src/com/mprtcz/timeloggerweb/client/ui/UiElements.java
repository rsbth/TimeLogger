package com.mprtcz.timeloggerweb.client.ui;

import com.google.gwt.user.client.ui.FlexTable;
import com.mprtcz.timeloggerweb.client.model.TaskArray;
import com.mprtcz.timeloggerweb.client.model.TaskOverlay;

import static com.mprtcz.timeloggerweb.client.ui.UiStyles.*;

/**
 * Created by mprtcz on 2017-02-15.
 */
public class UiElements {

    public static void populateTasksTable(FlexTable table, TaskArray<TaskOverlay> taskOverlays) {
        stylizeTable(table);
        populateTableHeaders(table);
        for (int i = 0; i < taskOverlays.length(); i++) {
            table.setText(i + 1, 0, taskOverlays.get(i).getName());
            table.setWidget(i + 1, 1, getColoredLabel(taskOverlays.get(i).getColor()));
            table.setText(i + 1, 2, taskOverlays.get(i).getDescription());
            table.setWidget(i + 1, 3, getEditButton());
            table.setWidget(i + 1, 4, getRemoveButton());
        }
    }

    private static void populateTableHeaders(FlexTable tasksFlexTable) {
        tasksFlexTable.setText(0, 0, "Name");
        tasksFlexTable.setText(0, 1, "Color");
        tasksFlexTable.setText(0, 2, "Description");
        tasksFlexTable.setText(0, 3, "");
        tasksFlexTable.setText(0, 4, "");
    }
}
