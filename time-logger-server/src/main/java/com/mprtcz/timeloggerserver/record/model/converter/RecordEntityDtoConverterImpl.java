package com.mprtcz.timeloggerserver.record.model.converter;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.service.TaskService;
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
    TaskService taskService;

    @Autowired
    public RecordEntityDtoConverterImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Record toEntity(RecordDto recordDto, Task task) {
        Record record = new Record();
        record.setTask(task);
        record.setEndDateTime(toLocalDateTime(recordDto.getEndDateTime()));
        record.setStartDateTime(toLocalDateTime(recordDto.getStartDateTime()));
        if(recordDto.getCreationDate() != null) {
            record.setCreationDate(toLocalDateTime(recordDto.getCreationDate()));
        }
        record.setSynchronizationDate(LocalDateTime.now());
        return record;
    }

    @Override
    public RecordDto toDto(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setTaskID(record.getTask().getId());
        recordDto.setCreationDate(toDate(record.getCreationDate()));
        recordDto.setStartDateTime(toDate(record.getStartDateTime()));
        recordDto.setEndDateTime(toDate(record.getEndDateTime()));
        recordDto.setSynchronizationDate(toDate(record.getSynchronizationDate()));
        return recordDto;
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
