package com.mprtcz.timeloggerdesktop.backend.record.service;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.record.controller.RecordWebController;
import com.mprtcz.timeloggerdesktop.backend.record.model.RecordDto;
import com.mprtcz.timeloggerdesktop.backend.utilities.webutils.CustomWebCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mprtcz.timeloggerdesktop.backend.record.model.converter.RecordConverter.toEntity;

/**
 * Created by mprtcz on 2017-01-30.
 */
public class RecordSyncService {
    private Logger logger = LoggerFactory.getLogger(RecordSyncService.class);

    private RecordService recordService;
    private RecordWebController recordWebController;

    public RecordSyncService(RecordService recordService,
                             RecordWebController recordWebController) {
        this.recordService = recordService;
        this.recordWebController = recordWebController;
    }

    public void synchronizeRecords() throws Exception {
        logger.info("RecordService.synchronizeRecords");
        List<Record> localRecords = this.recordService.getAllRecords();
        this.recordWebController.getAllRecordsFromServer(getRecordsSynchronizationCallback(localRecords));
    }

    private Callback<List<RecordDto>> getRecordsSynchronizationCallback(List<Record> localRecords) {
        logger.info("RecordService.getRecordsSynchronizationCallback");
        return new CustomWebCallback<List<RecordDto>>() {
            @Override
            public void onSuccessfulCall(Response<List<RecordDto>> response) {
                try {
                    updateLocalDBWithServerData(localRecords, response.body());
                } catch (Exception e) {
                    logger.error("Exception while updating local record database : " + e.toString());
                    e.printStackTrace();
                }
            }
        };
    }

    private void updateLocalDBWithServerData(List<Record> localRecords, List<RecordDto> serverRecords)
            throws Exception {
        logger.info("RecordService.updateLocalDBWithServerData");
        Map<Date, Record> localRecordsMap = getLocalRecordsMap(localRecords);
        for (RecordDto serverRecord : serverRecords) {
            Record localRecord = localRecordsMap.get(serverRecord.getSynchronizationDate());
            logger.info("Server record = {}", serverRecord);
            if (localRecord == null) {
                if (serverRecord.isActive()) {
                    logger.info("Server record active, ServerRecord = {}", serverRecord);
                    this.addNewRecordFromServer(serverRecord);
                }
            } else {
                logger.info("Local record exists, Record = {}", localRecord);
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
    }

    private void updateServerWithUnsyncLocalData() {
        try {
            List<Record> localRecordsAfterServerUpdate = this.recordService.getAllRecords();
            List<Record> unsyncLocalRecords = localRecordsAfterServerUpdate.stream()
                    .filter(record -> record.getSynchronizationDate() == null).collect(Collectors.toList());
            if (unsyncLocalRecords.size() > 0) {
                sendLocalRecordsToServer(unsyncLocalRecords);
            }
        } catch (Exception e) {
            logger.error("Exception while getting records from database = " + e.toString());
            e.printStackTrace();
        }
        this.recordService.getMainController().updateGUI();
    }

    private Map<Date, Record> getLocalRecordsMap(List<Record> localRecords) {
        logger.info("RecordService.getLocalRecordsMap");
        Map<Date, Record> map = new HashMap<>();
        localRecords.stream().filter(record ->
                record.getSynchronizationDate() != null).forEach(record ->
                map.put(record.getSynchronizationDate(), record));
        return map;
    }


    private void removeRecordFromServer(Record record) {
        logger.info("RecordService.removeRecordFromServer");
        this.recordWebController.removeRecordFromServer(new CustomWebCallback<Void>() {
            @Override
            public void onSuccessfulCall(Response<Void> response) {
                logger.info("Record {} deleted successfully", record);
            }
        }, record);
    }

    private void removeLocalRecord(Record record) throws Exception {
        record.setActive(false);
        this.recordService.updateRecord(record);
    }

    private void sendLocalRecordsToServer(List<Record> unsyncLocalRecords) {
        logger.info("RecordService.sendLocalRecordsToServer");
        for (Record record :
                unsyncLocalRecords) {
            this.recordWebController.postRecordToServer(
                    this.getPostNewRecordCallback(record), record);
        }
    }

    private Callback<RecordDto> getPostNewRecordCallback(Record record) {
        return new CustomWebCallback<RecordDto>() {
            @Override
            public void onSuccessfulCall(Response<RecordDto> response) {
                logger.info("Record post successful, Record = {}", record);
                RecordDto recordReturnedByServer = response.body();
                try {
                    addNewRecordFromServer(recordReturnedByServer);
                } catch (Exception e) {
                    logger.error(
                            "Exception while posting new record to local DB, record = {}, exception = {}",
                            record, e.toString());
                    e.printStackTrace();
                }
            }
        };
    }

    private void addNewRecordFromServer(RecordDto recordDto) throws Exception {
        logger.info("RecordService.addNewRecordFromServer");
        Activity activity = this.recordService.getActivityService()
                .findActivityByServerId(recordDto.getTaskServerId());
        Record record = toEntity(recordDto, activity);
        record.setActive(recordDto.isActive());
        Record recordEntity = this.recordService.findRecordByUuid(record.getUuId());
        if (recordEntity != null) {
            recordEntity.setSynchronizationDate(recordDto.getSynchronizationDate());
            this.recordService.updateRecord(recordEntity);
        }
        activity.addRecord(record);
    }

    public void postNewRecordToServer(Record record) {
        this.recordWebController.postRecordToServer(getPostNewRecordCallback(record), record);
    }
}
