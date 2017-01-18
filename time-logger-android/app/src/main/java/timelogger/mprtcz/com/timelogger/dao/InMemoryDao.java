package timelogger.mprtcz.com.timelogger.dao;

import java.util.List;
import java.util.Objects;

import timelogger.mprtcz.com.timelogger.model.Task;

/**
 * Created by Azet on 2017-01-18.
 */

public class InMemoryDao implements CustomDao {
    @Override
    public void saveTask(Task task) {
        task.setId(getLargestId() + 1);
    }

    @Override
    public List<Task> getAllTasks() {
        return Task.activities;
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
