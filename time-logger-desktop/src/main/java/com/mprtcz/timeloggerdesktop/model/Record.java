package com.mprtcz.timeloggerdesktop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@DatabaseTable(tableName = "records")
public class Record {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false, foreign = true)
    private Activity activity;

    @DatabaseField
    private Date startDateTime;
    @DatabaseField
    private Date endDateTime;

    public Record(LocalTime startTime,
                  LocalTime endTime,
                  LocalDate startDate,
                  LocalDate endDate,
                  Activity activity) {
        this.activity = activity;
        this.startDateTime = Date.from(LocalDateTime.of(startDate, startTime).atZone(ZoneId.systemDefault()).toInstant());
        this.endDateTime = Date.from(LocalDateTime.of(endDate, endTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Record() {}

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
