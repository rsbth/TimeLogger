package timelogger.mprtcz.com.timelogger.record.service;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import timelogger.mprtcz.com.timelogger.record.controller.RecordWebController;
import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.record.validator.RecordValidator;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-20.
 */

public class RecordService {
    private static final String TAG = "RecordService";

    @Getter
    private TaskService taskService;
    @Getter
    private RecordSyncService recordSyncService;

    public RecordService(TaskService taskService) {
        this.taskService = taskService;
        this.recordSyncService = new RecordSyncService(this, new RecordWebController());
    }

    public ValidationResult addRecord(Record record) throws Exception {
        RecordValidator recordValidator = new RecordValidator();
        ValidationResult validationResult = recordValidator.validateNewRecordData(record);
        if(validationResult.isErrorFree()) {
            Task rootTask = this.taskService.getTaskById(record.getTask().getId());
            record.setCreationDate(new Date());
            record.setUuId(UUID.randomUUID().toString());
            record.setActive(true);
            rootTask.addRecord(record);
            this.taskService.updateTask(rootTask, TaskService.UpdateType.LOCAL);
            if(this.recordSyncService != null) {
                this.recordSyncService.postNewRecordToServer(record);
            }
        }
        return validationResult;
    }

    public List<Record> getAllRecords() throws Exception {
        Log.d(TAG, "getAllRecords() called");
        List<Task> tasks = this.taskService.getAllTasks();
        List<Record> allRecords = new ArrayList<>();
        for (Task task : tasks) {
            if(task.getTaskRecords() != null) {
                allRecords.addAll(task.getTaskRecords());
            }
        }
        Log.d(TAG, "getAllRecords() returned: " + allRecords);
        return allRecords;
    }

    public Record findRecordByUuid(String uuId) throws Exception {
        List<Record> records = this.taskService.getCustomDao().getAllRecords();
        for (Record record : records) {
            if (record.getUuId().equals(uuId)) {
                return record;
            }
        }
        return null;
    }

    public void updateRecord(Record record) throws Exception {
        this.taskService.getCustomDao().update(record);
    }

    public static RecordService getInstance(Activity activity) {
        TaskService taskService = TaskService.getInstance(activity);
        RecordService recordService = new RecordService(taskService);
        Log.d(TAG, "getInstance() returned: " + recordService);
        return recordService;
    }
}