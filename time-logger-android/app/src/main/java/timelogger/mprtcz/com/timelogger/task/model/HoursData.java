package timelogger.mprtcz.com.timelogger.task.model;

import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import timelogger.mprtcz.com.timelogger.record.model.Record;

/**
 * Created by Azet on 2017-01-20.
 */
@Getter
public class HoursData {
    public static final String TAG = "HoursData";

    private List<Task> allTasks;
    private List<Record> allRecords;
    private DateTime earliest;
    private DateTime latest;

    public HoursData(List<Task> allTasks) {
        this.allTasks = allTasks;
        this.getAllRecords();
        this.setExtremeRecords();
    }

    private void getAllRecords() {
        this.allRecords = new ArrayList<>();
        for (Task task : this.allTasks) {
            if(task.getTaskRecords() != null) {
                this.allRecords.addAll(task.getTaskRecords());
            }
        }
        System.out.println("allRecords = " + allRecords);
        Log.d(TAG, "allRecords = " + allRecords);
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
        }
    }
}
