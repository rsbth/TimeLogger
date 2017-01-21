package timelogger.mprtcz.com.timelogger.task.controllers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

/**
 * Created by Azet on 2017-01-19.
 */

public class EditTaskController extends AddTaskController {

    private Button pickColorButton;
    private static final String TAG = "EditTaskActivity";
    public static final String EDITED_ACTIVITY_ID = "EDITED_TASK_ID";
    private Task updatingTask;

    public EditTaskController(EditText nameEditText, EditText descriptionEditText,
                              Activity activity, Button pickColorButton) {
        super(nameEditText, descriptionEditText, activity);
        this.pickColorButton = pickColorButton;
        Button confirmButton = (Button) activity.findViewById(R.id.addTaskConfButton);
        confirmButton.setText(activity.getResources().getText(R.string.updateTaskButtonText));
        this.populateFieldsWithEditData();
    }

    private void populateFieldsWithEditData() {
        final Long taskId = baseActivity.getIntent().getLongExtra(EDITED_ACTIVITY_ID, -1);
        Task returnedTask = UiUtils.getTaskByIdAsync(taskId, baseActivity);

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
        this.updatingTask.setName(name);
        this.updatingTask.setDescription(description);
        this.updatingTask.setColor(this.stringColor);
        return UiUtils.updateTaskAsync(this.updatingTask, baseActivity);
    }
}
