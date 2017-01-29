package com.mprtcz.timeloggerserver.task.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Getter
@Setter
public class TaskDto {

    private Long serverId;

    private String name;

    private String description;

    private String color;

    private Date lastModified;

    private boolean active;
}
