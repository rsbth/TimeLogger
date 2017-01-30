package com.mprtcz.timeloggerdesktop.backend.activity.service;

import com.mprtcz.timeloggerdesktop.backend.activity.dao.CustomDao;
import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import com.mprtcz.timeloggerdesktop.backend.activity.validators.ActivityValidator;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import com.mprtcz.timeloggerdesktop.frontend.controller.MainController;
import com.mprtcz.timeloggerdesktop.web.activity.service.ActivitySyncService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Getter
    private MainController mainController;
    @Setter
    ActivitySyncService activitySyncService;

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
        if(this.activitySyncService != null) {
            this.activitySyncService.deleteActivityOnServer(activity);
        }
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
            if (Objects.equals(activity.getServerId(), serverId)) {
                return activity;
            }
        }
        return null;
    }

    public ValidationResult validateNewActivityAndSave(Activity activity) throws Exception {
        logger.info("validateNewActivityAndSave, activity= " + activity.toString());
        ValidationResult validationResult = activityValidator.validateNewActivity(activity, getActivities());
        if (validationResult.isErrorFree()) {
            activity.setActive(true);
            customDao.save(activity);
            if (activity.getServerId() == null && this.activitySyncService != null) {
                this.activitySyncService.postNewActivityToServer(activity);
            }
            return new ValidationResult(ValidationResult.CustomErrorEnum.RECORD_SAVED);
        } else {
            return validationResult;
        }
    }

    public ValidationResult updateActivity(Activity activity, UpdateType updateType) throws Exception {
        logger.info("updateActivity activity = {}", activity);
        ValidationResult validationResult = activityValidator.validateUpdatedActivity(activity);
        if (validationResult.isErrorFree()) {
            if (activity.getServerId() == null && this.activitySyncService != null) {
                this.activitySyncService.postNewActivityToServer(activity);
            } else {
                if (updateType == UpdateType.LOCAL) {
                    activity.setLastModified(new Date());
                    if(this.activitySyncService != null) {
                        this.activitySyncService.patchActivityOnServer(activity);
                    }
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

    public List<Activity> getAllActivities() throws Exception {
        return this.customDao.getAllActivities();
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
