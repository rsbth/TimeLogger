package timelogger.mprtcz.com.timelogger.task.model;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;

/**
 * Created by Azet on 2017-01-20.
 */
@Getter
public class HoursDataService {
    public static final String TAG = "HoursDataService";

    private List<Task> allTasks;
    private List<Record> allRecords;
    private DateTime earliest;
    private DateTime latest;

    private List<Hour> hours = new ArrayList<>();

    public HoursDataService(List<Task> allTasks) {
        this.allTasks = allTasks;
        this.getAllRecords();
        this.setExtremeRecords();
        this.createHoursList();
        this.fillHoursWithActivities();
    }

    private void getAllRecords() {
        this.allRecords = new ArrayList<>();
        for (Task task : this.allTasks) {
            if(task.getTaskRecords() != null) {
                this.allRecords.addAll(task.getTaskRecords());
            }
        }
        LogWrapper.d(TAG, "allRecords = " + allRecords);
    }

    private void setExtremeRecords() {
        Date earliestRecord = new Date(Long.MAX_VALUE);
        Date latestRecord = new Date(Long.MIN_VALUE);
        if(allRecords.size() > 0) {
            for (Record record : allRecords) {
                if (record.getStartDateTime().before(earliestRecord)) {
                    earliestRecord = record.getStartDateTime();
                }
                if (record.getEndDateTime().after(latestRecord)) {
                    latestRecord = record.getEndDateTime();
                }
            }
            this.earliest = new DateTime(earliestRecord);
            this.latest = new DateTime(latestRecord);
        } else {
            this.latest = new DateTime();
            this.earliest = new DateTime();
        }
    }

    private long calculateDayDelta(DateTime earliest, DateTime current) {
        DateTime earliestModulus = earliest;
        earliestModulus = earliestModulus.minusHours(earliestModulus.getHourOfDay());
        return Days.daysBetween(earliestModulus, current).getDays();
    }

    public HoursDataService.Hour[][] getHoursArray() {
        long dayDelta = calculateDayDelta(earliest, latest);
        LogWrapper.d(TAG, "getHoursArray dayDelta = " +dayDelta);
        LogWrapper.d(TAG, "earlitest =" +earliest);
        LogWrapper.d(TAG, "latest = " +latest);
        int dayDeltaInt = (int)dayDelta;
        HoursDataService.Hour[][] hoursArray = new HoursDataService.Hour[dayDeltaInt + 1][24];

        for (HoursDataService.Hour hourObject : this.getHours()) {
            int hour = hourObject.getDatetime().getHourOfDay();
            int objectsDayDelta = (int)(calculateDayDelta(earliest, hourObject.getDatetime()));

            hoursArray[objectsDayDelta][hour] = hourObject;
        }
        return hoursArray;
    }

    private void createHoursList() {
        LogWrapper.d(TAG, "Earliest = " + earliest);
        LogWrapper.d(TAG, "Latest = " + latest);
        long hoursDelta = Hours.hoursBetween(earliest, latest).getHours();
        LogWrapper.i(TAG, "hoursDelta = " + hoursDelta);
        DateTime currentHour = earliest;
        this.hours = new ArrayList<>();
        for (int i = 0; i < hoursDelta; i++) {
            Hour hour = new Hour(currentHour);
            this.hours.add(hour);
            currentHour = currentHour.plusHours(1);
        }
    }

    private void fillHoursWithActivities() {
        for (Record record : this.allRecords) {
            DateTime start = new DateTime(record.getStartDateTime());
            DateTime end = new DateTime(record.getEndDateTime());
            LogWrapper.d(TAG, "Start datetime = " +start +", end Datetime = "+ end);
            long duration = Hours.hoursBetween(start, end).getHours();
            int position = getListPositionOfSpecificHour(start);
            for (int i = 0; i < duration; i++) {
                Hour hour = this.hours.get(position);
                hour.getActivitiesDuringThisHour().add(record.getTask());
                position++;
            }
        }
    }

    private int getListPositionOfSpecificHour(DateTime hourToFind) {
        for (Hour hour :
                this.hours) {
//            LogWrapper.d(TAG, "Current hour = " +hour);
//            LogWrapper.d(TAG, "hour to find = " +hourToFind);
            if (hourToFind.equals(hour.getDatetime())) {
                return this.hours.indexOf(hour);
            }
        }
        LogWrapper.e(TAG, "Couldn't find hour = " +hourToFind);
        return -1;
    }

    public static DateTime getLatestDateTime(List<Task> allTasks) {
        Date latestRecord = new Date(Long.MIN_VALUE);
        boolean recordsEmpty = true;

        for (Task task :
                allTasks) {
            for (Record record :
                    task.getTaskRecords()) {
                recordsEmpty = false;
                   if(record.getEndDateTime().after(latestRecord)) {
                       latestRecord = record.getEndDateTime();
                   }
            }
        }

        if(recordsEmpty) {
            return new DateTime();
        } else {
            return new DateTime(latestRecord);
        }
    }

    @Getter
    @EqualsAndHashCode
    public static class Hour {
        List<Task> activitiesDuringThisHour;
        DateTime datetime;

        public Hour(DateTime datetime) {
            this.datetime = datetime;
            this.activitiesDuringThisHour = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "\nHour{\n" +
                    ", datetime=" + datetime +
                    ", num of activities = " + activitiesDuringThisHour.size() +
                    "}";
        }
    }
}
