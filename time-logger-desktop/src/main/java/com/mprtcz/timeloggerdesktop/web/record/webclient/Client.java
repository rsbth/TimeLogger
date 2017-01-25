package com.mprtcz.timeloggerdesktop.web.record.webclient;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
public interface Client {

    @GET("/task/all")
    Call<List<Activity>> allActivities();

}
