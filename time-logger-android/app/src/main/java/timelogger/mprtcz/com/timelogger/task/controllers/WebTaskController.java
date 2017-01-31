package timelogger.mprtcz.com.timelogger.task.controllers;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timelogger.mprtcz.com.timelogger.task.dao.TaskEndpoint;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.model.TaskDto;
import timelogger.mprtcz.com.timelogger.utils.web.RetrofitUtil;

import static timelogger.mprtcz.com.timelogger.task.model.converter.TaskConverter.toDto;

/**
 * Created by Azet on 2017-01-30.
 */

public class WebTaskController {

    public void getTasksFromServerAsync(Callback<List<TaskDto>> callback) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<List<TaskDto>> activitiesCall = taskEndpoint.getAllTasks();
        activitiesCall.enqueue(callback);
    }

    public List<TaskDto> getTasksFromServer() throws IOException {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<List<TaskDto>> activitiesCall = taskEndpoint.getAllTasks();
        return activitiesCall.execute().body();
    }

    public void postNewTaskToServerAsync(Callback<TaskDto> callback, Task task) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<TaskDto> postNewTaskCall = taskEndpoint.postNewTask(toDto(task));
        postNewTaskCall.enqueue(callback);
    }

    public Response<TaskDto> postNewTaskToServer(Task task) throws IOException {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<TaskDto> postNewTaskCall = taskEndpoint.postNewTask(toDto(task));
        return postNewTaskCall.execute();
    }

    public void patchTaskOnServerAsync(Callback<TaskDto> taskDtoCallback, Task task) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<TaskDto> postNewTaskCall = taskEndpoint.patchTaskOnServer(toDto(task));
        postNewTaskCall.enqueue(taskDtoCallback);
    }

    public TaskDto patchTaskOnServer(Task task) throws IOException {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<TaskDto> postNewTaskCall = taskEndpoint.patchTaskOnServer(toDto(task));
        return postNewTaskCall.execute().body();
    }

    public void deleteTaskOnServerAsync(Callback<Void> taskDtoCallback, Task task) {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<Void> postNewTaskCall = taskEndpoint.deleteTaskOnServer(task.getServerId());
        postNewTaskCall.enqueue(taskDtoCallback);
    }

    public void deleteTaskOnServer(Task task) throws IOException {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<Void> postNewTaskCall = taskEndpoint.deleteTaskOnServer(task.getServerId());
        postNewTaskCall.execute().body();
    }

    public TaskDto getTaskFromServerByName(String name) throws Exception {
        TaskEndpoint taskEndpoint = RetrofitUtil.getTaskEndpointRetrofit();
        Call<TaskDto> postNewTaskCall = taskEndpoint.getTaskByName(name);
        return postNewTaskCall.execute().body();
    }
}
