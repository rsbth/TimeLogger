package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.fragments.DateTimeFragment;
import timelogger.mprtcz.com.timelogger.record.controller.RecordController;
import timelogger.mprtcz.com.timelogger.task.dao.InMemoryDao;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;

import static timelogger.mprtcz.com.timelogger.task.controllers.AddTaskController.messageBox;

public class AddRecordActivity extends AppCompatActivity {
    DateTimeFragment startFragment;
    DateTimeFragment endFragment;
    RecordController recordController;
    TaskService taskService;
    public static final String ADD_TASK_ID = "ADD_TASK_ID";
    public static final String TAG = "AddRecordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        this.taskService = new TaskService(new InMemoryDao());
        this.startFragment = (DateTimeFragment) getSupportFragmentManager().findFragmentById(R.id.startDatetimeFragment);
        this.endFragment = (DateTimeFragment) getSupportFragmentManager().findFragmentById(R.id.endDatetimeFragment);
        TextView summaryTextView = (TextView) findViewById(R.id.newRecordSummaryTextView);
        this.recordController = new RecordController(this, startFragment, endFragment, getTaskById(), summaryTextView);
    }

    private Task getTaskById() {
        final Long id = getIntent().getLongExtra(ADD_TASK_ID, -1);
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
            messageBox(this, "Exception", e.toString());
        }
        return returnValue;
    }
}
