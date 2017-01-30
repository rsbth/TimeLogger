package timelogger.mprtcz.com.timelogger.record.controller;

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


    public void getAllRecordsFromServer(Callback<List<RecordDto>> allRecordsCallback) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<List<RecordDto>> allRecordsCall = recordEndpoint.getAllRecords();
        allRecordsCall.enqueue(allRecordsCallback);
    }

    public void postRecordToServer(Callback<RecordDto> newRecordCallback, Record record) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<RecordDto> newRecordCall = recordEndpoint.postNewRecord(toDto(record));
        newRecordCall.enqueue(newRecordCallback);
    }

    public void getRecordsAfterSyncDate(Callback<List<RecordDto>> latestSyncedRecordsCallback, Date date) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<List<RecordDto>> recordsCall = recordEndpoint.getRecordsAfterDate(date.getTime());
        recordsCall.enqueue(latestSyncedRecordsCallback);
    }

    public void removeRecordFromServer(Callback<Void> removeRecordCallback, Record record) {
        RecordEndpoint recordEndpoint = RetrofitUtil.getRecordEndpointRetrofit();
        Call<Void> removeRecordCall = recordEndpoint.deleteRecord(record.getUuId());
        removeRecordCall.enqueue(removeRecordCallback);
    }
}
