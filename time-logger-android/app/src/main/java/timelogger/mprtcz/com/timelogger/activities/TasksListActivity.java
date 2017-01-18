package timelogger.mprtcz.com.timelogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.model.TasksAdapter;
import timelogger.mprtcz.com.timelogger.model.Task;

public class TasksListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        ListView listView = (ListView) findViewById(R.id.activitiesList);
        TasksAdapter adapter = new TasksAdapter(this, Task.activities);
        listView.setAdapter(adapter);
    }

    public void onAddActivityButtonClicked(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }
}
