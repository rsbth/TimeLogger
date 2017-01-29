package com.mprtcz.timeloggerserver.record.model.converter;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.utils.DateTimeConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Component
public class RecordEntityDtoConverterImpl implements RecordEntityDtoConverter {

    @Override
    public Record toEntity(RecordDto recordDto, Task task) {
        Record record = new Record();
        record.setTask(task);
        record.setEndDateTime(DateTimeConverter.toLocalDateTimeWithZeroMinutes(recordDto.getEndDateTime()));
        record.setStartDateTime(DateTimeConverter.toLocalDateTimeWithZeroMinutes(recordDto.getStartDateTime()));
        if (recordDto.getCreationDate() != null) {
            record.setCreationDate(DateTimeConverter.toLocalDateTime(recordDto.getCreationDate()));
        }
        if (recordDto.getSynchronizationDate() != null) {
            record.setSynchronizationDate(DateTimeConverter.toLocalDateTime(recordDto.getSynchronizationDate()));
        }
        record.setUuId(recordDto.getUuId());
        return record;
    }

    @Override
    public RecordDto toDto(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setCreationDate(DateTimeConverter.toDate(record.getCreationDate()));
        recordDto.setStartDateTime(DateTimeConverter.toDate(record.getStartDateTime()));
        recordDto.setEndDateTime(DateTimeConverter.toDate(record.getEndDateTime()));
        recordDto.setSynchronizationDate(DateTimeConverter.toDate(record.getSynchronizationDate()));
        recordDto.setActive(record.isActive());
        recordDto.setUuId(record.getUuId());
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