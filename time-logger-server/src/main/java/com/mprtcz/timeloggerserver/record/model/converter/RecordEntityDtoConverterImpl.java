package com.mprtcz.timeloggerserver.record.model.converter;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.service.TaskService;
import com.mprtcz.timeloggerserver.utils.DateTimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        record.setEndDateTime(DateTimeConverter.toLocalDateTimeWithZeroMinutes(recordDto.getEndDateTime()));
        record.setStartDateTime(DateTimeConverter.toLocalDateTimeWithZeroMinutes(recordDto.getStartDateTime()));
        if(recordDto.getCreationDate() != null) {
            record.setCreationDate(DateTimeConverter.toLocalDateTimeWithZeroMinutes(recordDto.getCreationDate()));
        }
        record.setSynchronizationDate(LocalDateTime.now());
        return record;
    }

    @Override
    public RecordDto toDto(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setTaskID(record.getTask().getId());
        recordDto.setCreationDate(DateTimeConverter.toDate(record.getCreationDate()));
        recordDto.setStartDateTime(DateTimeConverter.toDate(record.getStartDateTime()));
        recordDto.setEndDateTime(DateTimeConverter.toDate(record.getEndDateTime()));
        recordDto.setSynchronizationDate(DateTimeConverter.toDate(record.getSynchronizationDate()));
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

    
}
