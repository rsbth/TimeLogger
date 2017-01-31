package timelogger.mprtcz.com.timelogger.record.service;


import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import timelogger.mprtcz.com.timelogger.interfaces.Synchrotron;
import timelogger.mprtcz.com.timelogger.record.controller.RecordWebController;
import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.record.model.RecordDto;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.utils.web.CustomWebCallback;

import static timelogger.mprtcz.com.timelogger.record.model.converter.RecordConverter.toEntity;

/**
 * Created by mprtcz on 2017-01-30.
 */
public class RecordSyncService {
    private static final String TAG = "RecordSyncService";

    private RecordService recordService;
    private RecordWebController recordWebController;
    private Synchrotron synchrotron;

    public RecordSyncService(RecordService recordService,
                             RecordWebController recordWebController) {
        this.recordService = recordService;
        this.recordWebController = recordWebController;
    }

    public void synchronizeRecords(Synchrotron synchrotron) throws Exception {
        Log.i(TAG, "RecordService.synchronizeRecords");
        this.synchrotron = synchrotron;
        List<Record> localRecords = this.recordService.getAllRecords();
        this.recordWebController.getAllRecordsFromServer(getRecordsSynchronizationCallback(localRecords));
    }

    private Callback<List<RecordDto>> getRecordsSynchronizationCallback(final List<Record> localRecords) {
        Log.i(TAG, "RecordService.getRecordsSynchronizationCallback");
        return new CustomWebCallback<List<RecordDto>>(this.synchrotron) {
            @Override
            public void onSuccessfulCall(Response<List<RecordDto>> response) {
                try {
                    updateLocalDBWithServerData(localRecords, response.body());
                } catch (Exception e) {
                    Log.e(TAG, "Exception while updating local record database : " + e.toString());
                    e.printStackTrace();
                    synchrotron.completeSynchronization();
                }
            }
        };
    }

    private void updateLocalDBWithServerData(List<Record> localRecords, List<RecordDto> serverRecords)
            throws Exception {
        Log.i(TAG, "RecordService.updateLocalDBWithServerData");
        Map<Date, Record> localRecordsMap = getLocalRecordsMap(localRecords);
        for (RecordDto serverRecord : serverRecords) {
            Record localRecord = localRecordsMap.get(serverRecord.getSynchronizationDate());
            Log.i(TAG, "Server record = " + serverRecord);
            if (localRecord == null) {
                if (serverRecord.isActive()) {
                    Log.i(TAG, "Server record active, ServerRecord = " +serverRecord);
                    this.addNewRecordFromServer(serverRecord);
                }
            } else {
                Log.i(TAG, "Local record exists, Record = " + localRecord);
                if (!localRecord.isActive()) {
                    if (serverRecord.isActive()) {
                        removeRecordFromServer(localRecord);
                    }
                } else {
                    if (!serverRecord.isActive()) {
                        removeLocalRecord(localRecord);
                    }
                }
            }
        }
        this.updateServerWithUnsyncLocalData();
        this.synchrotron.completeSynchronization();
    }

    private void updateServerWithUnsyncLocalData() {
        try {
            List<Record> localRecordsAfterServerUpdate = this.recordService.getAllRecords();
            List<Record> unsyncLocalRecords = new ArrayList<>();
            for (Record record : localRecordsAfterServerUpdate) {
                if(record.getSynchronizationDate() == null) {
                    unsyncLocalRecords.add(record);
                }
            }
            if (unsyncLocalRecords.size() > 0) {
                sendLocalRecordsToServer(unsyncLocalRecords);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting records from database = " + e.toString());
            e.printStackTrace();
        }
    }

    private Map<Date, Record> getLocalRecordsMap(List<Record> localRecords) {
        Log.i(TAG, "RecordService.getLocalRecordsMap");
        Map<Date, Record> map = new HashMap<>();
        for (Record record : localRecords) {
            if(record.getSynchronizationDate() != null) {
                map.put(record.getSynchronizationDate(), record);
            }
        }
        return map;
    }

    private void removeRecordFromServer(final Record record) {
        Log.i(TAG, "RecordService.removeRecordFromServer");
        this.recordWebController.removeRecordFromServer(
                new CustomWebCallback<Void>(this.synchrotron) {
            @Override
            public void onSuccessfulCall(Response<Void> response) {
                Log.i(TAG, "Record {} deleted successfully" +record);
            }
        }, record);
    }

    private void removeLocalRecord(Record record) throws Exception {
        record.setActive(false);
        this.recordService.updateRecord(record);
    }

    private void sendLocalRecordsToServer(List<Record> unsyncLocalRecords) {
        Log.i(TAG, "RecordService.sendLocalRecordsToServer");
        for (Record record :
                unsyncLocalRecords) {
            this.recordWebController.postRecordToServer(
                    this.getPostNewRecordCallback(record), record);
        }
    }

    private Callback<RecordDto> getPostNewRecordCallback(final Record record) {
        return new CustomWebCallback<RecordDto>(this.synchrotron) {
            @Override
            public void onSuccessfulCall(Response<RecordDto> response) {
                Log.i(TAG, "Record post successful, Record = " +record);
                RecordDto recordReturnedByServer = response.body();
                try {
                    addNewRecordFromServer(recordReturnedByServer);
                } catch (Exception e) {
                    Log.e(TAG, 
                            "Exception while posting new record to local DB, record = "
                                    +record + " exception = {}" + e.toString());
                    e.printStackTrace();
                }
            }
        };
    }

    private void addNewRecordFromServer(RecordDto recordDto) throws Exception {
        Log.i(TAG, "RecordService.addNewRecordFromServer");
        Task task = this.recordService.getTaskService()
                .findTaskByServerID(recordDto.getTaskServerId());
        if(task == null) {
            Log.w(TAG, "addNewRecordFromServer: parent task null for serverID = "
                    +recordDto.getTaskServerId());
            return;
        }
        Record record = toEntity(recordDto, task);
        record.setActive(recordDto.isActive());
        Record recordEntity = this.recordService.findRecordByUuid(record.getUuId());
        if (recordEntity != null) {
            recordEntity.setSynchronizationDate(recordDto.getSynchronizationDate());
            this.recordService.updateRecord(recordEntity);
        }
        task.addRecord(record);
    }

    public void postNewRecordToServer(Record record) {
        this.recordWebController.postRecordToServer(getPostNewRecordCallback(record), record);
    }
}