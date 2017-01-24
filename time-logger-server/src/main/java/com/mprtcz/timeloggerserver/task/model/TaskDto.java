package com.mprtcz.timeloggerserver.task.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Getter
@Setter
public class TaskDto {

    private Long id;

    private String name;

    private String description;

    private String color;
}
