package com.mprtcz.timeloggerserver.record.model.converter;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.task.model.Task;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
public interface RecordEntityDtoConverter {

    Record toEntity(RecordDto recordDto, Task task);

    RecordDto toDto(Record record);

    List<RecordDto> toDtos(Iterable<Record> entities);
}
