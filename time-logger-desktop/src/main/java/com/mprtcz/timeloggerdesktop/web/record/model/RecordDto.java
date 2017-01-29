package com.mprtcz.timeloggerdesktop.web.record.model;

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

    private Long taskServerId;

    private Date startDateTime;

    private Date endDateTime;

    private Date creationDate;

    private Date synchronizationDate;

    private String uuId;

    private boolean active;
}