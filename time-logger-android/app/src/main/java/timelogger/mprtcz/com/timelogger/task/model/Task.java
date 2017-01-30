package timelogger.mprtcz.com.timelogger.task.model;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import timelogger.mprtcz.com.timelogger.record.model.Record;

/**
 * Created by Azet on 2017-01-17.
 */
@Getter
@Setter
@ToString
@DatabaseTable(tableName = "tasks")
public class Task {
    private static final String TAG = "Task";

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private Long serverId;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String description;

    @DatabaseField(canBeNull = false)
    private String color;

    @DatabaseField(canBeNull = false)
    private Date lastModified;

    @DatabaseField(canBeNull = false)
    private boolean active;

    @ForeignCollectionField(eager = true)
    Collection<Record> taskRecords;

    public Task() {
    }

    public Task(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public Task(Long id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public void addRecord(Record record) {
        if (this.taskRecords == null) {
            this.taskRecords = new ArrayList<>();
        }
        List<Record> records = new ArrayList<>(this.taskRecords);
        int recordPosition = records.indexOf(record);
        if (recordPosition != -1) {
            Log.i(TAG, "Record exists at " +recordPosition +" replacing: " + record);
            records.set(recordPosition, record);
            this.taskRecords = records;
        } else {
            this.taskRecords.add(record);
        }
    }

    public static List<Task> tasks = new ArrayList<>();
    static {
        tasks.add(new Task(1L, "name", "description", "#03a9f4"));
        tasks.add(new Task(2L, "name2", "long description", "#009688"));
        tasks.add(new Task(3L, "name3", "even longer description", "#795548"));
        tasks.add(new Task(4L, "name4", "even longer description \nwhich will contain more than 50 " +
                "word necessary to identify whether a string cutting method appears to work correctly", "#2196f3"));
        Record record = new Record(new Date(2017, 1, 20), new Date(2017, 1, 20), tasks.get(0));
    }

}
