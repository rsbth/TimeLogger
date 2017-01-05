package com.mprtcz.timeloggerdesktop.dao;

import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.Record;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public interface CustomDao {
    void save(Activity activity) throws Exception;

    void update(Activity activity) throws Exception;

    List<Activity> getAllActivities() throws Exception;

    List<Record> getAllRecords() throws Exception;

    Activity findActivityById(Long id) throws Exception;
}
