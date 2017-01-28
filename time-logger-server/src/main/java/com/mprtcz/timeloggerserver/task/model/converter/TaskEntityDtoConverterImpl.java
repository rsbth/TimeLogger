package com.mprtcz.timeloggerserver.task.model.converter;

import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.model.TaskDto;
import com.mprtcz.timeloggerserver.utils.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Component
public class TaskEntityDtoConverterImpl implements TaskEntityDtoConverter {
    private static final Logger logger = LoggerFactory.getLogger(TaskEntityDtoConverterImpl.class);

    @Override
    public Task toEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setColor(taskDto.getColor());
        task.setLastModified(DateTimeConverter.toLocalDateTime(taskDto.getLastModified()));
        task.setActive(taskDto.isActive());
        return task;
    }

    @Override
    public TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(task.getName());
        taskDto.setColor(task.getColor());
        taskDto.setDescription(task.getDescription());
        taskDto.setUuID(task.getUuID());
        taskDto.setLastModified(DateTimeConverter.toDate(task.getLastModified()));
        logger.info("taskDto.getLastModified() = " + taskDto.getLastModified());
        taskDto.setActive(task.isActive());
        return taskDto;
    }

    @Override
    public List<Task> toEntities(List<TaskDto> dtos) {
        List<Task> tasks = new ArrayList<>();
        for (TaskDto taskDto :
                dtos) {
            tasks.add(toEntity(taskDto));
        }
        return tasks;

    }

    @Override
    public Iterable<TaskDto> toDtos(Iterable<Task> entities) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task :
                entities) {
            taskDtos.add(toDto(task));
        }
        return taskDtos;
    }
}
