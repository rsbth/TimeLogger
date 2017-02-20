package com.mprtcz.timeloggerweb.client.application.task.model;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.Date;

/**
 * Created by mprtcz on 2017-02-15.
 */
public class TaskOverlay extends JavaScriptObject {
    protected TaskOverlay() {}
    public final native Long getServerId()  /*-{
        return this.serverId;
    }-*/;

    public final native void setServerId(Long serverId) /*-{
        this.serverId = serverId;
    }-*/;

    public final native String getName()  /*-{
        return this.name;
    }-*/;

    public final native void setName(String name)  /*-{
        this.name = name;
    }-*/;

    public final native String getDescription()  /*-{
        return this.description;
    }-*/;

    public final native void setDescription(String description)  /*-{
        this.description = description;
    }-*/;

    public final native String getColor()  /*-{
        return this.color;
    }-*/;

    public final native void setColor(String color) /*-{
        this.color = color;
    }-*/;

    public final native Date getLastModified()  /*-{
        return this.lastModified;
    }-*/;

    public final native void setLastModified(Date lastModified) /*-{
        this.lastModified = lastModified;
    }-*/;

    public final native boolean isActive()  /*-{
        return this.active;
    }-*/;

    public final native void setActive(boolean active) /*-{
        this.active = active;
    }-*/;

    public static native TaskOverlay buildTaskOverlay(String json) /*-{
        return eval('(' + json + ')');
    }-*/;

    public static native TaskArray<TaskOverlay> buildTasksArray(String json)  /*-{
        return eval('(' + json + ')');
    }-*/;
}
