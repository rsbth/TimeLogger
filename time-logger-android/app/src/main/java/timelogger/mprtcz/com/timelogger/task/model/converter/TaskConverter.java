package timelogger.mprtcz.com.timelogger.task.model.converter;

import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.model.TaskDto;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class TaskConverter {

    public static Task toEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setColor(taskDto.getColor());
        task.setDescription(taskDto.getDescription());
        task.setLastModified(taskDto.getLastModified());
        task.setServerId(taskDto.getServerId());
        task.setActive(taskDto.isActive());
        return task;
    }

    public static TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(task.getName());
        taskDto.setColor(task.getColor());
        taskDto.setDescription(task.getDescription());
        taskDto.setLastModified(task.getLastModified());
        taskDto.setServerId(task.getServerId());
        taskDto.setActive(task.isActive());
        return taskDto;
    }
}
