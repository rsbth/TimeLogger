package com.mprtcz.timeloggerdesktop.validators;

import com.mprtcz.timeloggerdesktop.handlers.ValidationResult;
import com.mprtcz.timeloggerdesktop.model.Record;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class RecordValidator {
    private ValidationResult validationResult;

    public RecordValidator() {
        this.validationResult = new ValidationResult();
    }

    public ValidationResult validateNewRecordData(Record recordToValidate) {
        nullCheck(recordToValidate);
        hourConsecutivenessCheck(recordToValidate);
        return this.validationResult;
    }

    private void nullCheck(Record recordToValidate) {
        if(recordToValidate.getStartTime() == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.START_TIME_NULL);
        }
        if(recordToValidate.getEndTime() == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_TIME_NULL);
        }
        if(recordToValidate.getStartDate() == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.START_DATE_NULL);
        }
        if(recordToValidate.getEndDate() == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_DATE_NULL);
        }
    }

    private void hourConsecutivenessCheck(Record recordToValidate) {
        if(recordToValidate.getStartDate().isAfter(recordToValidate.getEndDate())) { //end date before start date
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_DATE_BEFORE);
        }
        if(recordToValidate.getStartDate() == recordToValidate.getEndDate()) {
            if(recordToValidate.getStartTime().isAfter(recordToValidate.getEndTime())) { // end time before start time within the same day
                this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_TIME_BEFORE);
            }
        }
    }
}
