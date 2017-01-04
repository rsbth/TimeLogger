package com.mprtcz.timeloggerdesktop.service;

import com.mprtcz.timeloggerdesktop.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.validators.ValidationResult;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.Record;
import com.mprtcz.timeloggerdesktop.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.validators.RecordValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    Activity createNewActivity(String name, String description) {
        return new Activity(name, description);
    }


    public List<Activity> getActivities() throws Exception {
        return customDao.getAllActivities();
    }

    public List<String> getActivityNames() throws Exception {
        return getActivities().stream().map(Activity::getName).collect(Collectors.toList());
    }

    public ValidationResult addActivity(String name, String description) throws Exception {
        Activity activity = new Activity(name, description);
        return validateNewActivityAndSave(activity);
    }

    public void saveActivity(Activity activity) throws Exception {
        chooseActivityColor(activity);
        customDao.save(activity);
    }

    public ValidationResult validateNewActivityAndSave(Activity activity) throws Exception {
        ValidationResult validationResult = activityValidator.validateNewActivity(activity, getActivities());
        if (validationResult.isErrorFree()) {
            saveActivity(activity);
            return new ValidationResult(ValidationResult.CustomErrorEnum.SAVED);
        } else {
            return validationResult;
        }
    }

    public ValidationResult addNewRecord(LocalTime startTime, LocalTime endTime,
                                         LocalDate startDate, LocalDate endDate, Activity activity) {
        Record recordToValidate = new Record(startTime, endTime, startDate, endDate, activity);
        System.out.println("recordToValidate = " + recordToValidate);
        ValidationResult validationResult = this.recordValidator.validateNewRecordData(recordToValidate);
        if (validationResult.isErrorFree()) {
            //TODO record saving
        }
        return validationResult;
    }

    private void chooseActivityColor(Activity activity) throws Exception {
        List<Activity> activities = getActivities();
        if(Activity.colorCodes.size() >= activities.size()) {
            activity.setColor(Activity.colorCodes.get(activities.size()));
        } else {
            activity.setColor(Activity.colorCodes.get(0));
        }
    }
}
