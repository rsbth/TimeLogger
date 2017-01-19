package timelogger.mprtcz.com.timelogger.task.service;

import java.util.List;

import timelogger.mprtcz.com.timelogger.task.dao.CustomDao;
import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-18.
 */

public class TaskService {

    private CustomDao customDao;

    public TaskService(CustomDao customDao) {
        this.customDao = customDao;
    }

    public void saveTask(Task task) {
        this.customDao.saveTask(task);
    }

    public boolean isNameUnique(String taskName) {
        List<Task> tasks = this.customDao.getAllTasks();
        for (Task task :
                tasks) {
            if(taskName.equals(task.getName())) {
                return false;
            }
        }
        return true;
    }
}
