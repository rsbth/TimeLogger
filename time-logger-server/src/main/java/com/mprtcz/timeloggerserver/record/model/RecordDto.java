package com.mprtcz.timeloggerserver.record.model;

import com.mprtcz.timeloggerserver.task.model.Task;

import java.time.LocalDateTime;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class RecordDto {

    private Task task;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
}
