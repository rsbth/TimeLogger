package com.mprtcz.timeloggerdesktop.backend.activity.controller;

import com.mprtcz.timeloggerdesktop.backend.activity.dao.ActivityEndpoint;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.ActivityDto;
import com.mprtcz.timeloggerdesktop.backend.utilities.webutils.RetrofitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static com.mprtcz.timeloggerdesktop.backend.activity.model.converter.ActivityConverter.toDto;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class ActivityWebController {
    private Logger logger = LoggerFactory.getLogger(ActivityWebController.class);


    public void getActivitiesFromServerAsync(Callback<List<ActivityDto>> callback) {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<List<ActivityDto>> activitiesCall = activityEndpoint.getAllActivities();
        activitiesCall.enqueue(callback);
    }

    public List<ActivityDto> getActivitiesFromServer() throws IOException {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<List<ActivityDto>> activitiesCall = activityEndpoint.getAllActivities();
        return activitiesCall.execute().body();
    }

    public void postNewActivityToServerAsync(Callback<ActivityDto> callback, Activity activity) {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<ActivityDto> postNewActivityCall = activityEndpoint.postNewActivity(toDto(activity));
        postNewActivityCall.enqueue(callback);
    }

    public Response<ActivityDto> postNewActivityToServer(Activity activity) throws IOException {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<ActivityDto> postNewActivityCall = activityEndpoint.postNewActivity(toDto(activity));
        return postNewActivityCall.execute();
    }

    public void patchActivityOnServerAsync(Callback<ActivityDto> activityDtoCallback, Activity activity) {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<ActivityDto> postNewActivityCall = activityEndpoint.patchActivityOnServer(toDto(activity));
        postNewActivityCall.enqueue(activityDtoCallback);
    }

    public ActivityDto patchActivityOnServer(Activity activity) throws IOException {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<ActivityDto> postNewActivityCall = activityEndpoint.patchActivityOnServer(toDto(activity));
        return postNewActivityCall.execute().body();
    }

    public void deleteActivityOnServerAsync(Callback<Void> activityDtoCallback, Activity activity) {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<Void> postNewActivityCall = activityEndpoint.deleteActivityOnServer(activity.getServerId());
        postNewActivityCall.enqueue(activityDtoCallback);
    }

    public void deleteActivityOnServer(Activity activity) throws IOException {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<Void> postNewActivityCall = activityEndpoint.deleteActivityOnServer(activity.getServerId());
        postNewActivityCall.execute();
    }

    public ActivityDto getActivityFromServerByName(String name) throws IOException {
        ActivityEndpoint taskEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        String encodedString = URLEncoder.encode(name);
        Call<ActivityDto> postNewTaskCall = taskEndpoint.getActivityByName(encodedString);
        ActivityDto returnedActivity = postNewTaskCall.execute().body();
        logger.info("getActivityFromServerByName(String {}) = {} ", name, returnedActivity);
        return returnedActivity;
    }
}
