package com.mprtcz.timeloggerdesktop.backend.record.service;

import com.mprtcz.timeloggerdesktop.backend.activity.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.web.record.controller.RecordWebController;
import com.mprtcz.timeloggerdesktop.web.record.model.RecordDto;
import com.mprtcz.timeloggerdesktop.web.webstatic.WebHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

import static com.mprtcz.timeloggerdesktop.web.record.model.converter.RecordConverter.toEntity;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class RecordService {
    private Logger logger = LoggerFactory.getLogger(RecordService.class);

    private RecordValidator recordValidator;
    private ActivityService activityService;
    private RecordWebController recordWebController;
    private CustomDao customDao;

    public RecordService(RecordValidator recordValidator,
                         ActivityService activityService,
                         CustomDao customDao) {
        this.recordValidator = recordValidator;
        this.activityService = activityService;
        this.customDao = customDao;
    }

    public ValidationResult addNewRecord(RecordValidator.ValidationObject object) throws Exception {
        logger.info("object to validate = {}", object);
        ValidationResult validationResult = this.recordValidator.validateNewRecordData(object);
        logger.info("validationResult = {}", validationResult.toString());
        if (validationResult.isErrorFree()) {
            Record record = RecordValidator.ValidationObject.toRecord(object);
            record.setCreationDate(new Date());
            record.setUuId(UUID.randomUUID().toString());
            record.setActive(true);
            logger.info("record's creation date = " + record.getCreationDate());
            Activity rootActivity = this.activityService.findActivityById(record.getActivity().getId());
            rootActivity.addRecord(record);
            logger.info("rootActivity = {}", rootActivity.toString());
            this.activityService.updateActivityWithNewRecord(rootActivity);

            if (this.recordWebController != null) {
                this.recordWebController.postRecordToServer(getPostNewRecordCallback(record), record);
            }
        }
        return validationResult;
    }


    public void synchronizeRecords(RecordWebController recordWebController) throws Exception {
        this.recordWebController = recordWebController;
        List<Record> localRecords = getAllRecords();
        this.recordWebController.getAllRecordsFromServer(getRecordsSynchronizationCallback(localRecords));
    }

    public List<Record> getAllRecords() throws Exception {
        logger.info("RecordService.getAllRecords");
        List<Activity> activities = this.activityService.getActivities();
        List<Record> allRecords = new ArrayList<>();
        activities.stream().filter(a -> a.getActivityRecords() != null)
                .forEach(a -> allRecords.addAll(a.getActivityRecords()));
        logger.info("Returning records: " + allRecords);
        return allRecords;
    }

    public Date getLastSynchDate(List<Record> allRecords) {
        Date lastDate = new Date(Long.MIN_VALUE);
        for (Record record : allRecords) {
            if (record.getSynchronizationDate() != null && record.getSynchronizationDate().after(lastDate)) {
                lastDate = record.getSynchronizationDate();
            }
        }
        return lastDate;
    }

    private Callback<List<RecordDto>> getRecordsSynchronizationCallback(List<Record> localRecords) {
        logger.info("RecordService.getRecordsSynchronizationCallback");
        return new Callback<List<RecordDto>>() {
            @Override
            public void onResponse(Call<List<RecordDto>> call, Response<List<RecordDto>> response) {
                if (response.isSuccessful()) {
                    try {
                        updateLocalDBWithServerData(localRecords, response.body());
                    } catch (Exception e) {
                        logger.error("Exception while updating local record database : " + e.toString());
                        e.printStackTrace();
                    }
                } else {
                    WebHandler.handleBadCodeResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<RecordDto>> call, Throwable throwable) {
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
                    addNewRecordFromServer(serverRecord);
                }
            } else {
                logger.info("Local record exists, Record = {}", localRecord);
                if (!localRecord.isActive()) {
                    if (serverRecord.isActive()) {
                        removeRecordFromServer(localRecord);
                    }
                }
            }
        }
        try {
            List<Record> localRecordsAfterServerUpdate = getAllRecords();
            List<Record> unsyncLocalRecords = new ArrayList<>();
            for (Record record : localRecordsAfterServerUpdate) {
                if (record.getSynchronizationDate() == null) {
                    unsyncLocalRecords.add(record);
                }
            }
            if (unsyncLocalRecords.size() > 0) {
                sendLocalRecordsToServer(unsyncLocalRecords);
            }
        } catch (Exception e) {
            logger.error("Exception while getting records from database = " + e.toString());
            e.printStackTrace();
        }
    }

    private void sendLocalRecordsToServer(List<Record> unsyncLocalRecords) {
        logger.info("RecordService.sendLocalRecordsToServer");
        for (Record record :
                unsyncLocalRecords) {
            this.recordWebController.postRecordToServer(getPostNewRecordCallback(record), record);
        }
    }

    public Callback<RecordDto> getPostNewRecordCallback(Record record) {
        return new Callback<RecordDto>() {
            @Override
            public void onResponse(Call<RecordDto> call, Response<RecordDto> response) {
                if (response.isSuccessful()) {
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
                } else {
                    WebHandler.handleBadCodeResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<RecordDto> call, Throwable throwable) {
                logger.error("Exception while posting new record to server, record = {}, exception = {}",
                        record, throwable.toString());
            }
        };
    }

    private Map<Date, Record> getLocalRecordsMap(List<Record> localRecords) {
        logger.info("RecordService.getLocalRecordsMap");
        Map<Date, Record> map = new HashMap<>();
        for (Record record :
                localRecords) {
            if (record.getSynchronizationDate() != null) {
                map.put(record.getSynchronizationDate(), record);
            }
        }
        return map;
    }

    public void addNewRecordFromServer(RecordDto recordDto) throws Exception {
        logger.info("RecordService.addNewRecordFromServer");
        Activity activity = this.activityService.findActivityByUuId(recordDto.getTaskUuId());
        Record record = toEntity(recordDto, activity);
        record.setActive(recordDto.isActive());
        Record recordEntity = findRecordByUuid(record.getUuId());
        if(recordEntity != null) {
            recordEntity.setSynchronizationDate(recordDto.getSynchronizationDate());
            this.customDao.update(recordEntity);
        }
        activity.addRecord(record);
    }

    private Record findRecordByUuid(String uuId) throws Exception {
        List<Record> records = this.customDao.getAllRecords();
        for (Record record : records) {
            if (record.getUuId().equals(uuId)) {
                return record;
            }
        }
        return null;
    }

    private void removeRecordFromServer(Record record) {
        logger.info("RecordService.removeRecordFromServer");
        this.recordWebController.removeRecordFromServer(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    logger.info("Record {} deleted successfully", record);
                } else {
                    WebHandler.handleBadCodeResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                logger.error("Error while deleting record");
                throwable.printStackTrace();
            }
        }, record);
    }
}
