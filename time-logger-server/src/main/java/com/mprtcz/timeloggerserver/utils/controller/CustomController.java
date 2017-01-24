package com.mprtcz.timeloggerserver.utils.controller;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.service.RecordService;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by mprtcz on 2017-01-23.
 */
@RestController
public class CustomController {

    @Autowired
    TaskService taskService;
    @Autowired
    RecordService recordService;

    @RequestMapping("/hello")
    public ResponseEntity getDefaultMessage() {
        return new ResponseEntity<>("Hello world!", HttpStatus.OK);
    }

    @RequestMapping("/savetask")
    public ResponseEntity saveDummyTask() {
        taskService.saveTask(new Task("name1", "somecolor"));
        return new ResponseEntity<>("saved", HttpStatus.OK);
    }

    @RequestMapping("/saverecord")
    public ResponseEntity saveDummyRecord() {
        Task task = taskService.getTaskById(1L);
        Record r = new Record();
        r.setTask(task);
        r.setStartDateTime(LocalDateTime.now());
        r.setEndDateTime(LocalDateTime.now());
        task.getActivityRecords().add(r);
        recordService.saveRecord(r);
        return new ResponseEntity<>("saved", HttpStatus.OK);
    }
}
