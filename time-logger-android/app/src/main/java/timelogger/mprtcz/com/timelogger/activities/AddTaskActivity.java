package timelogger.mprtcz.com.timelogger.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.model.Task;


public class AddTaskActivity extends Activity {
    String stringColor = "";
    int selectedColorRGB = 0;
    String name = "";
    String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("AddTaskActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    public void onAddTaskButtonClicked(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.taskNameEditText);
        EditText descriptionEditText = (EditText) findViewById(R.id.taskDescriptionEditText);
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
                                    saveNewTask();
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.noButtonText), null)
                    .show();
        } else {
            this.saveNewTask();
        }
    }

    private void saveNewTask() {
        Task newTask = new Task(this.name,
                this.description, this.stringColor);
        System.out.println("newTask = " + newTask);
        String message = getResources().getString(R.string.taskSavedToast) + "  " + newTask.getName();
        Toast toast = Toast.makeText(
                this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onCancelAddTaskButtonClicked(View view) {
        finish();
    }

    public void onPickColorButtonClicked(View view) {
        final ColorPicker cp = new ColorPicker(this);
        cp.show();
        Button okColor = (Button) cp.findViewById(R.id.okColorButton);
        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColorRGB = cp.getColor();
                System.out.println("selectedColorRGB = " + selectedColorRGB);

                Button colorButton = (Button) findViewById(R.id.pickColorButton);

                colorButton.setBackgroundColor(selectedColorRGB);
                stringColor = String.format("%02x%02x%02x", cp.getRed(), cp.getGreen(), cp.getBlue());
                cp.dismiss();
            }
        });
    }
}
