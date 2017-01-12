package com.mprtcz.timeloggerdesktop.backend.settings.dao;

import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.model.LanguageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class InMemorySettingsDao implements SettingsDao{
    private Logger logger = LoggerFactory.getLogger(InMemorySettingsDao.class);
    private AppSettings settings;
    private static final LanguageEnum BASIC_LOCALE = LanguageEnum.ENGLISH;
    private static final int DAYS_VISIBLE = 10;
    private static final boolean IS_GRAPHIC_VISIBLE = true;
    private static final boolean ARE_HEADERS_VISIBLE = true;

    public InMemorySettingsDao() {
        logger.info("creating DAO ");
        this.settings = new AppSettings(BASIC_LOCALE, DAYS_VISIBLE, IS_GRAPHIC_VISIBLE, ARE_HEADERS_VISIBLE);
    }

    @Override
    public void save(AppSettings settings) {
        this.settings = settings;
    }

    @Override
    public void update(AppSettings settings) {
        this.settings = settings;
    }

    @Override
    public AppSettings getSettings() {
        return this.settings;
    }
}
