package com.mprtcz.timeloggerdesktop.customfxelements;

import com.jfoenix.controls.JFXDatePicker;
import javafx.event.EventHandler;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mprtcz on 2017-01-06.
 */
@Getter
public class DateTimeInitializer {
    List<JFXDatePicker> itemsList = new ArrayList<>();

    public void initializeElements(EventHandler eventHandler) {
        for (JFXDatePicker datepicker : itemsList) {
            datepicker.setOnAction(eventHandler);
            datepicker.setOnMouseClicked(eventHandler);
            datepicker.setOnHiding(eventHandler);
            datepicker.setTime((LocalTime.of(0, 0)));
            datepicker.setValue(LocalDate.now());
        }
        Locale.setDefault(Locale.ENGLISH);
    }
}
