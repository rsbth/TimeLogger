package timelogger.mprtcz.com.timelogger.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.task.model.TasksAdapter;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

import static timelogger.mprtcz.com.timelogger.activities.AddRecordActivity.ADD_TASK_ID;
import static timelogger.mprtcz.com.timelogger.activities.AddTaskActivity.EDITED_TASK_ID;
import static timelogger.mprtcz.com.timelogger.utils.UiUtils.getActiveTasksFromBackendAsync;

public class TasksListActivity extends AppCompatActivity {
    public static final String TAG = "TasksListActivity";
    TasksAdapter adapter;
    TaskService taskService;
    static Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        this.taskService = TaskService.getInstance(this);
    }

    public void onAddTaskButtonClicked(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        ListView listView = (ListView) findViewById(R.id.activitiesList);
        adapter = new TasksAdapter(this, getActiveTasksFromBackendAsync(this));
        listView.setAdapter(adapter);
        UiUtils.loadLanguage(this, locale);
        locale = Locale.getDefault();
    }

    public void onRemoveTaskButtonClicked(View view) {
        System.out.println("adapter.getSelectedItemPosition() = " + adapter.getSelectedTask());
        if (adapter.getSelectedTask() != null) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.removeTaskConfirmTitle))
                    .setMessage(getResources().getString(R.string.removeTaskConfirmLabel))
                    .setPositiveButton(getResources().getString(
                            R.string.yesButtonText),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        taskService.removeTask(adapter.getSelectedTask());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "Exception while removing task: " +e.toString());
                                    }
                                    onStart();
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.noButtonText), null)
                    .show();
        } else {
            Toast exceptionToast = Toast.makeText(
                    this, getResources().getString(R.string.chooseTaskFirstToast), Toast.LENGTH_SHORT);
            exceptionToast.show();
        }
    }

    public void onEditTaskButtonClicked(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        if (adapter.getSelectedTask() != null) {
            intent.putExtra(EDITED_TASK_ID, adapter.getSelectedTask().getId());
            startActivity(intent);
        } else {
            Toast exceptionToast = Toast.makeText(
                    this, getResources().getString(R.string.chooseTaskFirstToast), Toast.LENGTH_SHORT);
            exceptionToast.show();
        }
    }

    public void onAddRecordButtonClicked(View view) {
        Intent intent = new Intent(this, AddRecordActivity.class);
        if (adapter.getSelectedTask() != null) {
            intent.putExtra(ADD_TASK_ID, adapter.getSelectedTask().getId());
            startActivity(intent);
        } else {
            Toast exceptionToast = Toast.makeText(
                    this, getResources().getString(R.string.chooseTaskFirstToast), Toast.LENGTH_SHORT);
            exceptionToast.show();
        }
    }

    public void onGraphicButtonClicked(View view) {
        Intent intent = new Intent(this, GraphicDataActivity.class);
        startActivity(intent);
    }

    public void onSettingsButtonClicked(View view) {
        Intent intent = new Intent(this, SettingsListActivity.class);
        startActivity(intent);
    }
}
