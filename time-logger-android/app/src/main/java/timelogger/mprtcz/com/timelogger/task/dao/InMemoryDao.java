package timelogger.mprtcz.com.timelogger.task.dao;

import java.util.List;
import java.util.Objects;

import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-18.
 */

public class InMemoryDao implements CustomDao {
    @Override
    public void saveTask(Task task) {
        task.setId(getLargestId() + 1);
        Task.tasks.add(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return Task.tasks;
    }

    @Override
    public Task findTaskById(Long id) {
        for (Task task :
                getAllTasks()){
            if (Objects.equals(task.getId(), id)) {
                return task;
            }
        }
        return null;
    }

    @Override
    public void removeTask(Task task) {
        Task.tasks.remove(task);
    }

    private Long getLargestId() {
        Long biggest = -1L;
        for (Task task :
                getAllTasks()){
            if (task.getId() > biggest) {
                biggest = task.getId();
            }
        }
        if(biggest == -1L) {
            return 0L;
        } else {
            return biggest;
        }
    }
}
