package com.mprtcz.timeloggerserver.task.service;

import com.mprtcz.timeloggerserver.record.repository.CustomRecordRepository;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.model.TaskDto;
import com.mprtcz.timeloggerserver.task.model.converter.TaskEntityDtoConverter;
import com.mprtcz.timeloggerserver.task.repository.TaskRepository;
import com.mprtcz.timeloggerserver.task.validator.TaskValidator;
import com.mprtcz.timeloggerserver.utils.exceptions.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mprtcz.timeloggerserver.utils.DateTimeConverter.toLocalDateTime;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

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
        TaskValidator.validateNewTask(task, this.getAllActiveTasks());
        task.setLastModified(toLocalDateTime(taskDto.getLastModified()));
        task.setActive(true);
        this.taskRepository.save(task);
    }

    public Iterable<Task> getAllTasks() {
        Iterable<Task> allTasks = this.taskRepository.findAll();
        logger.info("All tasks = " + allTasks);
        System.out.println("allTasks = " + allTasks);
        return allTasks;
    }

    public Iterable<Task> getAllActiveTasks() {
        List<Task> allActiveTasks = new ArrayList<>();
        Iterable<Task> tasks = getAllTasks();
        for (Task task :
                tasks) {
            if (task.isActive()) {
                allActiveTasks.add(task);
            }
        }
        return allActiveTasks;
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
        throw new TaskNotFoundException("Task with this name not found");
    }

    public TaskDto getTaskDtoById(Long id) {
        checkIfTaskWithIdExists(id);
        TaskDto taskDto = this.taskEntityDtoConverter.toDto(this.taskRepository.findOne(id));
        return taskDto;
    }

    public Task getTaskById(Long id) {
        checkIfTaskWithIdExists(id);
        return this.taskRepository.findOne(id);
    }

    public void deleteTask(Long id) {
        checkIfTaskWithIdExists(id);
        Task task = getTaskById(id);
        task.setLastModified(LocalDateTime.now());
        task.setActive(false);
        this.taskRepository.save(task);
    }

    public void checkIfTaskWithIdExists(Long id) {
        if (!this.taskRepository.exists(id)) {
            throw new TaskNotFoundException("Task with this id does not exist");
        }
    }

    private void taskNullCheck(Task task) {
        if (task == null) {
            throw new TaskNotFoundException("Task with this id does not exist");
        }
    }

    public void updateTask(TaskDto taskDto) {
        Task task = this.taskRepository.findOne(taskDto.getId());
        Task convertedDto = this.taskEntityDtoConverter.toEntity(taskDto);
        task.setName(convertedDto.getName());
        task.setColor(convertedDto.getColor());
        task.setDescription(convertedDto.getDescription());
        task.setLastModified(LocalDateTime.now());
        this.taskRepository.save(task);
    }
}
