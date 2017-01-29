package com.mprtcz.timeloggerserver.record.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Getter
@Setter
@ToString
public class RecordDto {

    private String taskUuId;

    private Date startDateTime;

    private Date endDateTime;

    private Date creationDate;

    private Date synchronizationDate;

    private boolean active;

    private String uuId;
}
