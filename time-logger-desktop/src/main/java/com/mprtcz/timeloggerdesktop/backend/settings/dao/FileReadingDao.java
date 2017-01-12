package com.mprtcz.timeloggerdesktop.backend.settings.dao;

import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.model.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class FileReadingDao implements SettingsDao {
    private Logger logger = LoggerFactory.getLogger(FileReadingDao.class);
    private static final String SETTINGS_PATH = "./app.properties";

    private Properties properties;

    public FileReadingDao() {
        this.properties = new Properties();
    }

    @Override
    public void save(AppSettings settings) throws IOException {
        logger.info("saving settings = {}", settings);
        OutputStream outputStream = new FileOutputStream(SETTINGS_PATH);
        this.properties.setProperty("language_enum", settings.getLanguage().getName());
        this.properties.setProperty("num_of_vis_days", String.valueOf(settings.getNumberOfVisibleDays()));
        this.properties.setProperty("is_graphic_visible", String.valueOf(settings.isGraphicVisible()));
        this.properties.setProperty("headers_visible", String.valueOf(settings.isHeadersVisible()));
        this.properties.store(outputStream, null);
    }

    @Override
    public AppSettings getSettings() throws Exception {
        FileInputStream input = new FileInputStream(SETTINGS_PATH);
        this.properties.load(input);
        Language language = Language.valueOf(this.properties.getProperty("language_enum").toUpperCase());
        int numberOfVisibleDays = Integer.parseInt(this.properties.getProperty("num_of_vis_days"));
        boolean isGraphicVisible = Boolean.parseBoolean(this.properties.getProperty("is_graphic_visible"));
        boolean areHeadersVisible = Boolean.parseBoolean(this.properties.getProperty("headers_visible"));
        return new AppSettings(language, numberOfVisibleDays, isGraphicVisible, areHeadersVisible);
    }
}
