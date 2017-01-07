package com.mprtcz.timeloggerdesktop.validators;

import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.Record;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class RecordValidator {
    private ValidationResult validationResult;

    public ValidationResult validateNewRecordData(
            LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Activity activity) {
        ValidationObject v = new ValidationObject(startTime, endTime, startDate, endDate, activity);
        return this.validateNewRecordData(v);
    }

    public ValidationResult validateNewRecordData(ValidationObject validationObject) {
        this.validationResult = new ValidationResult();
        nullCheck(validationObject);
        hourConsecutivenessCheck(validationObject);
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
        if(v.activity == null) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.ACTIVITY_NULL);
        }
    }

    private void hourConsecutivenessCheck(ValidationObject v) {
        if(v.startDateTime.equals(v.endDateTime)) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_START_EQUAL);
        }
        if(v.startDateTime.isAfter(v.endDateTime)) {
            this.validationResult.addErrorEnum(ValidationResult.CustomErrorEnum.END_DATE_BEFORE);
        }
    }

    @Getter
    public static class ValidationObject {
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Activity activity;

        public ValidationObject(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Activity activity) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.startDate = startDate;
            this.endDate = endDate;
            this.activity = activity;
            this.startDateTime = LocalDateTime.of(startDate, startTime);
            this.endDateTime = LocalDateTime.of(endDate, endTime);
        }

        public static Record toRecord(ValidationObject object) {
            return new Record(object.getStartTime(), object.getEndTime(),
                    object.getStartDate(), object.getEndDate(), object.getActivity());
        }
    }
}
