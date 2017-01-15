package com.mprtcz.timeloggerdesktop.backend.utilities;

import lombok.Getter;
import lombok.ToString;

import java.util.Locale;
import java.util.ResourceBundle;

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
        OK(messages.getString("ok"), ValidationStatus.SUCCESSFUL),
        ACTIVITY_SAVED(messages.getString("activity_saved"), ValidationStatus.SUCCESSFUL),
        SETTINGS_SAVED(messages.getString("settings_saved"), ValidationStatus.SUCCESSFUL),
        RECORD_SAVED(messages.getString("record_saved"), ValidationStatus.SUCCESSFUL),
        ACTIVITY_UPDATED(messages.getString("activity_updated"), ValidationStatus.SUCCESSFUL),
        DATA_EXPORTED(messages.getString("data_exported"), ValidationStatus.SUCCESSFUL),

        ACTIVITY_NAME_EMPTY(messages.getString("activity_name_empty"), ValidationStatus.FAILED),
        ACTIVITY_COLOR_EMPTY(messages.getString("activity_color_empty"), ValidationStatus.FAILED),
        ACTIVITY_ID_EMPTY(messages.getString("activity_id_empty"), ValidationStatus.FAILED),
        ACTIVITY_EXISTS(messages.getString("activity_exists"), ValidationStatus.FAILED),
        START_TIME_NULL(messages.getString("start_time_null"), ValidationStatus.FAILED),
        END_TIME_NULL(messages.getString("end_time_null"), ValidationStatus.FAILED),
        START_DATE_NULL(messages.getString("start_date_null"), ValidationStatus.FAILED),
        END_DATE_NULL(messages.getString("end_date_null"), ValidationStatus.FAILED),
        END_DATE_BEFORE(messages.getString("end_date_before"), ValidationStatus.FAILED),
        END_START_EQUAL(messages.getString("end_start_equal"), ValidationStatus.FAILED),
        ACTIVITY_NULL(messages.getString("activity_null"), ValidationStatus.FAILED),
        LANGUAGE_NULL(messages.getString("language_null"), ValidationStatus.FAILED),
        VISIBLE_DAYS_TOO_HIGH(messages.getString("visible_days_too_high"), ValidationStatus.FAILED),
        VISIBLE_DAYS_TOO_LOW(messages.getString("visible_days_too_low"), ValidationStatus.FAILED),;

        private String value;
        ValidationStatus validationStatus;

        CustomErrorEnum(String value, ValidationStatus validationStatus) {
            this.value = value;
            this.validationStatus = validationStatus;
        }
    }

    private enum ValidationStatus {
        SUCCESSFUL,
        FAILED
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
        return this.customErrorEnum.getValidationStatus() == ValidationStatus.SUCCESSFUL;
    }

}
