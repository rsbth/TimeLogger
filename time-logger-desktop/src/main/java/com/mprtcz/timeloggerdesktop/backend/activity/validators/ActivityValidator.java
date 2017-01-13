package com.mprtcz.timeloggerdesktop.backend.activity.validators;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class ActivityValidator {

    private ValidationResult validationResult;

    public ActivityValidator() {}

    public ValidationResult validateNewActivity(Activity activity, List<Activity> activities) {
        this.validationResult = new ValidationResult(ValidationResult.CustomErrorEnum.ACTIVITY_SAVED);
        if(activity.getName().equals("")) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_NAME_EMPTY);
        }
        checkIfActivityNameExists(activity.getName(), activities);
        return this.validationResult;
    }

    public ValidationResult validateUpdatedActivity(Activity activity) {
        this.validationResult = new ValidationResult(ValidationResult.CustomErrorEnum.ACTIVITY_SAVED);
        if(activity.getName().equals("") || activity.getName() ==null ) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_NAME_EMPTY);
        }
        if(activity.getColor().equals("") || activity.getColor() ==null ) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_COLOR_EMPTY);
        }
        if(activity.getId() < 1 || activity.getId() ==null ) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_ID_EMPTY);
        }
        return this.validationResult;
    }

    private void checkIfActivityNameExists(String name, List<Activity> activities) {
        for (Activity activity :
                activities) {
            if (activity.getName().equals(name)) {
                this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_EXISTS);
                break;
            }
        }
    }
}
