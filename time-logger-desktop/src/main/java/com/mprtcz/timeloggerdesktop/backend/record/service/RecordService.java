package com.mprtcz.timeloggerdesktop.backend.record.service;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.service.ActivityService;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.record.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class RecordService {
    private Logger logger = LoggerFactory.getLogger(RecordService.class);

    private RecordValidator recordValidator;
    private ActivityService activityService;

    public RecordService(RecordValidator recordValidator, ActivityService activityService) {
        this.recordValidator = recordValidator;
        this.activityService = activityService;
    }

    public ValidationResult addNewRecord(RecordValidator.ValidationObject object) throws Exception {
        logger.info("object to validate = {}", object);
        ValidationResult validationResult = this.recordValidator.validateNewRecordData(object);
        logger.info("validationResult = {}", validationResult.toString());
        if(validationResult.isErrorFree()) {
            Record record = RecordValidator.ValidationObject.toRecord(object);
            Activity rootActivity = this.activityService.findActivityById(record.getActivity().getId());
            rootActivity.addRecord(record);
            logger.info("rootActivity = {}", rootActivity.toString());
            this.activityService.updateActivity(rootActivity, ActivityService.UpdateType.RECORD_UPDATE);
        }
        return validationResult;
    }
}
