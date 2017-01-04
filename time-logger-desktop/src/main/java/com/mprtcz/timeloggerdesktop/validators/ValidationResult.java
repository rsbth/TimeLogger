package com.mprtcz.timeloggerdesktop.validators;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@ToString
public class ValidationResult {

    @Getter
    @ToString
    public enum CustomErrorEnum {
        OK("Validation successful"),
        SAVED("Activity saved"),
        ACTIVITY_NAME_EMPTY("Activity cannot have empty name"),
        ACTIVITY_EXISTS("Activity with that name already exists"),
        START_TIME_NULL("Start time cannot be null"),
        END_TIME_NULL("End time cannot be null"),
        START_DATE_NULL("Start date cannot be null"),
        END_DATE_NULL("End Date cannot be null"),
        END_DATE_BEFORE("End date is before start date"),
        END_START_EQUAL("The activity cannot take 0 hours"),
        ACTIVITY_NULL("Activity cannot be null"),
        ;

        private String value;

        CustomErrorEnum(String value) {
            this.value = value;
        }
    }

    private List<CustomErrorEnum> customErrorEnumList = new ArrayList<>();
    private String customMessage;

    public ValidationResult(CustomErrorEnum customErrorEnum) {
        this.customErrorEnumList = new ArrayList<>();
        this.customErrorEnumList.add(customErrorEnum);
    }

    public ValidationResult() {}

    public void addErrorEnum(CustomErrorEnum customErrorEnum) {
        this.customErrorEnumList = new ArrayList<>();
        this.customErrorEnumList.add(customErrorEnum);
    }

    public String getAllMessages() {
        StringBuilder stringBuilder = new StringBuilder();
        for (CustomErrorEnum c :
                this.customErrorEnumList) {
            stringBuilder.append(c.getValue());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public boolean isErrorFree() {
        if(this.customErrorEnumList.size() == 0) {
            return true;
        }
        if(this.customErrorEnumList.size() == 1) {
            if(this.customErrorEnumList.get(0) == CustomErrorEnum.OK) {
                return true;
            }
        }
        return false;
    }

    String getCustomMessage() {
        if(this.customMessage==null) {
            return "";
        } else {
            return this.customMessage;
        }
    }
}
