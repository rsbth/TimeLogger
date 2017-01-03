package com.mprtcz.timeloggerdesktop.validators;

import com.mprtcz.timeloggerdesktop.handlers.ValidationResult;
import com.mprtcz.timeloggerdesktop.model.Activity;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class ActivityValidator {

    private ValidationResult validationResult;

    public ActivityValidator() {
        this.validationResult = new ValidationResult();
    }

    public ValidationResult validateNewActivity(Activity activity, List<Activity> activities) {
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
