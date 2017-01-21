package timelogger.mprtcz.com.timelogger.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.record.service.RecordService;
import timelogger.mprtcz.com.timelogger.task.model.HoursData;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;

/**
 * Created by Azet on 2017-01-20.
 */

public class UiUtils {
    private static final String TAG = "UiUtils";

    public static void messageBox(Context context, String method, String message) {
        Log.d("EXCEPTION: " + method, message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(context);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    public static void displayValidationResult(Activity rootActivity,
                                               ValidationResult result,
                                               String messageBoxHeaderText) {
        if(result.isErrorFree()) {
            Toast toast = Toast.makeText(
                    rootActivity,
                    rootActivity.getResources().getString(result.getCustomErrorEnum().getValue()),
                    Toast.LENGTH_SHORT);
            toast.show();
            rootActivity.finish();
        } else {
            String message = rootActivity.getResources().getString(result.getCustomErrorEnum().getValue());
            messageBox(rootActivity, messageBoxHeaderText, message);
        }
    }

    public static List<Task> getAllTasksFromBackendAsync(Activity activity) {
        final TaskService taskService = TaskService.getInstance(activity);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<Task> returnedTask = null;
        Future<List<Task>> result = executor.submit(new Callable<List<Task>>() {
            public List<Task> call() throws Exception {
                Log.d("Task save", "saving task");
                return taskService.getAllTasks();
            }
        });
        try {
            returnedTask = result.get();
            Log.i(TAG, "getAllTasksFromBackendAsync, returned tasks = " + returnedTask.toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageBox(activity, "Exception", e.toString());
        }
        return returnedTask;
    }

    public static Task getTaskByIdAsync(final Long id, Activity activity) {
        final TaskService taskService = TaskService.getInstance(activity);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Task returnValue = null;
        Future<Task> result;
        result = executor.submit(new Callable<Task>() {
            public Task call() throws Exception {
                Log.d("Task save", "saving task");
                return taskService.getTaskById(id);
            }
        });
        try {
            returnValue = result.get();
            Log.i(TAG, "return value = " + returnValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageBox(activity, "Exception", e.toString());
        }
        return returnValue;
    }

    public static DateTime getRecordsLatestHourAsync(Activity activity) {
        HoursData hoursData = new HoursData(getAllTasksFromBackendAsync(activity));
        return hoursData.getLatest();
    }

    public static ValidationResult saveTaskAsync(final Task newTask, Activity activity) {
        final TaskService taskService = TaskService.getInstance(activity);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ValidationResult returnValue = null;
        Future<ValidationResult> result;
        result = executor.submit(new Callable<ValidationResult>() {
            public ValidationResult call() throws Exception {
                Log.d("Task save", "saving task");
                return taskService.saveTask(newTask);
            }
        });
        try {
            returnValue = result.get();
            Log.i(TAG, "return value = " + returnValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageBox(activity, "Exception", e.toString());
        }
        return returnValue;
    }

    public static ValidationResult updateTaskAsync(final Task updatingTask, Activity activity) {
        final TaskService taskService = TaskService.getInstance(activity);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ValidationResult returnValue = null;
        Future<ValidationResult> result;
        result = executor.submit(new Callable<ValidationResult>() {
            public ValidationResult call() throws Exception {
                Log.d("Task update", "updating task");
                return taskService.updateTask(updatingTask);
            }
        });
        try {
            returnValue = result.get();
            Log.i(TAG, "return value = " + returnValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageBox(activity, "Exception", e.toString());
        }
        return returnValue;
    }

    public static ValidationResult addRecordAsync(final Record record, Activity activity) {
        final RecordService recordService = new RecordService(TaskService.getInstance(activity));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ValidationResult returnValue = null;
        Future<ValidationResult> result;
        result = executor.submit(new Callable<ValidationResult>() {
            public ValidationResult call() throws Exception {
                Log.d("record save", "saving record = " + record.toString());
                return recordService.addRecord(record);
            }
        });
        try {
            returnValue = result.get();
            displayValidationResult(activity, returnValue,
                    activity.getResources().getString(R.string.recordValidationMsgBoxHeader));
            Log.i(TAG, "return value = " + returnValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageBox(activity, "Exception", e.toString());
        }
        return returnValue;
    }
}
