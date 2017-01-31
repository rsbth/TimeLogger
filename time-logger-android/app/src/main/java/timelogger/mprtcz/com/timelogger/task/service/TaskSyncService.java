package timelogger.mprtcz.com.timelogger.task.service;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import timelogger.mprtcz.com.timelogger.interfaces.Synchrotron;
import timelogger.mprtcz.com.timelogger.task.controllers.WebTaskController;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.model.TaskDto;
import timelogger.mprtcz.com.timelogger.task.model.converter.TaskConverter;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;
import timelogger.mprtcz.com.timelogger.utils.web.CustomWebCallback;

/**
 * Created by mprtcz on 2017-01-30.
 */
public class TaskSyncService {
    private static final String TAG = "TaskSyncService";

    private WebTaskController webTaskController;
    private TaskService taskService;
    private Synchrotron synchrotron;

    public TaskSyncService(WebTaskController webTaskController,
                           TaskService taskService) {
        this.webTaskController = webTaskController;
        this.taskService = taskService;
    }

    public void synchronizeActivities(Synchrotron synchrotron) throws Exception {
        this.synchrotron = synchrotron;
        List<Task> localActivities = this.taskService.getAllTasks();
        this.webTaskController.getActivitiesFromServer(getTaskSynchronizationCallback(localActivities));
    }

    private Callback<List<TaskDto>> getTaskSynchronizationCallback(final List<Task> localTasks) {
        return new CustomWebCallback<List<TaskDto>>(this.synchrotron) {
            @Override
            public void onSuccessfulCall(Response<List<TaskDto>> response) {
                synchronizeLocalActivitiesWithServer(localTasks, response.body());
            }
        };
    }

    private void synchronizeLocalActivitiesWithServer(List<Task> localTasks,
                                                      List<TaskDto> serverTasks) {
        Log.i(TAG, "TaskService.synchronizeLocalActivitiesWithServer, server activities = "
                + serverTasks);
        Map<Long, Task> localTasksMap = getLocalActivitiesMap(localTasks);
        if (serverTasks == null) {
            return;
        }
        List<Task> tasksToSendToServer = new ArrayList<>();
        for (TaskDto serverTask : serverTasks) {
            Task localTask = localTasksMap.get(serverTask.getServerId());
            Log.i(TAG, "server Task  = " + serverTask.toString());
            Log.i(TAG, "local matching Task  = " + localTask);
            if (serverTask.isActive()) {
                Log.i(TAG, "server task is active");
                if (localTask != null) {
                    Log.i(TAG, "task exists in local db");
                    if (localTask.isActive()) {
                        Log.i(TAG, "task is active in local db");
                        if (localTask.getLastModified().before(serverTask.getLastModified())) {
                            logModificationDates("task has been modified on server:\n",
                                    serverTask, localTask);
                            try {
                                this.updateTaskWithServerData(localTask, serverTask);
                            } catch (Exception e) {
                                Log.e(TAG, "Error while updating task with server data = " + e.toString());
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
                        Log.i(TAG, "Task is inactive in local db, task = " + localTask);
                        tasksToSendToServer.add(localTask);
                    }
                } else {
                    try {
                        this.taskService.saveTask(
                                TaskConverter.toEntity(serverTask));
                    } catch (Exception e) {
                        Log.e(TAG, "Error while saving server task = " + e.toString());
                        e.printStackTrace();
                    }
                }
            } else {
                Log.i(TAG, "server task is removed from server");
                if (localTask != null && localTask.isActive()) {
                    try {
                        this.taskService.removeTaskLocally(localTask);
                    } catch (Exception e) {
                        Log.e(TAG, "Error while removing server task = " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }

        for (Task task : localTasks) {
            if (task.getServerId() == null) {
                tasksToSendToServer.add(task);
            }
        }
        sendLocalChangesToServer(tasksToSendToServer);
        Log.i(TAG, "activitiesToSendToServer = " + tasksToSendToServer);
        this.synchrotron.synchronizeRecords();
    }


    private Map<Long, Task> getLocalActivitiesMap(List<Task> tasks) {
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
        Log.d(TAG, "updateTaskWithServerData() called with: task = [" + task + "], taskDto = [" + taskDto + "]");
        task.setName(taskDto.getName());
        task.setLastModified(taskDto.getLastModified());
        task.setColor(taskDto.getColor());
        task.setDescription(taskDto.getDescription());
        task.setServerId(taskDto.getServerId());
        return this.taskService.updateTask(task, TaskService.UpdateType.SERVER);
    }

    private void sendLocalChangesToServer(List<Task> activitiesToSendToServer) {
        for (Task task :
                activitiesToSendToServer) {
            if (!task.isActive() && task.getServerId() != null) {
                this.webTaskController.deleteTaskOnServer(getDeleteCallback(task), task);
                continue;
            }
            if (task.getServerId() == null) {
                this.webTaskController.postNewTaskToServer(
                        getPostNewTaskCallback(task), task);
            } else {
                this.webTaskController.patchTaskOnServer(getPatchTaskCallback(), task);
            }
        }
    }

    private Callback<Void> getDeleteCallback(final Task task) {
        return new CustomWebCallback<Void>(this.synchrotron) {
            @Override
            public void onSuccessfulCall(Response<Void> response) {
                Log.i(TAG, "Deletion successful, task = " + task);
            }
        };
    }

    private Callback<TaskDto> getPostNewTaskCallback(final Task task) {
        return new CustomWebCallback<TaskDto>(this.synchrotron) {
            @Override
            public void onSuccessfulCall(Response<TaskDto> response) {
                TaskDto taskDto = response.body();
                try {
                    updateTaskWithServerData(task, taskDto);
                } catch (Exception e) {
                    Log.w(TAG, "Update unsuccessful, exception = " + e.toString() +
                            " response code = " + response.code());
                    e.printStackTrace();
                }
            }
        };
    }

    private Callback<TaskDto> getPatchTaskCallback() {
        return new CustomWebCallback<TaskDto>(this.synchrotron) {
            @Override
            public void onSuccessfulCall(Response<TaskDto> response) {
                Log.i(TAG, "Patching successful");
            }
        };
    }

    public void postNewTaskToServer(Task task) {
        this.webTaskController.postNewTaskToServer(
                getPostNewTaskCallback(task), task);
    }

    public void patchTaskOnServer(Task task) {
        this.webTaskController.patchTaskOnServer(
                getPatchTaskCallback(), task);
    }

    public void deleteTaskOnServer(Task task) {
        this.webTaskController.deleteTaskOnServer(
                getDeleteCallback(task), task);
    }

    private void logModificationDates(String message, TaskDto serverTask, Task localTask) {
        Log.i(TAG, message + "task.getLastModified() = "
                + localTask.getLastModified() + " serverTask.getLastModified() = "
                + serverTask.getLastModified());
    }
}
