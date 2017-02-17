package com.mprtcz.timeloggerweb.client.http;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.mprtcz.timeloggerweb.client.model.TaskArray;
import com.mprtcz.timeloggerweb.client.model.TaskOverlay;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialRow;

import static com.mprtcz.timeloggerweb.client.uielements.UiElementsCreator.populateTasksTable;


/**
 * Created by mprtcz on 2017-02-17.
 */
public class HttpClientAsync {
    public static final String SERVER_URL = "http://192.168.0.3:8080";
    private RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(SERVER_URL));

    public void getTasks(MaterialCollection tasksCollection, MaterialRow tasksMaterialRow) {
        GWT.log("HttpClientAsync.getTasks");
        String tasksUrl = SERVER_URL + "/task/all";
        try {
            builder = new RequestBuilder(RequestBuilder.GET, URL.encode(tasksUrl));
            builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        GWT.log("Repsonse = " + response.getText());
                        TaskArray<TaskOverlay> taskOverlays = TaskOverlay.buildTasksArray(response.getText());
                        populateTasksTable(tasksCollection, taskOverlays, tasksMaterialRow);

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
