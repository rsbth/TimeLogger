package com.mprtcz.timeloggerdesktop.backend.record.model.converter;

import com.mprtcz.timeloggerdesktop.backend.activity.model.Activity;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.backend.record.model.RecordDto;

import java.util.Date;
import java.util.Objects;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class RecordConverter {

    public static Record toEntity(RecordDto recordDto, Activity activity) {
        Record record = new Record();
        if (Objects.equals(activity.getServerId(), recordDto.getTaskServerId())) {
            record.setActivity(activity);
        }
        record.setStartDateTime(recordDto.getStartDateTime());
        record.setEndDateTime(recordDto.getEndDateTime());
        record.setSynchronizationDate(recordDto.getSynchronizationDate());
        record.setCreationDate(recordDto.getCreationDate());
        record.setActive(recordDto.isActive());
        record.setUuId(recordDto.getUuId());
        return record;
    }

    public static RecordDto toDto(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setTaskServerId(record.getActivity().getServerId()); //translating id to global uuid
        recordDto.setStartDateTime(record.getStartDateTime());
        recordDto.setEndDateTime(record.getEndDateTime());
        recordDto.setCreationDate(record.getCreationDate());
        recordDto.setUuId(record.getUuId());
        recordDto.setActive(record.isActive());
        return recordDto;
    }

    public static void main(String[] args) {
        long longDate = 1485687120663L;
        Date date  = new Date(longDate);
        System.out.println("date = " + date);
    }
}
