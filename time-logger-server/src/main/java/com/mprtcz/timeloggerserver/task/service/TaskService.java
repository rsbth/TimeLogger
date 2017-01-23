package com.mprtcz.timeloggerserver.task.service;

import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Service
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void saveTask(Task task) {
        this.taskRepository.save(task);
    }

    public Iterable<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return this.taskRepository.findOne(id);
    }
}
