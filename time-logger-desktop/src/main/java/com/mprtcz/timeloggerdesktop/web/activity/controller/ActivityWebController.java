package com.mprtcz.timeloggerdesktop.web.activity.controller;

import com.mprtcz.timeloggerdesktop.web.activity.client.ActivityEndpoint;
import com.mprtcz.timeloggerdesktop.web.activity.model.ActivityDto;
import com.mprtcz.timeloggerdesktop.web.webstatic.RetrofitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class ActivityWebController {
    private Logger logger = LoggerFactory.getLogger(ActivityWebController.class);


    public void getActivitiesFromServer(Callback<List<ActivityDto>> callback) {
        ActivityEndpoint activityEndpoint = RetrofitUtil.getActivityEndpointRetrofit();
        Call<List<ActivityDto>> activitiesCall = activityEndpoint.getAllActivities();
        activitiesCall.enqueue(callback);
    }
}
