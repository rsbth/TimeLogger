package com.mprtcz.timeloggerweb.client.application.record.model;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Date;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class RecordOverlay extends JavaScriptObject {

    protected RecordOverlay() {}

    public final native Long getTaskServerId()  /*-{
        return taskServerId;
    }-*/;

    public final native void setTaskServerId(Long taskServerId)  /*-{
        this.taskServerId = taskServerId;
    }-*/;

    public final native Date getStartDateTime()  /*-{
        return startDateTime;
    }-*/;

    public final native void setStartDateTime(Date startDateTime)  /*-{
        this.startDateTime = startDateTime;
    }-*/;

    public final native Date getEndDateTime()  /*-{
        return endDateTime;
    }-*/;

    public final native void setEndDateTime(Date endDateTime)  /*-{
        this.endDateTime = endDateTime;
    }-*/;

    public final native Date getCreationDate()  /*-{
        return creationDate;
    }-*/;

    public final native void setCreationDate(Date creationDate)  /*-{
        this.creationDate = creationDate;
    }-*/;

    public final native Date getSynchronizationDate()  /*-{
        return synchronizationDate;
    }-*/;

    public final native void setSynchronizationDate(Date synchronizationDate)  /*-{
        this.synchronizationDate = synchronizationDate;
    }-*/;

    public final native String getUuId()  /*-{
        return uuId;
    }-*/;

    public final native void setUuId(String uuId)  /*-{
        this.uuId = uuId;
    }-*/;

    public final native boolean isActive()  /*-{
        return active;
    }-*/;

    public final native void setActive(boolean active)  /*-{
        this.active = active;
    }-*/;

    public static native RecordOverlay buildTaskOverlay(String json) /*-{
        return eval('(' + json + ')');
    }-*/;

    public static native RecordArray<RecordOverlay> buildTasksArray(String json)  /*-{
        return eval('(' + json + ')');
    }-*/;
}