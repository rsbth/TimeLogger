package com.mprtcz.timeloggerserver.task.controller;

import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.record.service.RecordService;
import com.mprtcz.timeloggerserver.task.model.TaskDto;
import com.mprtcz.timeloggerserver.task.service.TaskService;
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
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/{taskId}/records")
    public ResponseEntity getRecordsOfTask(@PathVariable Long taskId) {
        this.taskService.checkIfTaskWithIdExists(taskId);
        List<RecordDto> recordsByTaskId = this.recordService.getRecordsByTaskId(taskId);
        return new ResponseEntity<>(recordsByTaskId, OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity updateTask(@RequestBody TaskDto taskDto) {
        this.taskService.updateTask(taskDto);
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteTask(@PathVariable Long id) {
        this.taskService.deleteTask(id);
        return new ResponseEntity(OK);
    }
}
