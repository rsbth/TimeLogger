package com.mprtcz.timeloggerdesktop.backend.utilities.webutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mprtcz.timeloggerdesktop.backend.activity.dao.ActivityEndpoint;
import com.mprtcz.timeloggerdesktop.backend.record.dao.RecordEndpoint;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Date;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class RetrofitUtil {
    private static final String BASE_URL = "http://192.168.0.11:8080";
    private Retrofit retrofit;
    private static RetrofitUtil retrofitUtil;

    private RetrofitUtil() {
        createRetrofitInstance();
    }

    private void createRetrofitInstance() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        Gson gson = gsonBuilder.create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private Retrofit getRetrofit() {
        return this.retrofit;
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofitUtil == null) {
            retrofitUtil = new RetrofitUtil();
        }
        return retrofitUtil.getRetrofit();
    }

    public static ActivityEndpoint getActivityEndpointRetrofit() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(ActivityEndpoint.class);
    }

    public static RecordEndpoint getRecordEndpointRetrofit() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(RecordEndpoint.class);
    }
}
