package com.mprtcz.timeloggerdesktop.dao;

import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.Record;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public interface CustomDao {
    <T> void save(T t) throws Exception;

    List<Activity> getAllActivities() throws Exception;

    List<Record> getAllRecords() throws Exception;
}
