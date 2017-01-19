package timelogger.mprtcz.com.timelogger.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.task.dao.InMemoryDao;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.model.TasksAdapter;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;

public class TasksListActivity extends AppCompatActivity {
    TasksAdapter adapter;
    TaskService taskService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        this.taskService = new TaskService(new InMemoryDao());
    }

    public void onAddActivityButtonClicked(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView listView = (ListView) findViewById(R.id.activitiesList);
        adapter = new TasksAdapter(this, Task.tasks);
        listView.setAdapter(adapter);
    }

    public void onRemoveTaskButtonClicked(View view) {
        System.out.println("adapter.getSelectedItemPosition() = " + adapter.getSelectedTask());

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.removeTaskConfirmTitle))
                .setMessage(getResources().getString(R.string.removeTaskConfirmLabel))
                .setPositiveButton(getResources().getString(
                        R.string.yesButtonText),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskService.removeTask(adapter.getSelectedTask());
                                onStart();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.noButtonText), null)
                .show();
    }
}
