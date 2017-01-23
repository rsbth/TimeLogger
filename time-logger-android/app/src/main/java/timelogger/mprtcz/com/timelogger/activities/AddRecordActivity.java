package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.fragments.DateTimeFragment;
import timelogger.mprtcz.com.timelogger.record.controller.RecordController;
import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

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
        UiUtils.loadLanguage(this);
        this.taskService = TaskService.getInstance(this);
        this.startFragment = (DateTimeFragment) getSupportFragmentManager().findFragmentById(R.id.startDatetimeFragment);
        this.endFragment = (DateTimeFragment) getSupportFragmentManager().findFragmentById(R.id.endDatetimeFragment);
        TextView summaryTextView = (TextView) findViewById(R.id.newRecordSummaryTextView);
        final Long id = getIntent().getLongExtra(ADD_TASK_ID, -1);
        this.recordController = new RecordController(this, startFragment,
                endFragment, UiUtils.getTaskByIdAsync(id, this), summaryTextView, UiUtils.getRecordsLatestHourAsync(this));
    }

    public void onAddRecordCancelButtonClicked(View view) {
        finish();
    }

    public void onAddRecordConfirmButtonClicked(View view) {
        final Record record = this.recordController.addRecord();
        UiUtils.addRecordAsync(record, this);
    }
}
