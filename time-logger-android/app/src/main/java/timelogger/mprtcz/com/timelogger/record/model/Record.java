package timelogger.mprtcz.com.timelogger.record.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-17.
 */
@Getter
@Setter
@DatabaseTable(tableName = "records")
public class Record {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, foreign = true)
    private Task task;

    @DatabaseField
    private Date startDateTime;

    @DatabaseField
    private Date endDateTime;

    @DatabaseField
    private Date creationDate;

    @DatabaseField
    private Date synchronizationDate;

    @DatabaseField
    private String uuId;

    @DatabaseField(canBeNull = false)
    private boolean active;

    public Record() {
    }

    public Record(Date startDateTime,
                  Date endDateTime,
                  Task task) {
        this.task = task;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", task name=" + task.getName() +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", creationDate=" + creationDate +
                ", synchronizationDate=" + synchronizationDate +
                ", uuId='" + uuId + '\'' +
                ", active=" + active +
                '}';
    }
}
