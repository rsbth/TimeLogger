package com.mprtcz.timeloggerserver.utils.exceptions;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String s) {
        super(s);
    }
}
