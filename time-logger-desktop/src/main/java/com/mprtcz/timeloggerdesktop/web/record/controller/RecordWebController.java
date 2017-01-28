package com.mprtcz.timeloggerdesktop.web.record.controller;

import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.web.record.model.RecordDto;
import com.mprtcz.timeloggerdesktop.web.record.webclient.RecordEndpoint;
import com.mprtcz.timeloggerdesktop.web.webstatic.RetrofitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;

import java.util.Date;
import java.util.List;

import static com.mprtcz.timeloggerdesktop.web.record.model.converter.RecordConverter.toDto;


/**
 * Created by mprtcz on 2017-01-27.
 */
public class RecordWebController {
    private Logger logger = LoggerFactory.getLogger(RecordWebController.class);


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
}
