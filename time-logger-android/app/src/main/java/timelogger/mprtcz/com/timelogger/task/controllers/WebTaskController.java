package timelogger.mprtcz.com.timelogger.task.controllers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import timelogger.mprtcz.com.timelogger.task.dao.TaskEndpoint;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.model.TaskDto;
import timelogger.mprtcz.com.timelogger.utils.web.RetrofitUtil;

import static timelogger.mprtcz.com.timelogger.task.model.converter.TaskConverter.toDto;

/**
 * Created by Azet on 2017-01-30.
 */

public class WebTaskController {

    public void getActivitiesFromServer(Callback<List<TaskDto>> callback) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<List<TaskDto>> activitiesCall = taskEndpoint.getAllTasks();
        activitiesCall.enqueue(callback);
    }

    public void postNewTaskToServer(Callback<TaskDto> callback, Task activity) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<TaskDto> postNewTaskCall = taskEndpoint.postNewTask(toDto(activity));
        postNewTaskCall.enqueue(callback);
    }

    public void patchTaskOnServer(Callback<TaskDto> activityDtoCallback, Task activity) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<TaskDto> postNewTaskCall = taskEndpoint.patchTaskOnServer(toDto(activity));
        postNewTaskCall.enqueue(activityDtoCallback);
    }

    public void deleteTaskOnServer(Callback<Void> activityDtoCallback, Task activity) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<Void> postNewTaskCall = taskEndpoint.deleteTaskOnServer(activity.getServerId());
        postNewTaskCall.enqueue(activityDtoCallback);
    }
}
