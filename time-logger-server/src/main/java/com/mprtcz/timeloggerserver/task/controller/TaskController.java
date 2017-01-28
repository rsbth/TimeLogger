package com.mprtcz.timeloggerserver.task.controller;

import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.record.service.RecordService;
import com.mprtcz.timeloggerserver.task.model.TaskDto;
import com.mprtcz.timeloggerserver.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by mprtcz on 2017-01-24.
 */
@RestController
@RequestMapping("/task/")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final
    TaskService taskService;

    private final
    RecordService recordService;

    @Autowired
    public TaskController(TaskService taskService, RecordService recordService) {
        this.taskService = taskService;
        this.recordService = recordService;
    }

    @RequestMapping("/all")
    public ResponseEntity getAllTasks() {
        Iterable<TaskDto> tasks = this.taskService.getAllTaskDtos();
        return new ResponseEntity<>(tasks, OK);
    }

    @RequestMapping("/{taskName}")
    public ResponseEntity getTaskByName(@PathVariable String taskName) {
        TaskDto taskDto = this.taskService.getTaskDtoByName(taskName);
        return new ResponseEntity<>(taskDto, OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addNewTask(@RequestBody TaskDto taskDto) {
        this.taskService.saveTask(taskDto);
        TaskDto savedTaskDto = this.taskService.getTaskDtoByName(taskDto.getName());
        return new ResponseEntity<>(savedTaskDto, OK);
    }

    @RequestMapping(value = "/{taskUuId}/records")
    public ResponseEntity getRecordsOfTask(@PathVariable String taskUuId) {
        this.taskService.checkIfTaskWithUuIdExists(taskUuId);
        List<RecordDto> recordsByTaskId = this.recordService.getRecordsByTaskUuId(taskUuId);
        return new ResponseEntity<>(recordsByTaskId, OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity updateTask(@RequestBody TaskDto taskDto) {
        logger.info("Task to update = " + taskDto);
        this.taskService.updateTask(taskDto);
        TaskDto updatedTask = this.taskService.getTaskDtoByUuid(taskDto.getUuID());
        return new ResponseEntity<>(updatedTask, OK);
    }

    @RequestMapping(value = "/{uuId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteTask(@PathVariable String uuId) {
        logger.info("Task ID to delete = " + uuId);
        this.taskService.deleteTask(uuId);
        return new ResponseEntity<>(OK);
    }
}
