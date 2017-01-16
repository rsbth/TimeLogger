package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import javafx.beans.value.ChangeListener;

/**
 * Created by mprtcz on 2017-01-16.
 */
public interface MainController {

    void showAlertDialog(String message);

    void displayValidationResult(ValidationResult result);

    void exportData();

    void openFileChooser();

    ChangeListener<Throwable> getTaskExceptionListener();

    ChangeListener<Activity> getListViewChangeListener();
}
