package timelogger.mprtcz.com.timelogger.record.validator;

import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-20.
 */

public class RecordValidator {
    private ValidationResult validationResult;

    public ValidationResult validateNewRecordData(Record validationObject) {
        this.validationResult = new ValidationResult(ValidationResult.CustomErrorEnum.RECORD_SAVED);
        nullCheck(validationObject);
        hourConsecutivenessCheck(validationObject);
        System.out.println("this.validationResult = " + this.validationResult);
        return this.validationResult;
    }

    private void nullCheck(Record v) {
        if(v.getStartDateTime() == null) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.START_TIME_NULL);
        }
        if(v.getEndDateTime() == null) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.END_TIME_NULL);
        }
        if(v.getTask() == null) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.TASK_NULL);
        }
    }

    private void hourConsecutivenessCheck(Record v) {
        if(v.getStartDateTime().equals(v.getEndDateTime())) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.END_START_EQUAL);
        }
        if(v.getStartDateTime().after(v.getEndDateTime())) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.END_DATE_BEFORE);
        }
    }
}
