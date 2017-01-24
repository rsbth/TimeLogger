package com.mprtcz.timeloggerserver.record.model.converter;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
public interface RecordEntityDtoConverter {

    Record toEntity(RecordDto recordDto);

    RecordDto toDto(Record record);

    List<Record> toEntities(List<RecordDto> dtos);

    List<RecordDto> toDtos(Iterable<Record> entities);

}
