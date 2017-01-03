package com.mprtcz.timeloggerdesktop.service;

import com.mprtcz.timeloggerdesktop.handlers.ValidationResult;
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

    public Service(ActivityValidator activityValidator,
                   RecordValidator recordValidator) {
        this.activityValidator = activityValidator;
        this.recordValidator = recordValidator;
    }

    Activity createNewActivity(String name, String description) {
        return new Activity(name, description);
    }


    public List<Activity> getActivities() {
        return Activity.activities;
    }

    public List<String> getActivityNames() {
        return getActivities().stream().map(Activity::getName).collect(Collectors.toList());
    }

    public ValidationResult addActivity(String name, String description) {
        Activity activity = new Activity(name, description);
        return validateNewActivityAndSave(activity);
    }

    public void saveActivity(Activity activity) {
        Activity.activities.add(activity);
    }

    public ValidationResult validateNewActivityAndSave(Activity activity) {
        ValidationResult validationResult = activityValidator.validateNewActivity(activity, getActivities());
        if (validationResult.isErrorFree()) {
            saveActivity(activity);
            return new ValidationResult(ValidationResult.CustomErrorEnum.SAVED);
        } else {
            return validationResult;
        }
    }

    public ValidationResult addNewRecord(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        Record recordToValidate = new Record(startTime, endTime, startDate, endDate);
        ValidationResult validationResult = this.recordValidator.validateNewRecordData(recordToValidate);
        if (validationResult.isErrorFree()) {
            //TODO record saving
        }
        return validationResult;
    }
}
