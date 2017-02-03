package timelogger.mprtcz.com.timelogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.interfaces.UiUpdater;
import timelogger.mprtcz.com.timelogger.record.service.RecordService;
import timelogger.mprtcz.com.timelogger.record.service.RecordSyncService;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.task.service.TaskSyncService;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

public class SyncingActivity extends AppCompatActivity implements UiUpdater {
    private static final String TAG = "SyncingActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncing);
        final TaskSyncService taskSyncService = TaskService.getInstance(this).getTaskSyncService();
        final RecordSyncService recordSyncService = RecordService.getInstance(this).getRecordSyncService();
        this.textView = (TextView) findViewById(R.id.loadingTextViewId);
        Thread synchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    taskSyncService.synchronizeTasks(SyncingActivity.this);
                    recordSyncService.synchronizeRecords(SyncingActivity.this);
                } catch (final Exception e) {
                    LogWrapper.e(TAG, "exception during task sync");
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UiUtils.messageBox(SyncingActivity.this, "processSynchronization()", e.toString());
                        }
                    });
                } finally {
                    completeSynchronization();
                }
            }
        });
        synchThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                UiUtils.messageBox(SyncingActivity.this,
                        "uncaught exception while server syncing", e.toString());
                completeSynchronization();
            }
        });
        LogWrapper.i(TAG, "Thread starting background thread: " + Thread.currentThread().toString());
        synchThread.start();
    }

    public void completeSynchronization() {
        LogWrapper.d(TAG, "SyncingActivity completeSynchronization() called");
        Intent intent = new Intent(this, TasksListActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateTextView(SyncType syncType, final String text) {
        LogWrapper.i(TAG, "updateTextView() = " +text);
        final String message = getResources().getString(syncType.value) + text;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(message);
            }
        });
    }

    public enum SyncType {
        RECORD(R.string.recordSyncType),
        TASK(R.string.taskSyncType);

        private int value;

        SyncType(int value) {
            this.value = value;
        }
    }
}