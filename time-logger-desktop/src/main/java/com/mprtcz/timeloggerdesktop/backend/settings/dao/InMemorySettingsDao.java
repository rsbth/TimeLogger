package com.mprtcz.timeloggerdesktop.backend.settings.dao;

import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class InMemorySettingsDao implements SettingsDao{
    private Logger logger = LoggerFactory.getLogger(InMemorySettingsDao.class);
    private AppSettings settings;

    public InMemorySettingsDao() {
        logger.info("creating DAO ");
        this.settings = AppSettings.getDefaultInstance();
    }

    @Override
    public void save(AppSettings settings) {
        this.settings = settings;
    }

    @Override
    public AppSettings getSettings() {
        return this.settings;
    }
}
