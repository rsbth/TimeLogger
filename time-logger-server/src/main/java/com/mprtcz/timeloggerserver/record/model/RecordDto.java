package com.mprtcz.timeloggerserver.record.model;

import com.mprtcz.timeloggerserver.task.model.TaskDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Getter
@Setter
public class RecordDto {

    private TaskDto taskDto;

    private Date startDateTime;

    private Date endDateTime;

    private Date creationDate;

    private Date synchronizationDate;
}
