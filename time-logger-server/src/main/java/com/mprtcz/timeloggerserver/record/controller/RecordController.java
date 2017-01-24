package com.mprtcz.timeloggerserver.record.controller;

import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.record.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by mprtcz on 2017-01-24.
 */
@RestController
@RequestMapping("/record/")
public class RecordController {
    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    private final
    RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @RequestMapping("/all")
    public ResponseEntity getAllRecords() {
        Iterable<RecordDto> allRecords = this.recordService.getAllRecords();
        return new ResponseEntity<>(allRecords, OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addRecords(@RequestBody RecordDto recordDto) {
        logger.info("Record to save : {}", recordDto);
        this.recordService.saveRecord(recordDto);
        return new ResponseEntity(OK);
    }
}
