package timelogger.mprtcz.com.timelogger.task.service;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import timelogger.mprtcz.com.timelogger.task.controllers.WebTaskController;
import timelogger.mprtcz.com.timelogger.task.dao.CustomDao;
import timelogger.mprtcz.com.timelogger.task.dao.DatabaseDao;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.validator.TaskValidator;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-18.
 */

public class TaskService {
    private static final String TAG = "TaskService";

    @Getter
    private CustomDao customDao;
    @Getter
    private TaskSyncService taskSyncService;

    public TaskService(CustomDao customDao) {
        this.customDao = customDao;
        this.taskSyncService = new TaskSyncService(new WebTaskController(), this);
    }

    public ValidationResult saveTask(Task task) throws Exception {
        LogWrapper.i(TAG, "TaskService.saveTask");
        TaskValidator taskValidator = new TaskValidator();
        ValidationResult validationResult = taskValidator.validateNewTask(task, getActiveTasks());
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
        if (this.taskSyncService != null) {
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
        LogWrapper.d("TaskService.updateTask", "update task validation result = " + result.toString());
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
            if (task.isActive()) {
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

    public Task findTaskByServerID(Long serverId) throws Exception {
        List<Task> tasks = getAllTasks();
        for (Task task :
                tasks) {
            if (Objects.equals(task.getServerId(), serverId)) {
                return task;
            }
        }
        return null;
    }

    public static TaskService getInstance(Activity activity) {
        TaskService taskService = new TaskService(new DatabaseDao(activity));
        LogWrapper.d(TAG, "getInstance() returned: " + taskService);
        return taskService;
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
