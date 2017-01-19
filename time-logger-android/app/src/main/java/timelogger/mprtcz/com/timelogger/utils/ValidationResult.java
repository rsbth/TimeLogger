package timelogger.mprtcz.com.timelogger.utils;

import android.content.res.Resources;

import lombok.Getter;
import lombok.ToString;
import timelogger.mprtcz.com.timelogger.R;

/**
 * Created by Azet on 2017-01-19.
 */

public class ValidationResult {

    @Getter
    @ToString
    public enum CustomErrorEnum {
        OK(Resources.getSystem().getString(R.string.validationSuccessFul), ValidationStatus.SUCCESSFUL),

        ACTIVITY_EXISTS(Resources.getSystem().getString(R.string.activityExists), ValidationStatus.FAILED),
        ;


        private String value;
        private ValidationStatus validationStatus;

        CustomErrorEnum(String value, ValidationStatus validationStatus) {
            this.value = value;
            this.validationStatus = validationStatus;
        }
        }

    private enum ValidationStatus {
        SUCCESSFUL,
        FAILED
    }
}
