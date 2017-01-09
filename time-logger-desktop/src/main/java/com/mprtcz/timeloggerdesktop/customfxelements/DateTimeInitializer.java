package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXDatePicker;
import javafx.event.EventHandler;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
class DateTimeInitializer {
    List<JFXDatePicker> itemsList = new ArrayList<>();

    void initializeElements(EventHandler eventHandler, LocalDateTime latestRecord) {
        for (JFXDatePicker datepicker : itemsList) {
            datepicker.setOnAction(eventHandler);
            datepicker.setOnMouseClicked(eventHandler);
            datepicker.setOnHiding(eventHandler);
            datepicker.setTime((LocalTime.of(0, 0)));
            datepicker.setValue(LocalDate.now());
            System.out.println("latestRecord = " + latestRecord);
            if(latestRecord != null) {
                datepicker.setTime(latestRecord.toLocalTime());
                datepicker.setValue(latestRecord.toLocalDate());
            }
        }
        Locale.setDefault(Locale.ENGLISH);
    }
}
