package com.mprtcz.timeloggerdesktop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@ToString
@DatabaseTable(tableName = "records")
public class Record {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false)
    private LocalTime startTime;
    @DatabaseField(canBeNull = false)
    private LocalTime endTime;
    @DatabaseField(canBeNull = false)
    private LocalDate startDate;
    @DatabaseField(canBeNull = false)
    private LocalDate endDate;
    @DatabaseField(canBeNull = false, foreign = true)
    private Activity activity;

    public Record(LocalTime startTime,
                  LocalTime endTime,
                  LocalDate startDate,
                  LocalDate endDate,
                  Activity activity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activity = activity;
    }
}
