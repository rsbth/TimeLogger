package com.mprtcz.timeloggerdesktop.validators;

import com.mprtcz.timeloggerdesktop.model.Activity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class RecordValidator {
    private ValidationResult validationResult;

    public RecordValidator() {
        this.validationResult = new ValidationResult();
    }

    public ValidationResult validateNewRecordData(
            LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Activity activity) {
        ValidationObject v = new ValidationObject(startTime, endTime, startDate, endDate, activity);
        nullCheck(v);
        hourConsecutivenessCheck(v);
        return this.validationResult;
    }

    private void nullCheck(ValidationObject v) {
        if(v.startTime == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.START_TIME_NULL);
        }
        if(v.endTime == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_TIME_NULL);
        }
        if(v.startDate == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.START_DATE_NULL);
        }
        if(v.endDate == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_DATE_NULL);
        }
    }

    private void hourConsecutivenessCheck(ValidationObject v) {
        if(v.startDate.isAfter(v.endDate)) { //end date before start date
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_DATE_BEFORE);
        }
        if(v.startDate == v.endDate) {
            if(v.startTime.isAfter(v.endTime)) { // end time before start time within the same day
                this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_TIME_BEFORE);
            }
        }
    }
    
    private class ValidationObject {
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Activity activity;

        ValidationObject(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Activity activity) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.startDate = startDate;
            this.endDate = endDate;
            this.activity = activity;
            this.startDateTime = LocalDateTime.of(startDate, startTime);
            this.endDateTime = LocalDateTime.of(endDate, endTime);
        }
    }
}
