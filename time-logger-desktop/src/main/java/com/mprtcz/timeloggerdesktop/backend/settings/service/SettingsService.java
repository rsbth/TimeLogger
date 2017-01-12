package com.mprtcz.timeloggerdesktop.backend.settings.service;

import com.mprtcz.timeloggerdesktop.backend.settings.dao.SettingsDao;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.validator.SettingsValidator;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

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
        } catch (FileNotFoundException fileNotFoundException) {
            logger.info("Exception occured : " +fileNotFoundException.getStackTrace().toString());
            this.saveSettings(AppSettings.getDefaultInstance());
        }
        return this.settingsDao.getSettings();
    }
}
