package com.mprtcz.timeloggerweb.client.application.record.validator;

import com.google.gwt.core.client.GWT;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskOverlay;
import com.mprtcz.timeloggerweb.client.uielements.DateTimePicker;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class RecordDataValidator {

    public static boolean validateRecordData(DateTimePicker startDateTimePicker,
                                             DateTimePicker endDateTimePicker,
                                             TaskOverlay taskOverlay) {
        startDateTimePicker.getTimePicker().clearErrorOrSuccess();
        startDateTimePicker.getDatePicker().clearErrorOrSuccess();
        endDateTimePicker.getTimePicker().clearErrorOrSuccess();
        endDateTimePicker.getDatePicker().clearErrorOrSuccess();

        if (startDateTimePicker.getSelectedValues() == null || endDateTimePicker.getSelectedValues() == null) {
            if(startDateTimePicker.getTimePicker().getValue() == null) {
                startDateTimePicker.getTimePicker().setError("Missed a value here");
            }
            if(startDateTimePicker.getDatePicker().getValue() == null) {
                startDateTimePicker.getDatePicker().setError("Missed a value here");
            }
            if(endDateTimePicker.getTimePicker().getValue() == null) {
                endDateTimePicker.getTimePicker().setError("Missed a value here");
            }
            if(endDateTimePicker.getDatePicker().getValue() == null) {
                endDateTimePicker.getDatePicker().setError("Missed a value here");
            }
            return false;
        }

        if(startDateTimePicker.getSelectedValues().after(endDateTimePicker.getSelectedValues())) {
            endDateTimePicker.getDatePicker().setError("End date before start date");
            endDateTimePicker.getTimePicker().setError("End time before start time");
            return false;
        }

        if(startDateTimePicker.getSelectedValues().equals(endDateTimePicker.getSelectedValues())) {
            endDateTimePicker.getDatePicker().setError("End date and start date cannot be the same");
            endDateTimePicker.getTimePicker().setError("End time and start time cannot be the same");
            return false;
        }

        if(taskOverlay == null) {
            GWT.log("taskOverlay somehow appears to be null");
        }

        return true;
    }
}
