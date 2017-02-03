package timelogger.mprtcz.com.timelogger.task.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import timelogger.mprtcz.com.timelogger.activities.SyncingActivity;
import timelogger.mprtcz.com.timelogger.interfaces.UiUpdater;
import timelogger.mprtcz.com.timelogger.task.controllers.WebTaskController;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.model.TaskDto;
import timelogger.mprtcz.com.timelogger.task.model.converter.TaskConverter;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;
import timelogger.mprtcz.com.timelogger.utils.web.CustomWebCallback;

/**
 * Created by mprtcz on 2017-01-30.
 */
public class TaskSyncService {
    private static final String TAG = "TaskSyncService";
    public static final String TASK_EXISTS_RESPONSE_BODY = "Task with this name already exists";

    private WebTaskController webTaskController;
    private TaskService taskService;
    private UiUpdater uiUpdater;

    public TaskSyncService(WebTaskController webTaskController,
                           TaskService taskService) {
        this.webTaskController = webTaskController;
        this.taskService = taskService;
    }

    public void synchronizeTasks(UiUpdater uiUpdater) throws Exception {
        this.uiUpdater = uiUpdater;
        LogWrapper.d(TAG, "synchronizeTasks background thread: " + Thread.currentThread().toString());
        List<Task> localTasks = this.taskService.getAllTasks();
        List<TaskDto> serverTasks = this.webTaskController.getTasksFromServer();
        this.synchronizeLocalTasksWithServer(localTasks, serverTasks);
    }

    private Callback<List<TaskDto>> getTaskSynchronizationCallback(final List<Task> localTasks) {
        return new CustomWebCallback<List<TaskDto>>() {
            @Override
            public void onSuccessfulCall(Response<List<TaskDto>> response) throws Exception {
                synchronizeLocalTasksWithServer(localTasks, response.body());
            }
        };
    }

