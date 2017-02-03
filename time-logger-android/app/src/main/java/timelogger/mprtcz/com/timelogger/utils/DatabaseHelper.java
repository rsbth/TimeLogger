package timelogger.mprtcz.com.timelogger.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-21.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "timelogger1.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Task, Long> taskDao = null;
    private Dao<Record, Long> recordDao = null;
    private RuntimeExceptionDao<Task, Integer> taskRuntimeDao = null;

    public DatabaseHelper(Context context) throws SQLException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        LogWrapper.d("DatabaseHelper","DatabaseHelper.DatabaseHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            LogWrapper.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Task.class);
            TableUtils.createTable(connectionSource, Record.class);
        } catch (SQLException e) {
            LogWrapper.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            LogWrapper.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Task.class, true);
            TableUtils.dropTable(connectionSource, Record.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            LogWrapper.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }

    }

    public Dao<Task, Long> getTaskDao() throws SQLException {
        if (taskDao == null) {
            taskDao = new TaskDao(getConnectionSource(), Task.class);
        }
        return taskDao;
    }

    public Dao<Record, Long> getRecordDao() throws  SQLException {
        if(recordDao == null) {
            recordDao = new RecordDao(getConnectionSource(), Record.class);
        }
        return recordDao;
    }

    public RuntimeExceptionDao<Task, Integer> getRuntimeRxceptionDao() {
        if (taskRuntimeDao == null) {
            taskRuntimeDao = getRuntimeExceptionDao(Task.class);
        }
        return taskRuntimeDao;
    }

    @Override
    public void close() {
        super.close();
        taskDao = null;
        recordDao = null;
        taskRuntimeDao = null;
    }

    private class TaskDao extends BaseDaoImpl<Task, Long> {
        TaskDao(ConnectionSource connectionSource, Class<Task> dataClass) throws SQLException {
            super(connectionSource, dataClass);
        }
    }

    private class RecordDao extends BaseDaoImpl<Record, Long> {
        RecordDao(ConnectionSource connectionSource, Class<Record> dataClass) throws  SQLException {
            super(connectionSource, dataClass);
        }
    }
}
