package timelogger.mprtcz.com.timelogger.record.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import timelogger.mprtcz.com.timelogger.record.dao.RecordEndpoint;
import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.record.model.RecordDto;
import timelogger.mprtcz.com.timelogger.utils.web.RetrofitUtil;

import static timelogger.mprtcz.com.timelogger.record.model.converter.RecordConverter.toDto;


/**
 * Created by mprtcz on 2017-01-27.
 */
public class RecordWebController {


    public void getAllRecordsFromServerAsync(Callback<List<RecordDto>> allRecordsCallback) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<List<RecordDto>> allRecordsCall = recordEndpoint.getAllRecords();
        allRecordsCall.enqueue(allRecordsCallback);
    }

    public List<RecordDto> getAllRecordsFromServer() throws IOException {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<List<RecordDto>> allRecordsCall = recordEndpoint.getAllRecords();
        return allRecordsCall.execute().body();
    }

    public void postRecordToServerAsync(Callback<RecordDto> newRecordCallback, Record record) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<RecordDto> newRecordCall = recordEndpoint.postNewRecord(toDto(record));
        newRecordCall.enqueue(newRecordCallback);
    }

    public void getRecordsAfterSyncDateAsync(Callback<List<RecordDto>> latestSyncedRecordsCallback, Date date) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<List<RecordDto>> recordsCall = recordEndpoint.getRecordsAfterDate(date.getTime());
        recordsCall.enqueue(latestSyncedRecordsCallback);
    }

    public void removeRecordFromServerAsync(Callback<Void> removeRecordCallback, Record record) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<Void> removeRecordCall = recordEndpoint.deleteRecord(record.getUuId());
        removeRecordCall.enqueue(removeRecordCallback);
    }
}
