package com.mprtcz.timeloggerserver.utils;

import com.mprtcz.timeloggerserver.record.validator.RecordValidationException;
import com.mprtcz.timeloggerserver.utils.exceptions.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by mprtcz on 2017-01-24.
 */
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestResponseExceptionHandler.class);


    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    protected ResponseEntity handleEmptyResultDataAccessException(RuntimeException rex, WebRequest request) {
        String responseBody = "Null pointer exception";
        logger.info("Request = {}", request);
        logger.error(rex.toString());
        return handleExceptionInternal(rex, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = TaskNotFoundException.class)
    protected ResponseEntity handleTaskNotFoundException(RuntimeException rex, WebRequest request) {
        String responseBody = rex.getMessage();
        logger.info("Request = {}", request);
        logger.error(rex.toString());
        return handleExceptionInternal(rex, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity handleIllegalArgumentException(RuntimeException rex, WebRequest request) {
        String responseBody = rex.getMessage();
        logger.info("Request = {}", request);
        logger.error(rex.toString());
        return handleExceptionInternal(rex, responseBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(value = RecordValidationException.class)
    protected ResponseEntity handleRecordValidationException(RecordValidationException rex, WebRequest request) {
        String responseBody = rex.getViolationsMessage().toString();
        logger.info("Request = {}", request);
        logger.error(rex.toString());
        return handleExceptionInternal(rex, responseBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String responseBody = "Malformed request";
        logger.info("Request = {}", request);
        logger.error(ex.toString());
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);

    }

}
