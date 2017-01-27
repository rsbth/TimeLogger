package com.mprtcz.timeloggerdesktop.web.activity.client;

import com.mprtcz.timeloggerdesktop.web.activity.model.ActivityDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-26.
 */
public interface ActivityEndpoint {

    @GET("/task/all")
    Call<List<ActivityDto>> getAllActivities();

    @POST("/task/add")
    Call<ActivityDto> postNewActivity(@Body ActivityDto activityDto);

    @PATCH("/task/update")
    Call<ActivityDto> patchActivityOnServer(@Body ActivityDto activityDto);

    @DELETE("/task/{id}/delete")
    Call<Void> deleteActivityOnServer(@Path("id") Long id);
}