    private void synchronizeLocalTasksWithServer(List<Task> localTasks,
                                                 List<TaskDto> serverTasks) throws Exception {
        LogWrapper.i(TAG, "TaskService.synchronizeLocalTasksWithServer, server tasks = "
                + serverTasks);
        Map<Long, Task> localTasksMap = getLocalTasksMap(localTasks);
        if (serverTasks == null) {
            return;
        }
        List<Task> tasksToSendToServer = new ArrayList<>();
        int index = 1;
        int serverTasksSize = serverTasks.size();
        for (TaskDto serverTask : serverTasks) {
            this.uiUpdater.updateTextView(SyncingActivity.SyncType.RECORD,
                    String.valueOf((index * 100) / serverTasksSize) + "%");
            Task localTask = localTasksMap.get(serverTask.getServerId());
            LogWrapper.i(TAG, "server Task  = " + serverTask.toString());
            LogWrapper.i(TAG, "local matching Task  = " + localTask);
            if (serverTask.isActive()) {
                LogWrapper.i(TAG, "server task is active");
                if (localTask != null) {
                    LogWrapper.i(TAG, "task exists in local db");
                    if (localTask.isActive()) {
                        LogWrapper.i(TAG, "task is active in local db");
                        if (localTask.getLastModified().before(serverTask.getLastModified())) {
                            logModificationDates("task has been modified on server:\n",
                                    serverTask, localTask);
                            try {
                                this.updateTaskWithServerData(localTask, serverTask);
                            } catch (Exception e) {
                                LogWrapper.e(TAG, "Error while updating task with server data = " + e.toString());
                                e.printStackTrace();
                            }
                        } else if (localTask.getLastModified().after(serverTask.getLastModified())) {
                            logModificationDates("task has been modified locally:\n",
                                    serverTask, localTask);
                            tasksToSendToServer.add(localTask);
                        } else {
                            logModificationDates(
                                    "task has the same modification date locally and on server:\n",
                                    serverTask, localTask);
                        }
                    } else {
                        LogWrapper.i(TAG, "Task is inactive in local db, task = " + localTask);
                        tasksToSendToServer.add(localTask);
                    }
                } else {
                    try {
                        this.taskService.saveTask(
                                TaskConverter.toEntity(serverTask));
                    } catch (Exception e) {
                        LogWrapper.e(TAG, "Error while saving server task = " + e.toString());
                        e.printStackTrace();
                    }
                }
            } else {
                LogWrapper.i(TAG, "server task is removed from server");
                if (localTask != null && localTask.isActive()) {
                    try {
                        this.taskService.removeTaskLocally(localTask);
                    } catch (Exception e) {
                        LogWrapper.e(TAG, "Error while removing server task = " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
            index++;
        }

        for (Task task : localTasks) {
            if (task.getServerId() == null) {
                tasksToSendToServer.add(task);
            }
        }
        LogWrapper.i(TAG, "tasksToSendToServer = " + tasksToSendToServer);
        sendLocalChangesToServer(tasksToSendToServer);
        LogWrapper.i(TAG, "synchronizeLocalTasksWithServer() finishing");
    }


    private Map<Long, Task> getLocalTasksMap(List<Task> tasks) {
        Map<Long, Task> map = new HashMap<>();
        for (Task t : tasks) {
            if (t.getServerId() != null) {
                map.put(t.getServerId(), t);
            }
        }
        return map;
    }

    private ValidationResult updateTaskWithServerData(Task task, TaskDto taskDto)
            throws Exception {
        LogWrapper.d(TAG, "updateTaskWithServerData() called with: task = [" + task
                + "], taskDto = [" + taskDto + "]");
        task.setName(taskDto.getName());
        task.setLastModified(taskDto.getLastModified());
        task.setColor(taskDto.getColor());
        task.setDescription(taskDto.getDescription());
        task.setServerId(taskDto.getServerId());
        return this.taskService.updateTask(task, TaskService.UpdateType.SERVER);
    }

    private void sendLocalChangesToServer(List<Task> tasksToSendToServer) throws Exception {
        for (Task task :
                tasksToSendToServer) {
            if (!task.isActive() && task.getServerId() != null) {
                this.webTaskController.deleteTaskOnServer(task);
                continue;
            }
            if (task.getServerId() == null) {
                Response<TaskDto> response = this.webTaskController.postNewTaskToServer(task);
                LogWrapper.i(TAG, "sendLocalChangesToServer: response when posting new task to server: "
                        + response.toString());
                if(!response.isSuccessful()) {
                    if(response.errorBody().string().equals(TASK_EXISTS_RESPONSE_BODY)) {
                        TaskDto taskDto = this.webTaskController.getTaskFromServerByName(task.getName());
                        task.setServerId(taskDto.getServerId());
                        updateTaskWithServerData(task, taskDto);
                    }
                }

            } else {
                this.webTaskController.patchTaskOnServer(task);
            }
        }
    }

    private Callback<Void> getDeleteCallback(final Task task) {
        return new CustomWebCallback<Void>() {
            @Override
            public void onSuccessfulCall(Response<Void> response) {
                LogWrapper.i(TAG, "Deletion successful, task = " + task);
            }
        };
    }

    private Callback<TaskDto> getPostNewTaskCallback(final Task task) {
        return new CustomWebCallback<TaskDto>() {
            @Override
            public void onSuccessfulCall(Response<TaskDto> response) {
                TaskDto taskDto = response.body();
                try {
                    updateTaskWithServerData(task, taskDto);
                } catch (Exception e) {
                    LogWrapper.w(TAG, "Update unsuccessful, exception = " + e.toString() +
                            " response code = " + response.code());
                    e.printStackTrace();
                }
            }
        };
    }

    private Callback<TaskDto> getPatchTaskCallback() {
        return new CustomWebCallback<TaskDto>() {
            @Override
            public void onSuccessfulCall(Response<TaskDto> response) {
                LogWrapper.i(TAG, "Patching successful");
            }
        };
    }

    public void postNewTaskToServer(Task task) {
        this.webTaskController.postNewTaskToServerAsync(
                getPostNewTaskCallback(task), task);
    }

    public void patchTaskOnServer(Task task) {
        this.webTaskController.patchTaskOnServerAsync(
                getPatchTaskCallback(), task);
    }

    public void deleteTaskOnServer(Task task) {
        this.webTaskController.deleteTaskOnServerAsync(
                getDeleteCallback(task), task);
    }

    private void logModificationDates(String message, TaskDto serverTask, Task localTask) {
        LogWrapper.i(TAG, message + "task.getLastModified() = "
                + localTask.getLastModified() + " serverTask.getLastModified() = "
                + serverTask.getLastModified());
    }
}
