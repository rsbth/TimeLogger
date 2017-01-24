package com.mprtcz.timeloggerserver.task.controller;

import com.mprtcz.timeloggerserver.task.model.TaskDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by mprtcz on 2017-01-24.
 */
@RestController
@RequestMapping("/task/")
public class TaskController {

    @RequestMapping("/all")
    public ResponseEntity getAllTasks() {
        return new ResponseEntity(OK);
    }

    @RequestMapping("/{taskName}")
    public ResponseEntity getTaskByName(@PathVariable String taskName) {
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addNewTask(@RequestBody TaskDto taskDto) {
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/{taskId}/records")
    public ResponseEntity getRecordsOfTask(@PathVariable Long taskId) {
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.PATCH)
    public ResponseEntity updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return new ResponseEntity(OK);
    }

    @RequestMapping("/{id}/delete")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        return new ResponseEntity(OK);
    }
}
