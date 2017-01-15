package com.mprtcz.timeloggerdesktop.backend.activity.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-04.
 */
public class DatabaseCustomDao implements CustomDao {
    private Logger logger = LoggerFactory.getLogger(DatabaseCustomDao.class);
    private static final String SQLITE_DATABASE_URL = "jdbc:sqlite:timeloggertest.db";

    private ConnectionSource connectionSource = new JdbcConnectionSource(SQLITE_DATABASE_URL);

    private Dao<Activity, Long> activityDao;
    private Dao<Record, Long> recordDao;

    public DatabaseCustomDao() throws SQLException {
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
    public void save(Activity activity) throws Exception {
            activityDao.create(activity);
    }

    @Override
    public void update(Activity activity) throws Exception {
        activityDao.update(activity);
    }

    @Override
    public List<Activity> getAllActivities() throws SQLException {
        return this.activityDao.queryForAll();
    }

    @Override
    public List<Record> getAllRecords() throws SQLException {
        return this.recordDao.queryForAll();
    }

    @Override
    public Activity findActivityById(Long id) throws Exception {
        return this.activityDao.queryForId(id);
    }

    @Override
    public void replaceAllData(List<Activity> activitiesList) throws Exception {
        if(this.activityDao.isTableExists()) {
            TableUtils.dropTable(activityDao, true);
        }
        if(this.recordDao.isTableExists()) {
            TableUtils.dropTable(recordDao, true);
        }
        TableUtils.createTable(connectionSource, Activity.class);
        TableUtils.createTable(connectionSource, Record.class);
        for (Activity activity : activitiesList) {
            this.save(activity);
            logger.info("activity = {}", activity);
            Activity databaseActivity = this.findActivityById(activity.getId());
            logger.info("databaseActivity = {}", databaseActivity);
            for (Record record : activity.getActivityRecords()) {
                databaseActivity.getActivityRecords().add(record);
            }
            this.update(databaseActivity);
        }
    }
}
