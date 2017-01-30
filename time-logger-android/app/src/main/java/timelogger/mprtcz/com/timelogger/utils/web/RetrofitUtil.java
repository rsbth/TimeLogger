package timelogger.mprtcz.com.timelogger.utils.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timelogger.mprtcz.com.timelogger.record.dao.RecordEndpoint;
import timelogger.mprtcz.com.timelogger.task.dao.TaskEndpoint;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class RetrofitUtil {
    private static final String BASE_URL = "http://localhost:8080";
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

    public static Retrofit getRetrofitInstance() {
        if (retrofitUtil == null) {
            retrofitUtil = new RetrofitUtil();
        }
        return retrofitUtil.getRetrofit();
    }

    public static TaskEndpoint getActivityEndpointRetrofit() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(TaskEndpoint.class);
    }

    public static RecordEndpoint getRecordEndpointRetrofit() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(RecordEndpoint.class);
    }
}
