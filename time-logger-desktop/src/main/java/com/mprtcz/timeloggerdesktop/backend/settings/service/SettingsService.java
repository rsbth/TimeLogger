package com.mprtcz.timeloggerdesktop.backend.settings.service;

import com.mprtcz.timeloggerdesktop.backend.settings.dao.SettingsDao;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings.MAX_VISIBLE_DAYS;
import static com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings.MIN_VISIBLE_DAYS;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class SettingsService {
    private Logger logger = LoggerFactory.getLogger(SettingsService.class);

    private SettingsDao settingsDao;

    public SettingsService(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    public void saveSettings(AppSettings settings) throws Exception {
        this.settingsDao.save(settings);
    }

    public void updateSettings(AppSettings settings) throws Exception {
        logger.info("updating settings");
        AppSettings currentSettings = this.getSettings();
        if(settings.getLanguageEnum() != null) {
            logger.info("currentSettings.getLanguageEnum() = {}", currentSettings.getLanguageEnum());
            currentSettings.setLanguageEnum(settings.getLanguageEnum());
        }
        if(settings.getNumberOfVisibleDays() <= MAX_VISIBLE_DAYS && settings.getNumberOfVisibleDays() >= MIN_VISIBLE_DAYS) {
            currentSettings.setNumberOfVisibleDays(settings.getNumberOfVisibleDays());
        }
        currentSettings.setGraphicVisible(settings.isGraphicVisible());
        currentSettings.setHeadersVisible(settings.isHeadersVisible());
        logger.info("currentSettings = {}", currentSettings);
        this.settingsDao.update(currentSettings);
    }

    public AppSettings getSettings() throws Exception {
        logger.info("this.settingsDao.getSettings().toString() = {}", this.settingsDao.getSettings().toString());
        return this.settingsDao.getSettings();
    }
}
