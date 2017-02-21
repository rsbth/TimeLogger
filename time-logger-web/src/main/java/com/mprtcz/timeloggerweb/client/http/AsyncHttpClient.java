package com.mprtcz.timeloggerweb.client.http;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskArray;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskOverlay;
import com.mprtcz.timeloggerweb.client.application.task.uielements.TasksListUiCreator;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class AsyncHttpClient {
    public static final String SERVER_URL = "http://localhost:8080";
    private RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(SERVER_URL));

    public void getTasksAsync(TasksListUiCreator tasksListUiCreator) {
        GWT.log("HttpClientAsync.getTasksAsync");
        String tasksUrl = SERVER_URL + "/task/all";
        try {
            builder = new RequestBuilder(RequestBuilder.GET, URL.encode(tasksUrl));
            builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        GWT.log("Repsonse = " + response.getText());
                        TaskArray<TaskOverlay> taskOverlays = TaskOverlay.buildTasksArray(response.getText());
                        tasksListUiCreator.populateTasksTable(taskOverlays);

                    } else {
                        GWT.log("Repsonse code = " + response.getStatusCode());
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
