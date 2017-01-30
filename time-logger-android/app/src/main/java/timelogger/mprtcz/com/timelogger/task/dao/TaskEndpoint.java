package timelogger.mprtcz.com.timelogger.task.dao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import timelogger.mprtcz.com.timelogger.task.model.TaskDto;

/**
 * Created by Azet on 2017-01-30.
 */

public interface TaskEndpoint {

    @POST("/task/add")
    Call<TaskDto> postNewTask(@Body TaskDto taskDto);

    @GET("/task/all")
    Call<List<TaskDto>> getAllTasks();

    @PATCH("/task/update")
    Call<TaskDto> patchTaskOnServer(@Body TaskDto taskDto);

    @DELETE("/task/{id}/delete")
    Call<Void> deleteTaskOnServer(@Path("id") Long serverId);
}
