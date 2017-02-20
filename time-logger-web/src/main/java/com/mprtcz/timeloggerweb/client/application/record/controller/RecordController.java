package com.mprtcz.timeloggerweb.client.application.record.controller;

import com.mprtcz.timeloggerweb.client.application.record.model.RecordDto;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskOverlay;

import java.util.Date;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class RecordController {

    public void createRecordInstance(Date startDateTime, Date endDateTime, TaskOverlay taskOverlay) {
        RecordDto recordDto = new RecordDto();
        recordDto.setCreationDate(new Date());
        recordDto.setActive(true);
        recordDto.setStartDateTime(startDateTime);
        recordDto.setEndDateTime(endDateTime);
        recordDto.setTaskServerId(taskOverlay.getServerId());
    }
}
