package com.mprtcz.timeloggerserver.record.service;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.record.model.converter.RecordEntityDtoConverter;
import com.mprtcz.timeloggerserver.record.repository.CustomRecordRepository;
import com.mprtcz.timeloggerserver.record.repository.RecordRepository;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Service
public class RecordService {
    private static final Logger logger = LoggerFactory.getLogger(RecordService.class);

    private final
    RecordEntityDtoConverter recordEntityDtoConverter;

    private final
    TaskService taskService;

    private final
    RecordRepository recordRepository;

    private final
    CustomRecordRepository customRecordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository,
                         CustomRecordRepository customRecordRepository,
                         RecordEntityDtoConverter recordEntityDtoConverter, TaskService taskService) {
        this.recordRepository = recordRepository;
        this.customRecordRepository = customRecordRepository;
        this.recordEntityDtoConverter = recordEntityDtoConverter;
        this.taskService = taskService;
    }

    public void saveRecord(RecordDto r) {
        logger.info("RecordDTO to save : {}", r);
        Task associatedTask = this.taskService.getTaskById(r.getTaskID());
        logger.info("associatedTask : {}", associatedTask);
        Record record = this.recordEntityDtoConverter.toEntity(r, associatedTask);
        logger.info("Record to save : {}", record);
        this.recordRepository.save(record);
    }

    public Iterable<RecordDto> getAllRecords() {
        return this.recordEntityDtoConverter.toDtos(this.recordRepository.findAll());
    }

    public List<RecordDto> getRecordsByTaskId(Long taskId) {
        return this.recordEntityDtoConverter.toDtos(this.customRecordRepository.getRecordsByTaskId(taskId));
    }
}
