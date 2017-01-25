package timelogger.mprtcz.com.timelogger.task.validator;

import java.util.List;

import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

import static timelogger.mprtcz.com.timelogger.utils.ValidationResult.CustomErrorEnum.TASK_SAVED;

/**
 * Created by Azet on 2017-01-19.
 */

public class TaskValidator {

    private ValidationResult validationResult;

    public ValidationResult validateNewTask(Task newTask, List<Task> existingTasksList) {
        this.validationResult = new ValidationResult(TASK_SAVED);
        if(newTask.getName().equals("")) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.TASK_NAME_EMPTY);
        }
        if(newTask.getColor().equals("")) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.TASK_COLOR_EMPTY);
        }
        checkIfTaskNameExists(newTask.getName(), existingTasksList);
        return this.validationResult;
    }

    public ValidationResult validateUpdatedTask(Task task) {
        this.validationResult = new ValidationResult(ValidationResult.CustomErrorEnum.TASK_SAVED);
        if(task.getName().equals("") || task.getName() ==null ) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.TASK_NAME_EMPTY);
        }
        if(task.getColor().equals("") || task.getColor() ==null ) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.TASK_COLOR_EMPTY);
        }
        if(task.getId() < 1 || task.getId() ==null ) {
            this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.TASK_ID_EMPTY);
        }
        return this.validationResult;
    }

    private void checkIfTaskNameExists(String name, List<Task> activities) {
        for (Task task :
                activities) {
            if (task.getName().equals(name)) {
                this.validationResult.getNewErrorEnum(ValidationResult.CustomErrorEnum.TASK_EXISTS);
                break;
            }
        }
    }
}
