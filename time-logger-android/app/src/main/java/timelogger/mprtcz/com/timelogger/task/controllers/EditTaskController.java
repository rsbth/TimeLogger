package timelogger.mprtcz.com.timelogger.task.controllers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-19.
 */

public class EditTaskController extends AddTaskController {

    private Button pickColorButton;
    private static final String TAG = "EditTaskActivity";
    public static final String EDITED_ACTIVITY_ID = "EDITED_ACTIVITY_ID";
    private Task updatingTask;

    public EditTaskController(EditText nameEditText, EditText descriptionEditText,
                              Activity activity, Long editedTaskID, Button pickColorButton) {
        super(nameEditText, descriptionEditText, activity);
        this.pickColorButton = pickColorButton;
        this.populateFieldsWithEditData();
    }

    private void populateFieldsWithEditData() {
        final Long taskId = baseActivity.getIntent().getLongExtra(EDITED_ACTIVITY_ID, -1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Task returnedTask = null;
        Future<Task> result = executor.submit(new Callable<Task>() {
            public Task call() throws Exception {
                Log.d("Task save", "saving task");
                return taskService.getTaskById(taskId);
            }
        });
        try {
            returnedTask = result.get();
            Log.i(TAG, "returned task = " + returnedTask.toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageBox("Exception", e.toString());
        }
        if (returnedTask != null) {
            Log.i(TAG, "returned task's name = " + returnedTask.getName());
            this.updatingTask = returnedTask;
            nameEditText.setText(returnedTask.getName());
            descriptionEditText.setText(returnedTask.getDescription());
            this.pickColorButton.setBackgroundColor(Color.parseColor(returnedTask.getColor()));
            stringColor = returnedTask.getColor();
        }
    }

    @Override
    public ColorPicker getColorPicker(final Activity activity) {
        ColorDrawable buttonColor = (ColorDrawable) pickColorButton.getBackground();
            int colorId = buttonColor.getColor();
            return new ColorPicker(activity, Color.red(colorId), Color.green(colorId) , Color.blue(colorId));
    }

    @Override
    public void displayErrorIfNameExists() {}

    @Override
    public ValidationResult getTaskValidationResult() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ValidationResult returnValue = null;
        Future<ValidationResult> result;
        this.updatingTask.setName(name);
        this.updatingTask.setDescription(description);
        this.updatingTask.setColor(this.stringColor);
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
            messageBox("Exception", e.toString());
        }
        return returnValue;
    }
}
