package com.mprtcz.timeloggerdesktop.backend.settings.dao;

import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.model.LanguageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class FileReadingDao implements SettingsDao {
    private Logger logger = LoggerFactory.getLogger(FileReadingDao.class);
    public static final String SETTINGS_PATH = "./app.properties";

    private Properties properties;

    public FileReadingDao() {
        this.properties = new Properties();
    }

    @Override
    public void save(AppSettings settings) throws IOException {
        logger.info("saving settings = {}", settings);
        OutputStream outputStream = new FileOutputStream(SETTINGS_PATH);
        this.properties.setProperty("language_enum", settings.getLanguageEnum().getName());
        this.properties.setProperty("num_of_vis_days", String.valueOf(settings.getNumberOfVisibleDays()));
        this.properties.setProperty("is_graphic_visible", String.valueOf(settings.isGraphicVisible()));
        this.properties.setProperty("headers_visible", String.valueOf(settings.isHeadersVisible()));
        this.properties.store(outputStream, null);
    }

    @Override
    public void update(AppSettings settings) throws IOException {
        this.save(settings);
    }

    @Override
    public AppSettings getSettings() throws Exception {
        FileInputStream input = new FileInputStream(SETTINGS_PATH);
        this.properties.load(input);
        LanguageEnum language = LanguageEnum.of(this.properties.getProperty("language_enum"));
        int numberOfVisibleDays = Integer.parseInt(this.properties.getProperty("num_of_vis_days"));
        boolean isGraphicVisible = Boolean.parseBoolean(this.properties.getProperty("is_graphic_visible"));
        boolean areHeadersVisible = Boolean.parseBoolean(this.properties.getProperty("headers_visible"));
        return new AppSettings(language, numberOfVisibleDays, isGraphicVisible, areHeadersVisible);
    }
}
