package com.mprtcz.timeloggerdesktop.backend.settings.validator;

import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;

import static com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings.MAX_VISIBLE_DAYS;
import static com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings.MIN_VISIBLE_DAYS;

/**
 * Created by mprtcz on 2017-01-12.
 */
public class SettingsValidator {
    ValidationResult validationResult;

    public ValidationResult validateSettingsData(AppSettings appSettings) {
        this.validationResult = new ValidationResult(ValidationResult.CustomErrorEnum.SETTINGS_SAVED);
        nullCheck(appSettings);
        constraintCheck(appSettings);
        return this.validationResult;
    }

    private void nullCheck(AppSettings appSettings) {
        if(appSettings.getLanguage() == null) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.LANGUAGE_NULL);
        }
    }

    private void constraintCheck(AppSettings appSettings) {
        if(appSettings.getNumberOfVisibleDays() > MAX_VISIBLE_DAYS) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.VISIBLE_DAYS_TOO_HIGH);
        }
        if(appSettings.getNumberOfVisibleDays() < MIN_VISIBLE_DAYS) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.VISIBLE_DAYS_TOO_LOW);
        }
    }


}
