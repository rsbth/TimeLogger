package com.mprtcz.timeloggerweb.client.ui;

import com.google.gwt.user.client.ui.FlexTable;
import com.mprtcz.timeloggerweb.client.model.TaskArray;
import com.mprtcz.timeloggerweb.client.model.TaskOverlay;

import static com.mprtcz.timeloggerweb.client.ui.UiStyles.getColoredLabel;
import static com.mprtcz.timeloggerweb.client.ui.UiStyles.getEditButton;
import static com.mprtcz.timeloggerweb.client.ui.UiStyles.getRemoveButton;

/**
 * Created by mprtcz on 2017-02-15.
 */
public class UiElements {

    public static void populateTasksTable(FlexTable table, TaskArray<TaskOverlay> taskOverlays) {
        for (int i = 0; i < taskOverlays.length(); i++) {
            table.setText(i + 1, 0, taskOverlays.get(i).getName());
            table.setText(i + 1, 1, taskOverlays.get(i).getDescription());
            table.setWidget(i + 1, 2, getColoredLabel(taskOverlays.get(i).getColor()));
            table.setWidget(i + 1, 3, getEditButton());
            table.setWidget(i + 1, 4, getRemoveButton());
        }
    }
}
