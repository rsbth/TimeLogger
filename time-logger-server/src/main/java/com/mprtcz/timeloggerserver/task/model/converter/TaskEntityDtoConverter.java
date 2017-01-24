package com.mprtcz.timeloggerserver.task.model.converter;

import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.model.TaskDto;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
public interface TaskEntityDtoConverter {

    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);

    List<Task> toEntities(List<TaskDto> dtos);

    List<TaskDto> toDtos(List<Task> entities);
}
