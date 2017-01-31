package timelogger.mprtcz.com.timelogger.task.dao;

import java.util.List;

import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-18.
 */

public interface CustomDao {

    void saveTask(Task task) throws Exception;

    List<Task> getAllTasks() throws Exception;

    Task findTaskById(Long id) throws Exception;

    List<Record> getAllRecords() throws Exception;

    void update(Record record) throws Exception;

    void removeTask(Task task) throws Exception;

    void updateTask(Task newTask) throws Exception;
}
