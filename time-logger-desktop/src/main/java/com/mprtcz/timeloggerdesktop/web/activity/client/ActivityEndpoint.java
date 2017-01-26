package com.mprtcz.timeloggerdesktop.web.activity.client;

import com.mprtcz.timeloggerdesktop.web.activity.model.ActivityDto;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-26.
 */
public interface ActivityEndpoint {

    @GET("/task/all")
    Call<List<ActivityDto>> getAllActivities();
}
