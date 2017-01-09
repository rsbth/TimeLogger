package com.mprtcz.timeloggerdesktop.service;

import com.mprtcz.timeloggerdesktop.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.DataRepresentation;
import com.mprtcz.timeloggerdesktop.model.Record;
import com.mprtcz.timeloggerdesktop.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.validators.ValidationResult;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class Service {

    private ActivityValidator activityValidator;
    private RecordValidator recordValidator;
    private CustomDao customDao;

    public Service(ActivityValidator activityValidator,
                   RecordValidator recordValidator,
                   CustomDao customDao) {
        this.customDao = customDao;
        this.activityValidator = activityValidator;
        this.recordValidator = recordValidator;
    }

    public List<Activity> getActivities() throws Exception {
        List<Activity> activities = customDao.getAllActivities();
        for (Activity a :
                activities) {
            if (a.getActivityRecords() == null) {
                break;
            }
        }
        return activities;
    }

    public ValidationResult addActivity(String name, String description) throws Exception {
        Activity activity = new Activity(name, description);
        return validateNewActivityAndSave(activity);
    }

    private void chooseColorAndSave(Activity activity) throws Exception {
        chooseActivityColor(activity);
        customDao.save(activity);
    }

    private ValidationResult validateNewActivityAndSave(Activity activity) throws Exception {
        ValidationResult validationResult = activityValidator.validateNewActivity(activity, getActivities());
        if (validationResult.isErrorFree()) {
            chooseColorAndSave(activity);
            return new ValidationResult(ValidationResult.CustomErrorEnum.SAVED);
        } else {
            return validationResult;
        }
    }

    public ValidationResult addNewRecord(RecordValidator.ValidationObject object) throws Exception {
        ValidationResult validationResult = this.recordValidator.validateNewRecordData(object);
        if(validationResult.isErrorFree()) {
            Record record = RecordValidator.ValidationObject.toRecord(object);
            Activity rootActivity = customDao.findActivityById(record.getActivity().getId());
            rootActivity.addRecord(record);
            customDao.update(rootActivity);
        }
        return validationResult;
    }

    public void updateActivity(Activity activity) throws Exception {
        this.customDao.update(activity);
    }

    private void chooseActivityColor(Activity activity) throws Exception {
        List<Activity> activities = getActivities();
        if (Activity.colorCodes.size() >= activities.size()) {
            activity.setColor(Activity.colorCodes.get(activities.size()));
        } else {
            activity.setColor(Activity.colorCodes.get(0));
        }
    }

    public DataRepresentation getHours() throws Exception {
        return new DataRepresentation(getActivities());
    }
}
