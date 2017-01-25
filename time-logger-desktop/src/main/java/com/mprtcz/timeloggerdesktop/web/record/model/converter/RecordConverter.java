package com.mprtcz.timeloggerdesktop.web.record.model.converter;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.web.record.model.RecordDto;

import java.util.Date;
import java.util.Objects;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class RecordConverter {

    public Record toEntity(RecordDto recordDto, Activity activity) {
        Record record = new Record();
        if (Objects.equals(activity.getId(), recordDto.getTaskID())) {
            record.setActivity(activity);
        }
        record.setStartDateTime(recordDto.getStartDateTime());
        record.setEndDateTime(recordDto.getEndDateTime());
        return record;
    }

    public RecordDto toDto(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setTaskID(record.getActivity().getId());
        recordDto.setStartDateTime(record.getStartDateTime());
        recordDto.setEndDateTime(record.getEndDateTime());
        recordDto.setCreationDate(new Date());
        return recordDto;
    }
}
