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

    public ValidationResult addActivity(Activity activity) throws Exception {
        logger.info("new activity.toString() = {}", activity.toString());
        return validateNewActivityAndSave(activity);
    }

    public Activity findActivityById(Long id) throws Exception {
        return this.customDao.findActivityById(id);
    }

    private void chooseColorAndSave(Activity activity) throws Exception {
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

    public ValidationResult updateActivity(Activity activity) throws Exception {
        logger.info("updateActivity activity = {}", activity);
        ValidationResult validationResult = activityValidator.validateUpdatedActivity(activity);
        if (validationResult.isErrorFree()) {
            this.customDao.update(activity);
            return new ValidationResult(ValidationResult.CustomErrorEnum.ACTIVITY_UPDATED);
        } else {
            return validationResult;
        }
    }

    public HoursData getHoursData() throws Exception {
        return new HoursData(getActivities());
    }
}
