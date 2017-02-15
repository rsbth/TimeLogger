package com.mprtcz.timeloggerweb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mprtcz.timeloggerweb.client.http.HttpClientAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class TimeLoggerWeb implements EntryPoint {
    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable tasksFlexTable = new FlexTable();

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        tasksFlexTable.setText(0, 0, "Name");
        tasksFlexTable.setText(0, 1, "Description");
        tasksFlexTable.setText(0, 2, "Color");

        mainPanel.add(tasksFlexTable);

        RootPanel.get("tasksList").add(mainPanel);

        HttpClientAsync httpClientAsync = new HttpClientAsync();
        httpClientAsync.getTasks(tasksFlexTable);
    }

}
