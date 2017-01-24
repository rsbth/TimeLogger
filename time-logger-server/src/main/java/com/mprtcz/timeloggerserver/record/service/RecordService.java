package com.mprtcz.timeloggerserver.record.service;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.record.model.converter.RecordEntityDtoConverter;
import com.mprtcz.timeloggerserver.record.repository.CustomRecordRepository;
import com.mprtcz.timeloggerserver.record.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Service
public class RecordService {

    private final
    RecordEntityDtoConverter recordEntityDtoConverter;

    private final
    RecordRepository recordRepository;

    private final
    CustomRecordRepository customRecordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository,
                         CustomRecordRepository customRecordRepository,
                         RecordEntityDtoConverter recordEntityDtoConverter) {
        this.recordRepository = recordRepository;
        this.customRecordRepository = customRecordRepository;
        this.recordEntityDtoConverter = recordEntityDtoConverter;
    }

    public void saveRecord(Record r) {
        this.recordRepository.save(r);
    }

    public Iterable<RecordDto> getAllRecords() {
        return this.recordEntityDtoConverter.toDtos(this.recordRepository.findAll());
    }

    public List<RecordDto> getRecordsByTaskId(Long taskId) {
        return this.recordEntityDtoConverter.toDtos(this.customRecordRepository.getRecordsByTaskId(taskId));
    }
}
