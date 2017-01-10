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
        this.validationResult = new ValidationResult();
        if(activity.getName().equals("")) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_NAME_EMPTY);
        }
        checkIfActivityNameExists(activity.getName(), activities);
        return this.validationResult;
    }

    private void checkIfActivityNameExists(String name, List<Activity> activities) {
        for (Activity activity :
                activities) {
            if (activity.getName().equals(name)) {
                this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_EXISTS);
                break;
            }
        }
    }
}
