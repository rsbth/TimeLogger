package com.mprtcz.timeloggerdesktop.backend.record.service;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.web.record.controller.RecordWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class RecordService {
    private Logger logger = LoggerFactory.getLogger(RecordService.class);

    private RecordValidator recordValidator;
    private ActivityService activityService;
    RecordWebController recordWebController;

    public RecordService(RecordValidator recordValidator, ActivityService activityService) {
        this.recordValidator = recordValidator;
        this.activityService = activityService;
    }

    public ValidationResult addNewRecord(RecordValidator.ValidationObject object) throws Exception {
        logger.info("object to validate = {}", object);
        ValidationResult validationResult = this.recordValidator.validateNewRecordData(object);
        logger.info("validationResult = {}", validationResult.toString());
        if (validationResult.isErrorFree()) {
            Record record = RecordValidator.ValidationObject.toRecord(object);
            record.setCreationDate(new Date());
            Activity rootActivity = this.activityService.findActivityById(record.getActivity().getId());
            rootActivity.addRecord(record);
            logger.info("rootActivity = {}", rootActivity.toString());
            this.activityService.updateActivity(rootActivity, ActivityService.UpdateType.RECORD_UPDATE);
        }
        return validationResult;
    }

    public void synchronizeRecords(RecordWebController recordWebController) throws Exception {
        this.recordWebController = recordWebController;
        List<Record> localRecords = getAllRecords();
    }

    public List<Record> getAllRecords() throws Exception {
        List<Activity> activities = this.activityService.getActivities();
        List<Record> allRecords = new ArrayList<>();
        activities.stream().filter(a -> a.getActivityRecords() != null)
                .forEach(a -> allRecords.addAll(a.getActivityRecords()));
        return allRecords;
    }

    public Date getLastSynchDate(List<Record> allRecords) {
        Date lastDate = new Date(Long.MIN_VALUE);
        for (Record record : allRecords) {
            if(record.getSynchronizationDate() != null && record.getSynchronizationDate().after(lastDate)) {
                lastDate = record.getSynchronizationDate();
            }
        }
        return lastDate;
    }

    public void getRecordsFromServer() {

    }

}
