package com.mprtcz.timeloggerweb.client.application.record.model;

import java.util.Date;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class RecordDto {
    private Long taskServerId;

    private Date startDateTime;

    private Date endDateTime;

    private Date creationDate;

    private Date synchronizationDate;

    private String uuId;

    private boolean active;

    public Long getTaskServerId() {
        return taskServerId;
    }

    public void setTaskServerId(Long taskServerId) {
        this.taskServerId = taskServerId;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getSynchronizationDate() {
        return synchronizationDate;
    }

    public void setSynchronizationDate(Date synchronizationDate) {
        this.synchronizationDate = synchronizationDate;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
