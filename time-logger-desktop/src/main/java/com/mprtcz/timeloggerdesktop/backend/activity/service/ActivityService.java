package com.mprtcz.timeloggerdesktop.backend.activity.service;

import com.mprtcz.timeloggerdesktop.backend.activity.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import com.mprtcz.timeloggerdesktop.backend.activity.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.controller.MainController;
import com.mprtcz.timeloggerdesktop.web.activity.controller.ActivityWebController;
import com.mprtcz.timeloggerdesktop.web.activity.model.ActivityDto;
import com.mprtcz.timeloggerdesktop.web.activity.model.converter.ActivityConverter;
import com.mprtcz.timeloggerdesktop.web.webstatic.WebHandler;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.*;

/**
 * Created by mprtcz on 2017-01-02.
 */
public class ActivityService {
    private Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private ActivityValidator activityValidator;
    private CustomDao customDao;
    private MainController mainController;

    public ActivityService(ActivityValidator activityValidator,
                           CustomDao customDao,
                           MainController mainController) {
        this.customDao = customDao;
        this.activityValidator = activityValidator;
        this.mainController = mainController;
    }

    public List<Activity> getActivities() throws Exception {
        List<Activity> activities = customDao.getAllActivities();
        List<Activity> activeActivities = new ArrayList<>();
        for (Activity activity :
                activities) {
            if (activity.isActive()) {
                activeActivities.add(activity);
            }
        }
        for (Activity a :
                activeActivities) {
            if (a.getActivityRecords() == null) {
                break;
            }
        }
        return activeActivities;
    }

    public void removeActivity(Activity activity) throws Exception {
        activity.setActive(false);
        this.activityWebController.deleteActivityOnServer(getDeleteCallback(activity), activity);
        this.customDao.update(activity);
    }

    public void removeActivityLocally(Activity activity) throws Exception {
        activity.setActive(false);
        this.customDao.update(activity);
    }

    public ValidationResult addActivity(Activity activity) throws Exception {
        logger.info("new activity.toString() = {}", activity.toString());
        activity.setLastModified(new Date());
        activity.setActive(true);
        return validateNewActivityAndSave(activity);
    }

    public Activity findActivityById(Long id) throws Exception {
        return this.customDao.findActivityById(id);
    }

    public Activity findActivityByUuId(Long serverId) throws Exception {
        List<Activity> activities = getActivities();
        for (Activity activity :
                activities) {
            if(Objects.equals(activity.getServerId(), serverId)) {
                return activity;
            }
        }
        return null;
    }

    private ValidationResult validateNewActivityAndSave(Activity activity) throws Exception {
        logger.info("validateNewActivityAndSave, activity= " + activity.toString());
        ValidationResult validationResult = activityValidator.validateNewActivity(activity, getActivities());
        if (validationResult.isErrorFree()) {
            activity.setActive(true);
            customDao.save(activity);
            if (activity.getServerId() == null) {
                this.activityWebController.postNewActivityToServer(getPostNewActivityCallback(activity), activity);
            }
            return new ValidationResult(ValidationResult.CustomErrorEnum.RECORD_SAVED);
        } else {
            return validationResult;
        }
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
        return this.updateActivity(activity, UpdateType.SERVER);
    }

    public ValidationResult updateActivity(Activity activity, UpdateType updateType) throws Exception {
        logger.info("updateActivity activity = {}", activity);
        ValidationResult validationResult = activityValidator.validateUpdatedActivity(activity);
        if (validationResult.isErrorFree()) {
            if (activity.getServerId() == null) {
                this.activityWebController.postNewActivityToServer(getPostNewActivityCallback(activity), activity);
            } else {
                if (updateType == UpdateType.LOCAL) {
                    activity.setLastModified(new Date());
                    this.activityWebController.patchActivityOnServer(getPatchActivityCallback(), activity);
                }
            }
            logger.info("Activity to update = " + activity.toString());
            this.customDao.update(activity);
            this.mainController.updateActivityList();
            return new ValidationResult(ValidationResult.CustomErrorEnum.ACTIVITY_UPDATED);
        } else {
            return validationResult;
        }
    }

