package com.mprtcz.timeloggerweb.client.application.record.validator;

import com.google.gwt.core.client.GWT;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskOverlay;
import com.mprtcz.timeloggerweb.client.uielements.DateTimePicker;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class RecordDataValidator {

    public static final String VALUE_MISSING = "Missed a value here";
    public static final String END_DATE_BEFORE_START_DATE = "End date before start date";
    public static final String END_TIME_BEFORE_START_TIME = "End time before start time";
    public static final String END_START_DATE_EQUAL = "End date and start date cannot be the same";
    public static final String END_START_TIME_EQUAL = "End time and start time cannot be the same";

    public static boolean validateRecordData(DateTimePicker startDateTimePicker,
                                             DateTimePicker endDateTimePicker,
                                             TaskOverlay taskOverlay) {
        startDateTimePicker.getTimePicker().clearErrorOrSuccess();
        startDateTimePicker.getDatePicker().clearErrorOrSuccess();
        endDateTimePicker.getTimePicker().clearErrorOrSuccess();
        endDateTimePicker.getDatePicker().clearErrorOrSuccess();

        if (startDateTimePicker.getSelectedValues() == null || endDateTimePicker.getSelectedValues() == null) {
            if(startDateTimePicker.getTimePicker().getValue() == null) {
                startDateTimePicker.getTimePicker().setError(VALUE_MISSING);
            }
            if(startDateTimePicker.getDatePicker().getValue() == null) {
                startDateTimePicker.getDatePicker().setError(VALUE_MISSING);
            }
            if(endDateTimePicker.getTimePicker().getValue() == null) {
                endDateTimePicker.getTimePicker().setError(VALUE_MISSING);
            }
            if(endDateTimePicker.getDatePicker().getValue() == null) {
                endDateTimePicker.getDatePicker().setError(VALUE_MISSING);
            }
            return false;
        }

        if(startDateTimePicker.getSelectedValues().after(endDateTimePicker.getSelectedValues())) {
            endDateTimePicker.getDatePicker().setError(END_DATE_BEFORE_START_DATE);
            endDateTimePicker.getTimePicker().setError(END_TIME_BEFORE_START_TIME);
            return false;
        }

        if(startDateTimePicker.getSelectedValues().equals(endDateTimePicker.getSelectedValues())) {
            endDateTimePicker.getDatePicker().setError(END_START_DATE_EQUAL);
            endDateTimePicker.getTimePicker().setError(END_START_TIME_EQUAL);
            return false;
        }

        if(taskOverlay == null) {
            GWT.log("taskOverlay somehow appears to be null");
        }

        return true;
    }
}
