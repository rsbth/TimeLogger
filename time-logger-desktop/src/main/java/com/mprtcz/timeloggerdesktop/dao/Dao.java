package com.mprtcz.timeloggerdesktop.dao;

import com.mprtcz.timeloggerdesktop.model.Activity;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
public interface Dao {
    void save(Activity activity);

    List<Activity> getAll();
}