    public void updateActivityWithNewRecord(Activity activity) throws Exception {
        logger.info("updateActivityWithNewRecord, activity = {}", activity);
        this.customDao.update(activity);
        this.mainController.updateActivityList();
    }

    private Callback<List<ActivityDto>> getActivitySynchronizationCallback(List<Activity> localActivities) {
        return new Callback<List<ActivityDto>>() {
            @Override
            public void onResponse(Call<List<ActivityDto>> call, Response<List<ActivityDto>> response) {
                if (response.isSuccessful()) {
                    synchronizeLocalActivitiesWithServer(localActivities, response.body());
                } else {
                    WebHandler.handleBadCodeResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<ActivityDto>> call, Throwable throwable) {
                logger.error("getActivitySynchronizationCallback error, " + throwable.toString());
            }
        };
    }

    private void synchronizeLocalActivitiesWithServer(List<Activity> localActivities, List<ActivityDto> serverActivities) {
        logger.info("ActivityService.synchronizeLocalActivitiesWithServer, server activities = " + serverActivities);
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
                            logger.info("activity has been modified on server:\nactivity.getLastModified() = "
                                    + localActivity.getLastModified() + " serverActivity.getLastModified() = "
                                    + serverActivity.getLastModified());
                            try {
                                this.updateActivityWithServerData(localActivity, serverActivity);
                            } catch (Exception e) {
                                logger.error("Error while updating activity with server data = " + e.toString());
                                e.printStackTrace();
                            }
                        } else if (localActivity.getLastModified().after(serverActivity.getLastModified())) {
                            logger.info("activity has been modified locally:\nactivity.getLastModified() = "
                                    + localActivity.getLastModified() + " serverActivity.getLastModified() = "
                                    + serverActivity.getLastModified());
                            activitiesToSendToServer.add(localActivity);
                        } else {
                            logger.info("activity has the same modification date locally and on server:" +
                                    "\nactivity.getLastModified() = "
                                    + localActivity.getLastModified() + " serverActivity.getLastModified() = "
                                    + serverActivity.getLastModified());
                        }
                    } else {
                        logger.info("Activity is inactive in local db, activity = " + localActivity);
                        activitiesToSendToServer.add(localActivity);
                    }
                } else {
                    try {
                        this.validateNewActivityAndSave(ActivityConverter.toEntity(serverActivity));
                    } catch (Exception e) {
                        logger.error("Error while saving server activity = " + e.toString());
                        e.printStackTrace();
                    }
                }
            } else {
                logger.info("server activity is removed from server");
                if (localActivity != null && localActivity.isActive()) {
                    try {
                        this.removeActivityLocally(localActivity);
                    } catch (Exception e) {
                        logger.error("Error while removing server activity = " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        for (Activity localActivity : localActivities) {
            if (localActivity.getServerId() == null) {
                activitiesToSendToServer.add(localActivity);
            }
        }
        sendLocalChangesToServer(activitiesToSendToServer);
        logger.info("activitiesToSendToServer = " + activitiesToSendToServer);
        this.mainController.updateActivityList();
    }

    private void sendLocalChangesToServer(List<Activity> activitiesToSendToServer) {
        for (Activity activity :
                activitiesToSendToServer) {
            if (!activity.isActive() && activity.getServerId() != null) {
                this.activityWebController.deleteActivityOnServer(getDeleteCallback(activity), activity);
                continue;
            }
            if (activity.getServerId() == null) {
                this.activityWebController.postNewActivityToServer(getPostNewActivityCallback(activity), activity);
            } else {
                this.activityWebController.patchActivityOnServer(getPatchActivityCallback(), activity);
            }
        }
    }

    private Callback<Void> getDeleteCallback(Activity activity) {
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    logger.info("Deletion succesful, activity = " + activity);
                } else {
                    WebHandler.handleBadCodeResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                logger.warn("Deleting unsuccessful, " + throwable.toString());
                throwable.printStackTrace();
            }
        };
    }

    private Callback<ActivityDto> getPostNewActivityCallback(Activity activity) {
        return new Callback<ActivityDto>() {
            @Override
            public void onResponse(Call<ActivityDto> call, Response<ActivityDto> response) {
                ActivityDto activityDto = response.body();
                logger.info("getPostNewActivityCallback, response.body() = {}", response.body());
                if (response.isSuccessful()) {
                    try {
                        updateActivityWithServerData(activity, activityDto);
                    } catch (Exception e) {
                        logger.warn("Update unsuccesful, exception = {},\n response body = {}",
                                e.toString(), response.code());
                        e.printStackTrace();
                    }
                } else {
                    WebHandler.handleBadCodeResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<ActivityDto> call, Throwable throwable) {
                logger.warn("Posting new activity on server unsuccessful, " + throwable.toString());
                throwable.printStackTrace();
            }
        };
    }

    private Callback<ActivityDto> getPatchActivityCallback() {
        return new Callback<ActivityDto>() {
            @Override
            public void onResponse(Call<ActivityDto> call, Response<ActivityDto> response) {
                if (response.isSuccessful()) {
                    logger.info("Patching succesful");
                } else {
                    WebHandler.handleBadCodeResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<ActivityDto> call, Throwable throwable) {
                logger.warn("Updating on server unsuccessful, " + throwable.toString());
                throwable.printStackTrace();
            }
        };
    }

    private Map<Long, Activity> getLocalActivitiesMap(List<Activity> activities) {
        Map<Long, Activity> map = new HashMap<>();
        for (Activity activity :
                activities) {
            if (activity.getServerId() != null) {
                map.put(activity.getServerId(), activity);
            }
        }
        return map;
    }

    public void synchronizeActivities(ActivityWebController activityWebController) throws Exception {
        this.activityWebController = activityWebController;
        List<Activity> localActivities = getAllActivities();
        this.activityWebController.getActivitiesFromServer(getActivitySynchronizationCallback(localActivities));
    }

    private List<Activity> getAllActivities() throws Exception {
        return this.customDao.getAllActivities();
    }

    private ActivityWebController activityWebController;

    public HoursData getHoursData() throws Exception {
        return new HoursData(getActivities());
    }

    public void exportDataToFile() throws Exception {
        this.marshallAndExport();
    }

    public void importDataFromFile(File file) throws Exception {
        Activities activities = this.importAndUnmarshall(file);
        this.saveImportedActivities(activities);

    }

    private Activities importAndUnmarshall(File file) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Activities.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (Activities) unmarshaller.unmarshal(file);
    }

    private void setActivitiesInRecords(Activities activities) {
        for (Activity a :
                activities.getActivities()) {
            Collection<Record> records = a.getActivityRecords();
            if (records != null) {
                for (Record record :
                        records) {
                    record.setActivity(a);
                }
            }
        }
    }

    private void saveImportedActivities(Activities activities) throws Exception {
        this.setActivitiesInRecords(activities);
        List<Activity> activitiesList = activities.getActivities();
        logger.info("Unmarshalled activities = {}", activities);
        this.customDao.replaceAllData(activitiesList);
    }

    private void marshallAndExport() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Activities.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Activities activities = new Activities(getActivities());
        marshaller.marshal(activities, new File("./data.xml"));
    }

    @XmlRootElement(name = "Activities")
    @XmlAccessorType(XmlAccessType.FIELD)
    @ToString
    @Getter
    private static class Activities {
        @XmlElement(name = "activity")
        List<Activity> activities = new ArrayList<>();

        public Activities() {
        }

        Activities(List<Activity> activities) {
            this.activities = activities;
        }
    }

    public enum UpdateType {
        SERVER,
        LOCAL,
        RECORD_UPDATE
    }
}
