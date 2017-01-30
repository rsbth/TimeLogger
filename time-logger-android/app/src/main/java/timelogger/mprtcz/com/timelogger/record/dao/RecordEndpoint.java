package timelogger.mprtcz.com.timelogger.record.dao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import timelogger.mprtcz.com.timelogger.record.model.RecordDto;

/**
 * Created by Azet on 2017-01-30.
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
