package com.mprtcz.timeloggerdesktop.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.Record;

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

    private Dao<Activity, Long> activityDao;
    private Dao<Record, Long> recordDao;

    public DatabaseActivityCustomDao() throws SQLException {
        this.activityDao = DaoManager.createDao(connectionSource, Activity.class);
        this.recordDao = DaoManager.createDao(connectionSource, Record.class);
        if(!this.activityDao.isTableExists()) {
            TableUtils.createTable(connectionSource, Activity.class);
        }
        if(!this.recordDao.isTableExists()) {
            TableUtils.createTable(connectionSource, Record.class);
        }
    }

    @Override
    public <T> void save(T t) throws Exception {
        if(t instanceof Activity) {
            activityDao.create((Activity) t);
        }
        if(t instanceof Record) {
            recordDao.create((Record) t);
        }
    }

    @Override
    public List<Activity> getAllActivities() throws SQLException {
        return this.activityDao.queryForAll();
    }

    @Override
    public List<Record> getAllRecords() throws SQLException {
        return this.recordDao.queryForAll();
    }
}
