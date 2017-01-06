package com.mprtcz.timeloggerdesktop.service;

import com.mprtcz.timeloggerdesktop.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.DataRepresentation;
import com.mprtcz.timeloggerdesktop.model.Record;
import com.mprtcz.timeloggerdesktop.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.validators.RecordValidator;
import com.mprtcz.timeloggerdesktop.validators.ValidationResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        List<Activity> activities = customDao.getAllActivities();
        System.out.println("ACTIVITIES = " + activities.toString());
        System.out.println("activities.size() = " + activities.size());
        for (Activity a :
                activities) {
            if (a.getActivityRecords() == null) {
                break;
            }
            System.out.println("a.getActivityRecords() = " + a.getActivityRecords().toString());
            System.out.println("a.getActivityRecords().size() = " + a.getActivityRecords().size());
        }
        DataRepresentation dataRepresentation = new DataRepresentation(activities);
        return activities;
    }

    public List<String> getActivityNames() throws Exception {
        return getActivities().stream().map(Activity::getName).collect(Collectors.toList());
    }

    public ValidationResult addActivity(String name, String description) throws Exception {
        System.out.println("name = " + name);
        System.out.println("description = " + description);
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

    public ValidationResult addNewRecord(LocalTime startTime, LocalTime endTime,
                                         LocalDate startDate, LocalDate endDate, Activity activity) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(startDate, startTime);
        ValidationResult validationResult = this.recordValidator.validateNewRecordData(
                startTime, endTime, startDate, endDate, activity);
        if (validationResult.isErrorFree()) {
            Record newRecord = new Record(startTime, endTime, startDate, endDate, activity);
            Activity rootActivity = customDao.findActivityById(activity.getId());
            rootActivity.addRecord(newRecord);
            System.out.println("rootActivity.toString() = " + rootActivity.toString());
            customDao.update(rootActivity);
            getActivities();
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
