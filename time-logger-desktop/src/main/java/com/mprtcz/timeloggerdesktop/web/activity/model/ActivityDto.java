package com.mprtcz.timeloggerdesktop.web.activity.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Getter
@Setter
@ToString
public class ActivityDto {

    private Long id;

    private String name;

    private String description;

    private String color;
}
