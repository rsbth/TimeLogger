package timelogger.mprtcz.com.timelogger.record.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-17.
 */
@Getter
@Setter
public class Record {

    private Long id;

    private Task task;

    private Date startDateTime;

    private Date endDateTime;

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
                ", activityName=" + task.getName() +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
