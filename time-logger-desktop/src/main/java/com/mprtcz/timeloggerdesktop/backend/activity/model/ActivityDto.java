package com.mprtcz.timeloggerdesktop.backend.activity.model;

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
public class ActivityDto {

    private Long serverId;

    private String name;

    private String description;

    private String color;

    private Date lastModified;

    private boolean active;
}
