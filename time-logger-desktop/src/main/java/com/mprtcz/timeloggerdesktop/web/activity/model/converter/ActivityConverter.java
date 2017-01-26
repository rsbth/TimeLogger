package com.mprtcz.timeloggerdesktop.web.activity.model.converter;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.web.activity.model.ActivityDto;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class ActivityConverter {

    public static Activity toEntity(ActivityDto activityDto) {
        Activity activity = new Activity();
        activity.setName(activityDto.getName());
        activity.setColor(activityDto.getColor());
        activity.setDescription(activityDto.getDescription());
        activity.setLastModified(activityDto.getLastModified());
        activity.setUuId(activityDto.getId());
        return activity;
    }
}
