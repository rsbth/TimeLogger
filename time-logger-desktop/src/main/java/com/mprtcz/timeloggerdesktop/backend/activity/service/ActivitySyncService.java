package com.mprtcz.timeloggerdesktop.backend.activity.service;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.backend.activity.controller.ActivityWebController;
import com.mprtcz.timeloggerdesktop.backend.activity.model.ActivityDto;
import com.mprtcz.timeloggerdesktop.backend.activity.model.converter.ActivityConverter;
import com.mprtcz.timeloggerdesktop.backend.utilities.webutils.CustomWebCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mprtcz on 2017-01-30.
 */
public class ActivitySyncService {
    private Logger logger = LoggerFactory.getLogger(ActivitySyncService.class);

    private ActivityWebController activityWebController;
    private ActivityService activityService;

    public ActivitySyncService(ActivityWebController activityWebController,
                               ActivityService activityService) {
        this.activityWebController = activityWebController;
        this.activityService = activityService;
    }

    public void synchronizeActivities(ActivityWebController activityWebController) throws Exception {
        this.activityWebController = activityWebController;
        List<Activity> localActivities = this.activityService.getAllActivities();
        this.activityWebController.getActivitiesFromServer(getActivitySynchronizationCallback(localActivities));
    }

    private Callback<List<ActivityDto>> getActivitySynchronizationCallback(List<Activity> localActivities) {
        return new CustomWebCallback<List<ActivityDto>>() {
            @Override
            public void onSuccessfulCall(Response<List<ActivityDto>> response) {
                synchronizeLocalActivitiesWithServer(localActivities, response.body());
            }
        };
    }

    private void synchronizeLocalActivitiesWithServer(List<Activity> localActivities,
                                                      List<ActivityDto> serverActivities) {
        logger.info("ActivityService.synchronizeLocalActivitiesWithServer, server activities = "
                + serverActivities);
        Map<Long, Activity> localActivitiesMap = getLocalActivitiesMap(localActivities);
        if (serverActivities == null) {
            return;
        }
        List<Activity> activitiesToSendToServer = new ArrayList<>();
        for (ActivityDto serverActivity : serverActivities) {
            Activity localActivity = localActivitiesMap.get(serverActivity.getServerId());
            logger.info("server Activity  = " + serverActivity.toString());
            logger.info("local matching Activity  = " + localActivity);
            if (serverActivity.isActive()) {
                logger.info("server activity is active");
                if (localActivity != null) {
                    logger.info("activity exists in local db");
                    if (localActivity.isActive()) {
                        logger.info("activity is active in local db");
                        if (localActivity.getLastModified().before(serverActivity.getLastModified())) {
                            logModificationDates("activity has been modified on server:\n",
                                    serverActivity, localActivity);
                            try {
                                this.updateActivityWithServerData(localActivity, serverActivity);
                            } catch (Exception e) {
                                logger.error("Error while updating activity with server data = " + e.toString());
                                e.printStackTrace();
                            }
                        } else if (localActivity.getLastModified().after(serverActivity.getLastModified())) {
                            logModificationDates("activity has been modified locally:\n",
                                    serverActivity, localActivity);
                            activitiesToSendToServer.add(localActivity);
                        } else {
                            logModificationDates(
                                    "activity has the same modification date locally and on server:\n",
                                    serverActivity, localActivity);
                        }
                    } else {
                        logger.info("Activity is inactive in local db, activity = " + localActivity);
                        activitiesToSendToServer.add(localActivity);
                    }
                } else {
                    try {
                        this.activityService.validateNewActivityAndSave(
                                ActivityConverter.toEntity(serverActivity));
                    } catch (Exception e) {
                        logger.error("Error while saving server activity = " + e.toString());
                        e.printStackTrace();
                    }
                }
            } else {
                logger.info("server activity is removed from server");
                if (localActivity != null && localActivity.isActive()) {
                    try {
                        this.activityService.removeActivityLocally(localActivity);
                    } catch (Exception e) {
                        logger.error("Error while removing server activity = " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        activitiesToSendToServer.addAll(localActivities.stream()
                .filter(localActivity -> localActivity.getServerId() == null)
                .collect(Collectors.toList()));
        sendLocalChangesToServer(activitiesToSendToServer);
        logger.info("activitiesToSendToServer = " + activitiesToSendToServer);
        this.activityService.getMainController().updateActivityList();
        this.activityService.getMainController().processRecordSynchronization();
    }


    private Map<Long, Activity> getLocalActivitiesMap(List<Activity> activities) {
        Map<Long, Activity> map = new HashMap<>();
        activities.stream().filter(activity -> activity.getServerId() != null).forEach(
                activity -> map.put(activity.getServerId(), activity));
        return map;
    }

    private ValidationResult updateActivityWithServerData(Activity activity, ActivityDto activityDto)
            throws Exception {
        logger.info("updateActivityWithServerData(Activity activity = {},\n ActivityDto activityDto = {})",
                activity, activityDto);
        activity.setName(activityDto.getName());
        activity.setLastModified(activityDto.getLastModified());
        activity.setColor(activityDto.getColor());
        activity.setDescription(activityDto.getDescription());
        activity.setServerId(activityDto.getServerId());
        return this.activityService.updateActivity(activity, ActivityService.UpdateType.SERVER);
    }

    private void sendLocalChangesToServer(List<Activity> activitiesToSendToServer) {
        for (Activity activity :
                activitiesToSendToServer) {
            if (!activity.isActive() && activity.getServerId() != null) {
                this.activityWebController.deleteActivityOnServer(getDeleteCallback(activity), activity);
                continue;
            }
            if (activity.getServerId() == null) {
                this.activityWebController.postNewActivityToServer(
                        getPostNewActivityCallback(activity), activity);
            } else {
                this.activityWebController.patchActivityOnServer(getPatchActivityCallback(), activity);
            }
        }
    }

    private Callback<Void> getDeleteCallback(Activity activity) {
        return new CustomWebCallback<Void>() {
            @Override
            public void onSuccessfulCall(Response<Void> response) {
                logger.info("Deletion successful, activity = " + activity);
            }
        };
    }

    private Callback<ActivityDto> getPostNewActivityCallback(Activity activity) {
        return new CustomWebCallback<ActivityDto>() {
            @Override
            public void onSuccessfulCall(Response<ActivityDto> response) {
                ActivityDto activityDto = response.body();
                try {
                    updateActivityWithServerData(activity, activityDto);
                } catch (Exception e) {
                    logger.warn("Update unsuccessful, exception = {},\n response body = {}",
                            e.toString(), response.code());
                    e.printStackTrace();
                }
            }
        };
    }

    private Callback<ActivityDto> getPatchActivityCallback() {
        return new CustomWebCallback<ActivityDto>() {
            @Override
            public void onSuccessfulCall(Response<ActivityDto> response) {
                logger.info("Patching successful");
            }
        };
    }

    public void postNewActivityToServer(Activity activity) {
        this.activityWebController.postNewActivityToServer(
                getPostNewActivityCallback(activity), activity);
    }

    public void patchActivityOnServer(Activity activity) {
        this.activityWebController.patchActivityOnServer(
                getPatchActivityCallback(), activity);
    }

    public void deleteActivityOnServer(Activity activity) {
        this.activityWebController.deleteActivityOnServer(
                getDeleteCallback(activity), activity);
    }

    private void logModificationDates(String message, ActivityDto serverActivity, Activity localActivity) {
        logger.info(message + "activity.getLastModified() = "
                + localActivity.getLastModified() + " serverActivity.getLastModified() = "
                + serverActivity.getLastModified());
    }
}
