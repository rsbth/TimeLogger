package com.mprtcz.timeloggerdesktop.handlers;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@ToString
public class ErrorHandler {

    @Getter
    @ToString
    public enum CustomErrorEnum {
        ACTIVITY_NAME_EMPTY("Activity cannot have empty name"),
        ACTIVITY_EXISTS("Activity with that name already exists"),
        SAVED("Activity saved"),;

        private String value;

        CustomErrorEnum(String value) {
            this.value = value;
        }
    }

    private CustomErrorEnum customErrorEnum;
    private String customMessage;

    public ErrorHandler(CustomErrorEnum customErrorEnum, String customMessage) {
        this.customErrorEnum = customErrorEnum;
        this.customMessage = customMessage;
    }

    public ErrorHandler(CustomErrorEnum customErrorEnum) {
        this.customErrorEnum = customErrorEnum;
    }

    String getCustomMessage() {
        if(this.customMessage==null) {
            return "";
        } else {
            return this.customMessage;
        }
    }
}
