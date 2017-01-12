package com.mprtcz.timeloggerdesktop.backend.settings.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mprtcz.timeloggerdesktop.backend.settings.model.AppSettings;
import com.mprtcz.timeloggerdesktop.backend.settings.model.Language;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-12.
 */
public class DatabaseSettingsDao implements SettingsDao {
    private static final String SQLITE_DATABASE_URL = "jdbc:sqlite:timeloggertest.db";
    private ConnectionSource connectionSource = new JdbcConnectionSource(SQLITE_DATABASE_URL);

    private Dao<AppSettings, Long> settingsDao;
    private Dao<Language, String> languageDao;

    public DatabaseSettingsDao() throws SQLException {
        this.settingsDao = DaoManager.createDao(connectionSource, AppSettings.class);
        if (!this.settingsDao.isTableExists()) {
            TableUtils.createTable(connectionSource, AppSettings.class);
        }
    }

    @Override
    public void save(AppSettings settings) throws Exception {
        System.out.println("saving attempt, settings = " + settings);
        this.settingsDao.createOrUpdate(settings);
    }

    @Override
    public AppSettings getSettings() throws Exception {
        List<AppSettings> list = this.settingsDao.queryForAll();
        return list.get(0);
    }
}
