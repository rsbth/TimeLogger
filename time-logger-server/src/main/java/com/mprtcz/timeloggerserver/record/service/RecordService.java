package com.mprtcz.timeloggerserver.record.service;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.model.RecordDto;
import com.mprtcz.timeloggerserver.record.model.converter.RecordEntityDtoConverter;
import com.mprtcz.timeloggerserver.record.repository.CustomRecordRepository;
import com.mprtcz.timeloggerserver.record.repository.RecordRepository;
import com.mprtcz.timeloggerserver.record.validator.RecordValidator;
import com.mprtcz.timeloggerserver.task.model.Task;
import com.mprtcz.timeloggerserver.task.service.TaskService;
import com.mprtcz.timeloggerserver.utils.exceptions.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    public RecordDto saveRecord(RecordDto r) {
        logger.info("RecordDTO to save : {}", r);
        Task associatedTask = this.taskService.getTaskByUuId(r.getTaskUuId());
        logger.info("associatedTask : {}", associatedTask);
        Record record = this.recordEntityDtoConverter.toEntity(r, associatedTask);
        logger.info("Record to save : {}", record);
        RecordValidator recordValidator = new RecordValidator(record, getAllRecords());
        recordValidator.validateNewRecord();
        record.setActive(true);
        logger.info("Record to save : {}", record);
        if (record.getSynchronizationDate() != null) {
            throw new IllegalArgumentException(
                    "Attempting to save a record which already has a synchronization date");
        }
        record.setSynchronizationDate(LocalDateTime.now());
        this.recordRepository.save(record);
        logger.info("Saved record = " + record.toString());
        return this.recordEntityDtoConverter.toDto(record);
    }

    public Iterable<RecordDto> getAllRecordDtos() {
        return this.recordEntityDtoConverter.toDtos(this.recordRepository.findAll());
    }

    private Iterable<Record> getAllRecords() {
        return this.recordRepository.findAll();
    }

    public List<RecordDto> getRecordsByTaskUuId(String taskUuId) {
        this.taskService.checkIfTaskWithUuIdExists(taskUuId);
        Task task = this.taskService.getTaskByUuId(taskUuId);
        return this.recordEntityDtoConverter.toDtos(
                this.customRecordRepository.getRecordsByTaskId(task.getId()));
    }

    public List<RecordDto> getRecordsByTaskId(Long taskId) {
        this.taskService.checkIfTaskWithIdExists(taskId);
        Task task = this.taskService.getTaskById(taskId);
        return this.recordEntityDtoConverter.toDtos(
                this.customRecordRepository.getRecordsByTaskId(taskId));
    }

    public List<RecordDto> getAllRecordsAfterSyncDate(Long dateMilis) {
        Iterable<RecordDto> records = getAllRecordDtos();
        List<RecordDto> recordsAfterDate = new ArrayList<>();
        Date date = new Date(dateMilis);
        for (RecordDto recordDto :
                records) {
            if (recordDto.getSynchronizationDate().after(date)) {
                recordsAfterDate.add(recordDto);
            }
        }
        return recordsAfterDate;
    }

    public void deleteRecord(String uuId) {
        Record record = this.customRecordRepository.getRecordByUuId(uuId);
        recordNullCheck(record);
        record.setActive(false);
        this.recordRepository.save(record);
    }

    private void recordNullCheck(Record record) {
        if (record == null) {
            throw new TaskNotFoundException("Task with this id does not exist");
        }
    }
}
