package com.mprtcz.timeloggerserver.record.model.converter;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.task.model.converter.TaskEntityDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Component
public class RecordEntityDtoConverterImpl implements RecordEntityDtoConverter {

    private final
    TaskEntityDtoConverter taskEntityDtoConverter;

    @Autowired
    public RecordEntityDtoConverterImpl(TaskEntityDtoConverter taskEntityDtoConverter) {
        this.taskEntityDtoConverter = taskEntityDtoConverter;
    }

    @Override
    public Record toEntity(RecordDto recordDto) {
        Record record = new Record();
        record.setTask(taskEntityDtoConverter.toEntity(recordDto.getTaskDto()));
        record.setEndDateTime(toLocalDateTime(recordDto.getEndDateTime()));
        record.setStartDateTime(toLocalDateTime(recordDto.getStartDateTime()));
        if(record.getCreationDate() != null) {
            record.setCreationDate(toLocalDateTime(recordDto.getCreationDate()));
        }
        return null;
    }

    @Override
    public RecordDto toDto(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setTaskDto(taskEntityDtoConverter.toDto(record.getTask()));
        recordDto.setCreationDate(toDate(record.getCreationDate()));
        recordDto.setStartDateTime(toDate(record.getStartDateTime()));
        recordDto.setEndDateTime(toDate(record.getEndDateTime()));
        recordDto.setSynchronizationDate(toDate(record.getSynchronizationDate()));
        return recordDto;
    }

    @Override
    public List<Record> toEntities(List<RecordDto> dtos) {
        List<Record> records = new ArrayList<>();
        for (RecordDto recordDto :
                dtos) {
            records.add(toEntity(recordDto));
        }
        return records;
    }

    @Override
    public List<RecordDto> toDtos(Iterable<Record> entities) {
        List<RecordDto> recordDtos = new ArrayList<>();
        for (Record record :
                entities) {
            recordDtos.add(toDto(record));
        }
        return recordDtos;
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
