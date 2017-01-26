package com.mprtcz.timeloggerdesktop.backend.activity.service;

import com.mprtcz.timeloggerdesktop.backend.activity.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import com.mprtcz.timeloggerdesktop.backend.activity.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.web.activity.controller.ActivityWebController;
import com.mprtcz.timeloggerdesktop.web.activity.model.ActivityDto;
import com.mprtcz.timeloggerdesktop.web.activity.model.converter.ActivityConverter;
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

    public ActivityService(ActivityValidator activityValidator,
                           CustomDao customDao) {
        this.customDao = customDao;
        this.activityValidator = activityValidator;
    }

    public List<Activity> getActivities() throws Exception {
        List<Activity> activities = customDao.getAllActivities();
        for (Activity a :
                activities) {
            if (a.getActivityRecords() == null) {
                break;
            }
        }
        return activities;
    }

    public void removeActivity(Activity activity) throws Exception {
        this.customDao.remove(activity);
    }

    public ValidationResult addActivity(Activity activity) throws Exception {
        logger.info("new activity.toString() = {}", activity.toString());
        activity.setLastModified(new Date());
        return validateNewActivityAndSave(activity);
    }

    public Activity findActivityById(Long id) throws Exception {
        return this.customDao.findActivityById(id);
    }

    private ValidationResult validateNewActivityAndSave(Activity activity) throws Exception {
        ValidationResult validationResult = activityValidator.validateNewActivity(activity, getActivities());
        if (validationResult.isErrorFree()) {
            customDao.save(activity);
            return new ValidationResult(ValidationResult.CustomErrorEnum.RECORD_SAVED);
        } else {
            return validationResult;
        }
    }

    private ValidationResult updateActivityWithServerData(Activity activity, ActivityDto activityDto)
            throws Exception {
        activity.setName(activityDto.getName());
        activity.setLastModified(activityDto.getLastModified());
        activity.setColor(activityDto.getColor());
        activity.setDescription(activityDto.getDescription());
        return this.updateActivity(activity, UpdateType.SERVER);
    }

    public ValidationResult updateActivity(Activity activity, UpdateType updateType) throws Exception {
        logger.info("updateActivity activity = {}", activity);
        ValidationResult validationResult = activityValidator.validateUpdatedActivity(activity);
        if (validationResult.isErrorFree()) {
            if(updateType == UpdateType.LOCAL) {
                activity.setLastModified(new Date());
            }
            logger.info("Activity to update = " +activity.toString());
            this.customDao.update(activity);
            return new ValidationResult(ValidationResult.CustomErrorEnum.ACTIVITY_UPDATED);
        } else {
            return validationResult;
        }
    }

    private Callback<List<ActivityDto>> getActivitySynchronizationCallback(List<Activity> localActivities) {
        return new Callback<List<ActivityDto>>() {
            @Override
            public void onResponse(Call<List<ActivityDto>> call, Response<List<ActivityDto>> response) {
                updateActivities(localActivities, response.body());
            }

            @Override
            public void onFailure(Call<List<ActivityDto>> call, Throwable throwable) {

            }
        };
    }

    private void updateActivities(List<Activity> localActivities, List<ActivityDto> serverActivities) {
        logger.info("ActivityService.updateActivities, server activities = " + serverActivities);
        Map<Long, Activity> localActivitiesMap = getLocalActivitiesMap(localActivities);
        List<Activity> activitiesToSendToServer = new ArrayList<>();
        for (ActivityDto serverActivity :
                serverActivities) {
            Activity activity = localActivitiesMap.get(serverActivity.getId());
            logger.info("server Activity  = " +serverActivity.toString());
            logger.info("local matching Activity  = " +activity);
            if (serverActivity.isActive()) { //server activity is not removed on server
                logger.info("server activity is active");
                if (activity != null) { //activity exists in local db
                    if (activity.getLastModified().before(serverActivity.getLastModified())) {
                        logger.info("activity has been modified on server:\nactivity.getLastModified() = "
                        +activity.getLastModified() + " serverActivity.getLastModified() = " +serverActivity.getLastModified());
                        try {
                            this.updateActivityWithServerData(activity, serverActivity); //updating local activity
                        } catch (Exception e) {
                            logger.error("Error while updating activity with server data = " + e.toString());
                            e.printStackTrace();
                        }
                    } else if (activity.getLastModified().after(serverActivity.getLastModified())) {
                        logger.info("activity has been modified locally:\nactivity.getLastModified() = "
                                +activity.getLastModified() + " serverActivity.getLastModified() = "
                                +serverActivity.getLastModified());
                        activitiesToSendToServer.add(activity);
                    } else {
                        logger.info("activity has the same modification date locally and on server:" +
                                "\nactivity.getLastModified() = "
                                +activity.getLastModified() + " serverActivity.getLastModified() = "
                                +serverActivity.getLastModified());
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
                if (activity != null) { //activity exists but is not active on server (removed)
                    try {
                        this.removeActivity(activity);
                    } catch (Exception e) {
                        logger.error("Error while removing server activity = " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        logger.info("activitiesToSendToServer = " + activitiesToSendToServer);
    }

    private Map<Long, Activity> getLocalActivitiesMap(List<Activity> activities) {
        Map<Long, Activity> map = new HashMap<>();
        for (Activity activity :
                activities) {
            map.put(activity.getUuId(), activity);
        }
        return map;
    }

    public void synchronizeActivities(ActivityWebController activityWebController) throws Exception {
        List<Activity> localActivities = getActivities();
        activityWebController.getActivitiesFromServer(getActivitySynchronizationCallback(localActivities));
    }

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
