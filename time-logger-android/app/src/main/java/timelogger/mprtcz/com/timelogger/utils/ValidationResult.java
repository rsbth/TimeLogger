package timelogger.mprtcz.com.timelogger.utils;

import android.support.annotation.StringRes;

import lombok.Getter;
import lombok.ToString;
import timelogger.mprtcz.com.timelogger.R;

/**
 * Created by Azet on 2017-01-19.
 */
@Getter
@ToString
public class ValidationResult {

    @Getter
    @ToString
    public enum CustomErrorEnum {

        OK(R.string.taskValidationSuccessful, ValidationStatus.SUCCESSFUL),
        TASK_SAVED(R.string.taskValidationSuccessful, ValidationStatus.SUCCESSFUL),

        TASK_NAME_EMPTY(R.string.taskNameEmpty, ValidationStatus.FAILED),
        TASK_COLOR_EMPTY(R.string.taskColorEmpty, ValidationStatus.FAILED),
        TASK_ID_EMPTY(R.string.taskIdEmpty, ValidationStatus.FAILED),
        TASK_EXISTS(R.string.taskNameExists, ValidationStatus.FAILED);


        private int value;
        private ValidationStatus validationStatus;

        CustomErrorEnum(@StringRes int value, ValidationStatus validationStatus) {
            this.value = value;
            this.validationStatus = validationStatus;
        }
    }

    private CustomErrorEnum customErrorEnum;

    public ValidationResult(CustomErrorEnum customErrorEnum) {
        this.customErrorEnum = customErrorEnum;
    }

    public void getNewErrorEnum(CustomErrorEnum customErrorEnum) {
        this.customErrorEnum = customErrorEnum;
    }

    private enum ValidationStatus {
        SUCCESSFUL,
        FAILED
    }

    public boolean isErrorFree() {
        return this.customErrorEnum.getValidationStatus() == ValidationStatus.SUCCESSFUL;
    }


}
