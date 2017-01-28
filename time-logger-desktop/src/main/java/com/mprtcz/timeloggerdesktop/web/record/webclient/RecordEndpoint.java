package com.mprtcz.timeloggerdesktop.web.record.webclient;

import com.mprtcz.timeloggerdesktop.web.record.model.RecordDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
public interface RecordEndpoint {

    @GET("/record/all")
    Call<List<RecordDto>> getAllRecords();

    @POST("/record/add")
    Call<RecordDto> postNewRecord(@Body RecordDto recordDto);

    @GET("/record/all/after/{date}")
    Call<List<RecordDto>> getRecordsAfterDate(@Path("date") Long date);
}
