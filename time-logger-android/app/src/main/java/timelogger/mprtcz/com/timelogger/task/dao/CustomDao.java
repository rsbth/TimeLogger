package timelogger.mprtcz.com.timelogger.task.dao;

import java.util.List;

import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-18.
 */

public interface CustomDao {

    void saveTask(Task task);

    List<Task> getAllTasks();

    Task findTaskById(Long id);

    void removeTask(Task task);

    void updateTask(Task newTask);
}
