package com.mprtcz.timeloggerdesktop.backend.activity.dao;

import com.mprtcz.timeloggerdesktop.backend.activity.model.ActivityDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-26.
 */
public interface ActivityEndpoint {

    @POST("/task/add")
    Call<ActivityDto> postNewActivity(@Body ActivityDto activityDto);

    @GET("/task/all")
    Call<List<ActivityDto>> getAllActivities();

    @PATCH("/task/update")
    Call<ActivityDto> patchActivityOnServer(@Body ActivityDto activityDto);

    @DELETE("/task/{id}/delete")
    Call<Void> deleteActivityOnServer(@Path("id") Long serverId);

    @GET("/task/name/{name}")
    Call<ActivityDto> getActivityByName(@Path("name") String name);
}
