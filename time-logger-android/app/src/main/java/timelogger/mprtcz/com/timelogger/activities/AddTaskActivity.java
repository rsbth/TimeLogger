package timelogger.mprtcz.com.timelogger.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.task.controllers.AddTaskController;
import timelogger.mprtcz.com.timelogger.task.controllers.EditTaskController;


public class AddTaskActivity extends Activity {
    EditText nameEditText;
    EditText descriptionEditText;
    Button pickColorButton;
    public static final String EDITED_TASK_ID = "EDITED_TASK_ID";
    AddTaskController addTaskController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("AddTaskActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        this.nameEditText = (EditText) findViewById(R.id.taskNameEditText);
        this.pickColorButton = (Button) findViewById(R.id.pickColorButton);
        this.descriptionEditText = (EditText) findViewById(R.id.taskDescriptionEditText);
        if (getIntent().getExtras() != null) {
            this.addTaskController = new EditTaskController(this.nameEditText,
                    this.descriptionEditText, this, this.pickColorButton);
        } else {
            this.addTaskController = new AddTaskController(this.nameEditText,
                    this.descriptionEditText, this);
        }
    }

    public void onAddTaskButtonClicked(View view) {
        boolean returnValue = this.addTaskController.onAddTaskButtonClicked();
        if (returnValue) {
            finish();
        }
    }

    public void onCancelAddTaskButtonClicked(View view) {
        finish();
    }

    public void onPickColorButtonClicked(View view) {
        this.addTaskController.onPickColorButtonClicked();
    }
}