package timelogger.mprtcz.com.timelogger.record.service;

import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.record.validator.RecordValidator;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-20.
 */

public class RecordService {

    private TaskService taskService;

    public RecordService(TaskService taskService) {
        this.taskService = taskService;
    }

    public ValidationResult addRecord(Record record) throws Exception {
        RecordValidator recordValidator = new RecordValidator();
        ValidationResult validationResult = recordValidator.validateNewRecordData(record);
        if(validationResult.isErrorFree()) {
            Task rootTask = this.taskService.getTaskById(record.getTask().getId());
            rootTask.addRecord(record);
            this.taskService.updateTask(rootTask);
        }
        return validationResult;
    }
}
