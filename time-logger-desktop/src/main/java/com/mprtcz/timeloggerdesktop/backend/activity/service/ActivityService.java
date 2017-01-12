package com.mprtcz.timeloggerdesktop.backend.activity.service;

import com.mprtcz.timeloggerdesktop.backend.activity.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import com.mprtcz.timeloggerdesktop.backend.activity.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class ActivityService {
    private Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private ActivityValidator activityValidator;
    private CustomDao customDao;

    public ActivityService(ActivityValidator activityValidator,
                           CustomDao customDao) {
        this.customDao = customDao;
        this.activityValidator = activityValidator;
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

    public Activity findActivityById(Long id) throws Exception {
        return this.customDao.findActivityById(id);
    }

    private void chooseColorAndSave(Activity activity) throws Exception {
        chooseActivityColor(activity);
        customDao.save(activity);
    }

    private ValidationResult validateNewActivityAndSave(Activity activity) throws Exception {
        ValidationResult validationResult = activityValidator.validateNewActivity(activity, getActivities());
        if (validationResult.isErrorFree()) {
            chooseColorAndSave(activity);
            return new ValidationResult(ValidationResult.CustomErrorEnum.RECORD_SAVED);
        } else {
            return validationResult;
        }
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

    public HoursData getHoursData() throws Exception {
        return new HoursData(getActivities());
    }


}
