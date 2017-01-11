package com.mprtcz.timeloggerdesktop.frontend.utils;

import com.mprtcz.timeloggerdesktop.backend.utilities.ValidationResult;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Created by mprtcz on 2017-01-11.
 */
public interface MyEventHandler<T extends Event> extends EventHandler{
    void setResult(ValidationResult result);
    ValidationResult getResult();
}
