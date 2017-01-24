package com.mprtcz.timeloggerserver.task.validator;

import com.mprtcz.timeloggerserver.task.model.Task;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class TaskValidator {

    public static void validateNewTask(Task task, Iterable<Task> allTasks) {
        for (Task t :
                allTasks) {
            if (t.getName().equals(task.getName())) {
                throw new IllegalArgumentException("Task with this name already exists");
            }
        }

    }
}
