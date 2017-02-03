package timelogger.mprtcz.com.timelogger.task.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.Date;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;

import static timelogger.mprtcz.com.timelogger.utils.UiUtils.displayValidationResult;
import static timelogger.mprtcz.com.timelogger.utils.UiUtils.messageBox;

/**
 * Created by Azet on 2017-01-19.
 */

public class AddTaskController {
    EditText nameEditText;
    EditText descriptionEditText;
    private Context context;
    TaskService taskService;
    Activity baseActivity;
    String stringColor = "";
    private int selectedColorRGB = 0;
    private static final String TAG = "AddTaskActivity";

    public AddTaskController(EditText nameEditText, EditText descriptionEditText, Activity activity) {
        this.nameEditText = nameEditText;
        this.descriptionEditText = descriptionEditText;
        this.context = activity.getApplicationContext();
        this.baseActivity = activity;
        this.setTaskService();
        this.setEditTextNameFocusListener();
    }

    private void setEditTextNameFocusListener() {
        this.nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            boolean isNameUnique = true;

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isNameUnique = taskService.isNameUnique(nameEditText.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogWrapper.e(TAG, "Exception while name uniqeness check: " +e.toString());
                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast exceptionToast = Toast.makeText(
                            context, e.toString(), Toast.LENGTH_SHORT);
                    exceptionToast.show();
                }
                if (!isNameUnique) {
                    displayErrorIfNameExists();
                }
            }
        });
    }

    private void setTaskService() {
        Thread taskServiceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                taskService = TaskService.getInstance(baseActivity);
            }
        });
        taskServiceThread.start();
        try {
            taskServiceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            messageBox(baseActivity.getApplicationContext(), "Exception", e.toString());
        }
    }

    public void onPickColorButtonClicked() {
        final ColorPicker cp = getColorPicker(baseActivity);
        cp.show();
        Button okColor = (Button) cp.findViewById(R.id.okColorButton);
        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColorRGB = cp.getColor();
                LogWrapper.i(TAG, "selectedColorRGB = " + selectedColorRGB);

                Button colorButton = (Button) baseActivity.findViewById(R.id.pickColorButton);

                colorButton.setBackgroundColor(selectedColorRGB);
                stringColor = "#" + String.format("%02x%02x%02x", cp.getRed(), cp.getGreen(), cp.getBlue());
                cp.dismiss();
            }
        });
    }

    public ColorPicker getColorPicker(final Activity activity) {
        return new ColorPicker(activity);
    }

    public void displayErrorIfNameExists() {
        nameEditText.setError(this.context.getResources().getString(R.string.taskNameExists));
    }

    String name;
    String description;

    public boolean onAddTaskButtonClicked() {
        this.name = nameEditText.getText().toString();
        this.description = descriptionEditText.getText().toString();
        if (name.equals("")) {
            Toast toast = Toast.makeText(
                    context, this.context.getResources().getString(R.string.name_cannot_be_null), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (stringColor.equals("")) {
            Toast toast = Toast.makeText(
                    this.context, this.context.getResources().getString(R.string.pickColorToast), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (description.equals("")) {
            final boolean[] valueToReturn = new boolean[1];
            valueToReturn[0] = false;
            new AlertDialog.Builder(this.baseActivity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(this.context.getResources().getString(R.string.emptyDescriptionPopupTitle))
                    .setMessage(this.context.getResources().getString(R.string.emptyDescriptionConfirmationLabel))
                    .setPositiveButton(this.context.getResources().getString(
                            R.string.yesButtonText),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    valueToReturn[0] = saveOrUpdateNewTask();
                                }
                            })
                    .setNegativeButton(this.context.getResources().getString(R.string.noButtonText), null)
                    .show();
            return valueToReturn[0];
        } else {
            return this.saveOrUpdateNewTask();
        }
    }

    private boolean saveOrUpdateNewTask() {
        ValidationResult returnValue = getTaskValidationResult();
        displayValidationResult(this.baseActivity, returnValue, getTaskServiceSuccessMessage());
        return returnValue.isErrorFree();
    }

    private String getTaskServiceSuccessMessage() {
        return context.getResources().getString(R.string.taskSavedToast);
    }

    public ValidationResult getTaskValidationResult() {
        final Task newTask = new Task(this.name,
                this.description, this.stringColor);
        newTask.setLastModified(new Date());
        newTask.setActive(true);
        return UiUtils.saveTaskAsync(newTask, baseActivity);
    }
}
