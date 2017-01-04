package com.mprtcz.timeloggerdesktop.dao;

import com.mprtcz.timeloggerdesktop.model.Activity;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public interface CustomDao {
    void save(Activity activity) throws Exception;

    List<Activity> getAll() throws Exception;
}
