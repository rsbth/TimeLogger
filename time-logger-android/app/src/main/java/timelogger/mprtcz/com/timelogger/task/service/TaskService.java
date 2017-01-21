package timelogger.mprtcz.com.timelogger.task.service;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import timelogger.mprtcz.com.timelogger.task.dao.CustomDao;
import timelogger.mprtcz.com.timelogger.task.dao.DatabaseDao;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.validator.TaskValidator;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-18.
 */

public class TaskService {

    private CustomDao customDao;

    private TaskService(CustomDao customDao) {
        this.customDao = customDao;
    }

    public ValidationResult saveTask(Task task)  throws Exception{
        System.out.println("TaskService.saveTask");
        TaskValidator taskValidator = new TaskValidator();
        ValidationResult validationResult = taskValidator.validateNewTask(task, getAllTasks());
        if (validationResult.isErrorFree()) {
            this.customDao.saveTask(task);
        }
        return validationResult;
    }

    public void removeTask(Task task) throws Exception {
        this.customDao.removeTask(task);
    }

    public Task getTaskById(Long id) throws Exception {
        return this.customDao.findTaskById(id);
    }

    public ValidationResult updateTask(Task task) throws Exception {
        TaskValidator taskValidator = new TaskValidator();
        ValidationResult result = taskValidator.validateUpdatedTask(task);
        Log.d("TaskService.updateTask", "update task validation result = " + result.toString());
        if(result.isErrorFree()) {
            this.customDao.updateTask(task);
        }
        return result;
    }

    public List<Task> getAllTasks() throws Exception {
        return customDao.getAllTasks();
    }

    public boolean isNameUnique(String taskName) throws Exception {
        List<Task> tasks = this.customDao.getAllTasks();
        for (Task task :
                tasks) {
            if(taskName.equals(task.getName())) {
                return false;
            }
        }
        return true;
    }

    public static TaskService getInstance(Activity activity) {
        return new TaskService(new DatabaseDao(activity));
    }
}
