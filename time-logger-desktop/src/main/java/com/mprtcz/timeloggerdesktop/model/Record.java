package com.mprtcz.timeloggerdesktop.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
public class Record {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;

    public Record(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
