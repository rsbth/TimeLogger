package com.mprtcz.timeloggerdesktop.backend.activity.dao;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class InMemoryCustomDao implements CustomDao {

    private static List<Activity> activities = new ArrayList<>();
    static {
        activities.add(new Activity(1L, "name", "description", "#ffcdd2"));
        activities.add(new Activity(2L, "name2", "long description", "#f8bbd0"));
        activities.add(new Activity(3L, "name3", "even longer description", "#e1bee7"));
        activities.add(new Activity(4L, "name4", "even longer description \nwhich will contain more than 50 " +
                "word necessary to identify whether a string cutting method appears to work correctly", "#d1c4e9"));
    }

    private static List<Record> records = new ArrayList<>();
    static {
        records.add(new Record(LocalTime.of(1, 0), LocalTime.of(1, 0), LocalDate.now(), LocalDate.now(), activities.get(0)));
    }

    @Override
    public void save(Activity activity) throws Exception {
        if(activity.getId() == null) {
            activity.setId(getBiggestId() + 1);
        }
        activities.add(activity);
    }

    @Override
    public void update(Activity activity) throws Exception {
        int id = activities.indexOf(activity);
        if(id != -1) {
            activities.set(id, activity);
        }
    }

    @Override
    public void remove(Activity activity) throws Exception {
        int id = -1;
        for (Activity a :
                activities) {
            if(Objects.equals(a.getId(), activity.getId())) {
                id = activities.indexOf(a);
            }
        }
        activities.remove(id);
    }

    @Override
    public List<Activity> getAllActivities() {
        return activities;
    }

    @Override
    public List<Record> getAllRecords() {
        return records;
    }

    @Override
    public Activity findActivityById(Long id) throws Exception {
        Integer idInt = Math.toIntExact(id);
        return activities.get(idInt-1);
    }

    @Override
    public void replaceAllData(List<Activity> activitiesList) throws Exception {
        activities = activitiesList;
    }

    private long getBiggestId() {
        long biggest = 0;
        for (Activity a :
                activities) {
            if(a.getId() > biggest) {
                biggest = a.getId();
            }
        }
        return biggest;
    }

}
