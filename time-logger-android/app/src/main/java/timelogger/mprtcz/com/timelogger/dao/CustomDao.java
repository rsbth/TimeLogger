package timelogger.mprtcz.com.timelogger.dao;

import java.util.List;

import timelogger.mprtcz.com.timelogger.model.Task;

/**
 * Created by Azet on 2017-01-18.
 */

public interface CustomDao {

    public void saveTask(Task task);

    public List<Task> getAllTasks();

    public Task findTaskById(Long id);
}
