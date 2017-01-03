package com.mprtcz.timeloggerdesktop.validators;

import com.mprtcz.timeloggerdesktop.handlers.ErrorHandler;
import com.mprtcz.timeloggerdesktop.model.Activity;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class ActivityValidator {

    public ErrorHandler validateNewActivity(Activity activity, List<Activity> activities) {
        if(activity.getName().equals("")) {
            return new ErrorHandler(ErrorHandler.CustomErrorEnum.ACTIVITY_NAME_EMPTY);
        }
        if(checkIfActivityNameExists(activity.getName(), activities)) {
            return new ErrorHandler(ErrorHandler.CustomErrorEnum.ACTIVITY_EXISTS);
        }
        return new ErrorHandler(ErrorHandler.CustomErrorEnum.OK);
    }

    private boolean checkIfActivityNameExists(String name, List<Activity> activities) {
        for (Activity activity :
                activities) {
            if (activity.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
