package com.mprtcz.timeloggerdesktop.backend.settings.dao;

import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;

/**
 * Created by mprtcz on 2017-01-10.
 */
public interface SettingsDao {
    void save(AppSettings settings) throws Exception;

    void update(AppSettings settings) throws Exception;

    AppSettings getSettings() throws Exception;
}
