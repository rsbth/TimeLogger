package com.mprtcz.timeloggerdesktop.backend.utilities;

import lombok.Getter;
import lombok.ToString;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult.CustomErrorEnum.ACTIVITY_SAVED;
import static com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult.CustomErrorEnum.RECORD_SAVED;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@ToString
public class ValidationResult {
    public static ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());

    @Getter
    @ToString
    public enum CustomErrorEnum {
        OK(messages.getString("ok")),
        ACTIVITY_SAVED(messages.getString("activity_saved")),
        RECORD_SAVED(messages.getString("record_saved")),
        ACTIVITY_NAME_EMPTY(messages.getString("activity_name_empty")),
        ACTIVITY_EXISTS(messages.getString("activity_exists")),
        START_TIME_NULL(messages.getString("start_time_null")),
        END_TIME_NULL(messages.getString("end_time_null")),
        START_DATE_NULL(messages.getString("start_date_null")),
        END_DATE_NULL(messages.getString("end_date_null")),
        END_DATE_BEFORE(messages.getString("end_date_before")),
        END_START_EQUAL(messages.getString("end_start_equal")),
        ACTIVITY_NULL(messages.getString("activity_null"));

        private String value;

        CustomErrorEnum(String value) {
            System.out.println("CONSTRUCTING ENUM = " + value);
            this.value = value;
        }
    }

    private CustomErrorEnum customErrorEnum;
    private String customMessage;

    public ValidationResult(CustomErrorEnum customErrorEnum) {
        this.customErrorEnum = customErrorEnum;
    }


    public void getNewErrorEnum(CustomErrorEnum customErrorEnum) {
        this.customErrorEnum = customErrorEnum;
    }

    public String getEnumMessage() {
        return this.customErrorEnum.getValue();
    }

    public boolean isErrorFree() {
        return this.customErrorEnum == CustomErrorEnum.OK || this.customErrorEnum == ACTIVITY_SAVED || this.customErrorEnum == RECORD_SAVED;
    }

}
