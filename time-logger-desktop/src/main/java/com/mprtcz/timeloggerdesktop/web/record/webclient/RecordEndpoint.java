package com.mprtcz.timeloggerdesktop.web.record.webclient;

import com.mprtcz.timeloggerdesktop.web.record.model.RecordDto;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
public interface RecordEndpoint {

    @GET("/record/all")
    Call<List<RecordDto>> getAllRecords();


}
