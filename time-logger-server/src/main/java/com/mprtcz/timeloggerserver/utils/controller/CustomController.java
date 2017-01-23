package com.mprtcz.timeloggerserver.utils.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mprtcz on 2017-01-23.
 */
@RestController
public class CustomController {


    @RequestMapping("/hello")
    public ResponseEntity getDefaultMessage() {
        return new ResponseEntity<>("Hello world!", HttpStatus.OK);
    }
}
