package com.mprtcz.timeloggerdesktop.backend.settings.service;

import com.mprtcz.timeloggerdesktop.backend.settings.dao.SettingsDao;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.validator.SettingsValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class SettingsService {
    private Logger logger = LoggerFactory.getLogger(SettingsService.class);

    private SettingsDao settingsDao;

    public SettingsService(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    private void saveSettings(AppSettings settings) throws Exception {
        logger.info("saving settings = {}", settings);
        this.settingsDao.save(settings);
    }

    public ValidationResult updateSettings(AppSettings settings) throws Exception {
        logger.info("updating settings");
        AppSettings currentSettings = this.getSettings();
        SettingsValidator settingsValidator = new SettingsValidator();
        ValidationResult result = settingsValidator.validateSettingsData(settings);
        if(result.isErrorFree()) {
            currentSettings = settings;
            this.settingsDao.save(currentSettings);
        }
        logger.info("currentSettings = {}", currentSettings);
        return result;
    }

    public AppSettings getSettings() throws Exception {
        try {
            AppSettings settings = this.settingsDao.getSettings();
            logger.info("this.settingsDao.getSettings().toString() = {}", settings.toString());
        } catch (FileNotFoundException | NullPointerException | IndexOutOfBoundsException exception ) {
            logger.info("Exception occured : " + Arrays.toString(exception.getStackTrace()));
            this.saveSettings(AppSettings.getDefaultInstance());
        }
        logger.info("this.settingsDao.getSettings() = {}", this.settingsDao.getSettings().toString());
        return this.settingsDao.getSettings();
    }
}
