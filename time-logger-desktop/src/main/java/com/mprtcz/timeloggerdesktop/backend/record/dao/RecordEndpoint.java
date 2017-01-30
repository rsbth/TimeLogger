package com.mprtcz.timeloggerdesktop.backend.record.dao;

import com.mprtcz.timeloggerdesktop.backend.record.model.RecordDto;
import retrofit2.Call;
import retrofit2.http.*;

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

    @DELETE("/record/{uuid}/delete")
    Call<Void> deleteRecord(@Path("uuid") String uuId);
}
