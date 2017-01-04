package com.mprtcz.timeloggerdesktop.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mprtcz.timeloggerdesktop.model.Activity;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-04.
 */
public class DatabaseActivityCustomDao implements CustomDao {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String MYSQL_DATABASE_URL = "jdbc:mysql://localhost:3306/timeloggerdb";
    private static final String SQLITE_DATABASE_URL = "jdbc:sqlite:timeloggertest.db";

    private ConnectionSource connectionSource = new JdbcConnectionSource(SQLITE_DATABASE_URL);

    private Dao<Activity, Long> dao;

    public DatabaseActivityCustomDao() throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, Activity.class);
        if(!this.dao.isTableExists()) {
            TableUtils.createTable(connectionSource, Activity.class);
        }
    }

    @Override
    public void save(Activity activity) throws SQLException {
        this.dao.create(activity);
    }

    @Override
    public List<Activity> getAll() throws SQLException {
        return this.dao.queryForAll();
    }
}
