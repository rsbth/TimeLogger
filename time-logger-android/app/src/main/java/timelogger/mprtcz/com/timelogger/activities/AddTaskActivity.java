package timelogger.mprtcz.com.timelogger.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.task.dao.InMemoryDao;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.utils.ValidationResult;


public class AddTaskActivity extends Activity {
    String stringColor = "";
    int selectedColorRGB = 0;
    String name = "";
    String description = "";
    Task updatingTask;
    TaskService taskService;
    EditText nameEditText;
    EditText descriptionEditText;
    Button pickColorButton;
    private static boolean isEditing = false;
    public static final String TAG = "AddTaskActivity";
    public static final String EDITED_ACTIVITY_ID = "EDITED_ACTIVITY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("AddTaskActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        this.nameEditText = (EditText) findViewById(R.id.taskNameEditText);
        this.pickColorButton = (Button) findViewById(R.id.pickColorButton);
        this.descriptionEditText = (EditText) findViewById(R.id.taskDescriptionEditText);

        this.setTaskService();
        if (getIntent().getExtras() != null) {
            isEditing = true;
            this.populateFieldsWithEditData();
        } else {
            isEditing = false;
        }
        this.nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            boolean isNameUnique = true;

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isNameUnique = taskService.isNameUnique(nameEditText.getText().toString());
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast exceptionToast = Toast.makeText(
                            AddTaskActivity.this, e.toString(), Toast.LENGTH_SHORT);
                    exceptionToast.show();
                }
                if (!isNameUnique && !isEditing) {
                    nameEditText.setError(getResources().getString(R.string.taskNameExists));
                }
            }
        });
    }

    private void setTaskService() {
        Thread taskServiceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                taskService = new TaskService(new InMemoryDao());
            }
        });
        taskServiceThread.start();
        try {
            taskServiceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            messageBox("Exception", e.toString());
        }
    }

    private void populateFieldsWithEditData() {
        final Long taskId = getIntent().getLongExtra(EDITED_ACTIVITY_ID, -1);
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
            this.nameEditText.setText(returnedTask.getName());
            this.descriptionEditText.setText(returnedTask.getDescription());
            this.pickColorButton.setBackgroundColor(Color.parseColor(returnedTask.getColor()));
            this.stringColor = returnedTask.getColor();
        }
    }

    public void onAddTaskButtonClicked(View view) {
        this.name = nameEditText.getText().toString();
        this.description = descriptionEditText.getText().toString();
        if (name.equals("")) {
            Toast toast = Toast.makeText(
                    this, getResources().getString(R.string.name_cannot_be_null), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (stringColor.equals("")) {
            Toast toast = Toast.makeText(
                    this, getResources().getString(R.string.pickColorToast), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (description.equals("")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.emptyDescriptionPopupTitle))
                    .setMessage(getResources().getString(R.string.emptyDescriptionConfirmationLabel))
                    .setPositiveButton(getResources().getString(
                            R.string.yesButtonText),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveOrUpdateNewTask();
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.noButtonText), null)
                    .show();
        } else {
            this.saveOrUpdateNewTask();
        }
    }

    private void saveOrUpdateNewTask() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ValidationResult returnValue = null;
        Future<ValidationResult> result;
        if(isEditing) {
            this.updatingTask.setName(this.name);
            this.updatingTask.setDescription(this.description);
            this.updatingTask.setColor(this.stringColor);
            result = executor.submit(new Callable<ValidationResult>() {
                public ValidationResult call() throws Exception {
                    Log.d("Task update", "updating task");
                    return taskService.updateTask(updatingTask);
                }
            });
        } else {
            final Task newTask = new Task(this.name,
                    this.description, this.stringColor);
            result = executor.submit(new Callable<ValidationResult>() {
                public ValidationResult call() throws Exception {
                    Log.d("Task save", "saving task");
                    return taskService.saveTask(newTask);
                }
            });
        }
        try {
            returnValue = result.get();
            Log.i(TAG, "return value = " + returnValue.toString());
        } catch (Exception e) {
            e.printStackTrace();
            messageBox("Exception", e.toString());
        }
        String message;
        if (returnValue != null && returnValue.isErrorFree()) {
            message = getResources().getString(R.string.taskSavedToast);
            Toast toast = Toast.makeText(
                    this, message, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        } else {
            message = getResources().getString(returnValue.getCustomErrorEnum().getValue());
            messageBox(getResources().getString(R.string.warningMessage), message);
        }

    }

    public void onCancelAddTaskButtonClicked(View view) {
        finish();
    }

    public void onPickColorButtonClicked(View view) {
        ColorPicker cp = new ColorPicker(this);
        if (isEditing) {
            ColorDrawable buttonColor = (ColorDrawable) pickColorButton.getBackground();
            int colorId = buttonColor.getColor();
            cp = new ColorPicker(this, Color.red(colorId), Color.green(colorId) , Color.blue(colorId));
        }
        cp.show();
        Button okColor = (Button) cp.findViewById(R.id.okColorButton);
        final ColorPicker finalCp = cp;
        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColorRGB = finalCp.getColor();
                System.out.println("selectedColorRGB = " + selectedColorRGB);

                Button colorButton = (Button) findViewById(R.id.pickColorButton);

                colorButton.setBackgroundColor(selectedColorRGB);
                stringColor = "#" + String.format("%02x%02x%02x", finalCp.getRed(), finalCp.getGreen(), finalCp.getBlue());
                finalCp.dismiss();
            }
        });
    }

    private void messageBox(String method, String message) {
        Log.d("EXCEPTION: " + method, message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

}