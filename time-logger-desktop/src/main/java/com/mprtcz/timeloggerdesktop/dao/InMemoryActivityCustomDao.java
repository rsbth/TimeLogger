package com.mprtcz.timeloggerdesktop.dao;

import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class InMemoryActivityCustomDao implements CustomDao {

    public static List<Activity> activities = new ArrayList<>();
    static {
        activities.add(new Activity("name", "description", "#ffcdd2"));
        activities.add(new Activity("name2", "long description", "#f8bbd0"));
        activities.add(new Activity("name3", "even longer description", "#e1bee7"));
        activities.add(new Activity("name4", "even longer description \nwhich will contain more than 50 " +
                "word necessary to identify whether a string cutting method appears to work correctly", "#d1c4e9"));
    }

    public static List<Record> records = new ArrayList<>();
    static {
        records.add(new Record(LocalTime.of(1, 0), LocalTime.of(1, 0), LocalDate.now(), LocalDate.now(), activities.get(0)));
    }

    @Override
    public <T> void save(T object) {
        if(object instanceof Activity) {
            activities.add((Activity) object);
        }
        if(object instanceof Record) {
            records.add((Record) object);
        }
    }

    @Override
    public List<Activity> getAllActivities() {
        return activities;
    }

    @Override
    public List<Record> getAllRecords() {
        return records;
    }

}
