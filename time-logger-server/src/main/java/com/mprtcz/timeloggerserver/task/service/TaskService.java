package com.mprtcz.timeloggerserver.task.service;

import com.mprtcz.timeloggerserver.record.repository.CustomRecordRepository;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.model.TaskDto;
import com.mprtcz.timeloggerserver.task.model.converter.TaskEntityDtoConverter;
import com.mprtcz.timeloggerserver.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Service
public class TaskService {

    private TaskRepository taskRepository;
    private final TaskEntityDtoConverter taskEntityDtoConverter;
    private final
    CustomRecordRepository customRecordRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       TaskEntityDtoConverter taskEntityDtoConverter, CustomRecordRepository customRecordRepository) {
        this.taskRepository = taskRepository;
        this.taskEntityDtoConverter = taskEntityDtoConverter;
        this.customRecordRepository = customRecordRepository;
    }

    public void saveTask(TaskDto taskDto) {
        Task task = this.taskEntityDtoConverter.toEntity(taskDto);
        this.taskRepository.save(task);
    }

    public Iterable<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    public Iterable<TaskDto> getAllTaskDtos() {
        return this.taskEntityDtoConverter.toDtos(this.taskRepository.findAll());
    }

    public TaskDto getTaskDtoByName(String name) {
        for (Task t :
                getAllTasks()) {
            if (t.getName().equals(name)) {
                return this.taskEntityDtoConverter.toDto(t);
            }
        }
        return null;
    }

    public TaskDto getTaskDtoById(Long id) {
        TaskDto taskDto = this.taskEntityDtoConverter.toDto(this.taskRepository.findOne(id));
        return taskDto;
    }

    public Task getTaskById(Long id) {
        return this.taskRepository.findOne(id);
    }

    public void deleteTask(Long id) {
        this.taskRepository.delete(id);
    }

    public void updateTask(TaskDto taskDto) {
        Task task = this.taskRepository.findOne(taskDto.getId());
        Task convertedDto = this.taskEntityDtoConverter.toEntity(taskDto);
        task.setName(convertedDto.getName());
        task.setColor(convertedDto.getColor());
        task.setDescription(convertedDto.getDescription());
        this.taskRepository.save(task);
    }
}
