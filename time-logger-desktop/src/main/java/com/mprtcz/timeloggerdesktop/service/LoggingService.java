package com.mprtcz.timeloggerdesktop.service;

import com.mprtcz.timeloggerdesktop.handlers.ErrorHandler;
import com.mprtcz.timeloggerdesktop.model.Activity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class LoggingService {

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
        if(activity.getName().equals("")) {
            return new ErrorHandler(ErrorHandler.CustomErrorEnum.ACTIVITY_NAME_EMPTY);
        }
        if(checkIfActivityNameExists(activity.getName())) {
            return new ErrorHandler(ErrorHandler.CustomErrorEnum.ACTIVITY_EXISTS);
        }
        saveActivity(activity);
        return new ErrorHandler(ErrorHandler.CustomErrorEnum.SAVED);
    }


}
