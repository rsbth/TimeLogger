package com.mprtcz.timeloggerdesktop.service;

import com.mprtcz.timeloggerdesktop.handlers.ErrorHandler;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.validators.ActivityValidator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class LoggingService {

    private ActivityValidator activityValidator;

    public LoggingService(ActivityValidator activityValidator) {
        this.activityValidator = activityValidator;
    }

    Activity createNewActivity(String name, String description) {
        return new Activity(name, description);
    }

    public boolean checkIfActivityNameExists(String name) {
        for (Activity activity :
                getActivities()) {
            if (activity.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Activity> getActivities() {
        return Activity.activities;
    }

    public List<String> getActivityNames() {
        return getActivities().stream().map(Activity::getName).collect(Collectors.toList());
    }

    public ErrorHandler addActivity(String name, String description) {
        Activity activity = new Activity(name, description);
        return validateNewActivityAndSave(activity);
    }

    public void saveActivity(Activity activity) {
        Activity.activities.add(activity);
    }

    public ErrorHandler validateNewActivityAndSave(Activity activity) {
        ErrorHandler errorHandler = activityValidator.validateNewActivity(activity, getActivities());
        if(errorHandler.getCustomErrorEnum() == ErrorHandler.CustomErrorEnum.OK) {
            saveActivity(activity);
            return new ErrorHandler(ErrorHandler.CustomErrorEnum.SAVED);
        } else {
            return errorHandler;
        }
    }
}
