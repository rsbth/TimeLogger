package timelogger.mprtcz.com.timelogger.task.dao;

import java.util.List;
import java.util.Objects;

import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;

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
        LogWrapper.d("InMemorydao", "find by id =" +id);
        for (Task task :
                getAllTasks()){
            if (Objects.equals(task.getId(), id)) {
                LogWrapper.d("InMemorydao", "returning task = " +task.toString());
                return task;
            }
        }
        return null;
    }

    @Override
    public List<Record> getAllRecords() throws Exception {
        return null;
    }

    @Override
    public void update(Record record) throws Exception {}

    @Override
    public void removeTask(Task task) {
        Task.tasks.remove(task);
    }

    @Override
    public void updateTask(Task newTask) {
        Task task = findTaskById(newTask.getId());
        int index = Task.tasks.indexOf(task);
        task.setName(newTask.getName());
        task.setDescription(newTask.getDescription());
        task.setColor(newTask.getColor());
        Task.tasks.set(index, task);
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
