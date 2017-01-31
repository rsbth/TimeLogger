package timelogger.mprtcz.com.timelogger.task.dao;

import android.app.Activity;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.utils.DatabaseHelper;
import timelogger.mprtcz.com.timelogger.utils.OrmBaseClass;

/**
 * Created by Azet on 2017-01-21.
 */

public class DatabaseDao implements CustomDao {
    public static final String TAG = "DatabaseDao";

    private Activity rootActivity;
    private DatabaseHelper databaseHelper;

    public DatabaseDao(Activity rootActivity) {
        Log.d(TAG, ", new");
        this.rootActivity = rootActivity;
        databaseHelper = OrmBaseClass.getInstance().getHelper(rootActivity);
    }

    @Override
    public void saveTask(Task task) throws SQLException {
        Log.d(TAG, "Saving task in db: " + task.toString());
        this.databaseHelper.getTaskDao().create(task);
    }

    @Override
    public List<Task> getAllTasks() throws SQLException {
        return this.databaseHelper.getTaskDao().queryForAll();
    }

    @Override
    public Task findTaskById(Long id) throws SQLException {
        return this.databaseHelper.getTaskDao().queryForId(id);
    }

    @Override
    public List<Record> getAllRecords() throws Exception {
        return this.databaseHelper.getRecordDao().queryForAll();
    }

    @Override
    public void update(Record record) throws Exception {
        Log.d(TAG, "update() called with: record = [" + record + "]");
        this.databaseHelper.getRecordDao().update(record);
    }

    @Override
    public void removeTask(Task task) throws SQLException {
        this.databaseHelper.getTaskDao().delete(task);
    }

    @Override
    public void updateTask(Task newTask) throws SQLException {
        this.databaseHelper.getTaskDao().update(newTask);
    }
}
