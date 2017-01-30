package com.mprtcz.timeloggerdesktop.backend.record.service;

import com.mprtcz.timeloggerdesktop.backend.activity.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.controller.MainController;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class RecordService {
    private Logger logger = LoggerFactory.getLogger(RecordService.class);

    private RecordValidator recordValidator;
    @Getter
    private ActivityService activityService;
    private CustomDao customDao;
    @Getter
    private MainController mainController;
    @Setter
    private RecordSyncService recordSyncService;

    public RecordService(RecordValidator recordValidator,
                         ActivityService activityService,
                         CustomDao customDao,
                         MainController mainController) {
        this.recordValidator = recordValidator;
        this.activityService = activityService;
        this.customDao = customDao;
        this.mainController = mainController;
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

            if (this.recordSyncService != null) {
                this.recordSyncService.postNewRecordToServer(record);
            }
        }
        return validationResult;
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


    public Record findRecordByUuid(String uuId) throws Exception {
        List<Record> records = this.customDao.getAllRecords();
        for (Record record : records) {
            if (record.getUuId().equals(uuId)) {
                return record;
            }
        }
        return null;
    }

    public void updateRecord(Record record) throws Exception {
        this.customDao.update(record);
    }
}