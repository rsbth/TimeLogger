package com.mprtcz.timeloggerweb.client.http;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.FlexTable;
import com.mprtcz.timeloggerweb.client.model.TaskArray;
import com.mprtcz.timeloggerweb.client.model.TaskOverlay;

import static com.mprtcz.timeloggerweb.client.ui.UiElements.populateTasksTable;

/**
 * Created by mprtcz on 2017-02-15.
 */
public class HttpClientAsync {
    public static final String SERVER_URL = "http://localhost:8080/task/all";
    private RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(SERVER_URL));

    public void getTasks(FlexTable tasksFlexTable) {
        try {

            Request request = builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        GWT.log("Repsonse = " + response.getText());
                        TaskArray<TaskOverlay> TaskOverlays = TaskOverlay.buildTasksArray(response.getText());
                        populateTasksTable(tasksFlexTable, TaskOverlays);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    GWT.log("Error while requesting, " +exception.toString());
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }


}
