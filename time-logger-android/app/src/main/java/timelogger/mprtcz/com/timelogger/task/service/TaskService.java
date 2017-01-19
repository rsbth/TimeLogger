package timelogger.mprtcz.com.timelogger.task.service;

import java.util.List;

import timelogger.mprtcz.com.timelogger.task.dao.CustomDao;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.validator.TaskValidator;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-18.
 */

public class TaskService {

    private CustomDao customDao;

    public TaskService(CustomDao customDao) {
        this.customDao = customDao;
    }

    public ValidationResult saveTask(Task task) {
        System.out.println("TaskService.saveTask");
        TaskValidator taskValidator = new TaskValidator();
        ValidationResult validationResult = taskValidator.validateNewTask(task, getAllTasks());
        if (validationResult.isErrorFree()) {
            this.customDao.saveTask(task);
        }
        return validationResult;
    }

    public void removeTask(Task task) {
        this.customDao.removeTask(task);
    }

    public List<Task> getAllTasks() {
        return customDao.getAllTasks();
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
