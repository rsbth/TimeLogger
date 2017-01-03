package com.mprtcz.timeloggerdesktop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@Setter
public class Activity {
    private Long id;
    private String name;
    private String description;

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static List<Activity> activities = new ArrayList<>();
    static {
        activities.add(new Activity("name", "description"));
        activities.add(new Activity("name2", "long description"));
        activities.add(new Activity("name3", "even longer description"));
    }

    @Override
    public String toString() {
        return name;
    }
}
