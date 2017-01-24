package com.mprtcz.timeloggerserver.record.controller;

import com.mprtcz.timeloggerserver.record.model.RecordDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by mprtcz on 2017-01-24.
 */
@RestController("/record")
public class RecordController {

    @RequestMapping("/all")
    public ResponseEntity getAllRecords() {
        return new ResponseEntity(OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity addRecords(@RequestBody RecordDto recordDto) {
        return new ResponseEntity(OK);
    }
}
