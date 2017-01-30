package timelogger.mprtcz.com.timelogger.task.service;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
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
    private TaskSyncService taskSyncService;

    private TaskService(CustomDao customDao) {
        this.customDao = customDao;
    }

    public ValidationResult saveTask(Task task) throws Exception {
        System.out.println("TaskService.saveTask");
        TaskValidator taskValidator = new TaskValidator();
        ValidationResult validationResult = taskValidator.validateNewTask(task, getAllTasks());
        if (validationResult.isErrorFree()) {
            this.customDao.saveTask(task);
            if (task.getServerId() == null && this.taskSyncService != null) {
                this.taskSyncService.postNewTaskToServer(task);
            }
        }
        return validationResult;
    }

    public void removeTask(Task task) throws Exception {
        task.setActive(false);
        if(this.taskSyncService != null) {
            this.taskSyncService.deleteTaskOnServer(task);
        }
        this.customDao.updateTask(task);
    }

    public Task getTaskById(Long id) throws Exception {
        return this.customDao.findTaskById(id);
    }

    public ValidationResult updateTask(Task task, UpdateType updateType) throws Exception {
        TaskValidator taskValidator = new TaskValidator();
        ValidationResult result = taskValidator.validateUpdatedTask(task);
        Log.d("TaskService.updateTask", "update task validation result = " + result.toString());
        if (result.isErrorFree()) {
            if (task.getServerId() == null && this.taskSyncService != null) {
                this.taskSyncService.postNewTaskToServer(task);
            } else {
                if (updateType == UpdateType.LOCAL) {
                    task.setLastModified(new Date());
                    if (this.taskSyncService != null) {
                        this.taskSyncService.patchTaskOnServer(task);
                    }
                }
            }
            this.customDao.updateTask(task);
        }
        return result;
    }

    public List<Task> getAllTasks() throws Exception {
        return customDao.getAllTasks();
    }

    public List<Task> getActiveTasks() throws Exception {
        List<Task> activeTasks = new ArrayList<>();
        for (Task task : this.customDao.getAllTasks()) {
            if(task.isActive()) {
                activeTasks.add(task);
            }
        }
        return activeTasks;
    }

    public boolean isNameUnique(String taskName) throws Exception {
        List<Task> tasks = this.getActiveTasks();
        for (Task task : tasks) {
            if (taskName.equals(task.getName())) {
                return false;
            }
        }
        return true;
    }

    public static TaskService getInstance(Activity activity) {
        return new TaskService(new DatabaseDao(activity));
    }

    public void removeTaskLocally(Task task) throws Exception {
        task.setActive(false);
        this.customDao.updateTask(task);
    }

    public enum UpdateType {
        SERVER,
        LOCAL,
        RECORD_UPDATE
    }
}
