package timelogger.mprtcz.com.timelogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.interfaces.Synchrotron;
import timelogger.mprtcz.com.timelogger.record.service.RecordService;
import timelogger.mprtcz.com.timelogger.record.service.RecordSyncService;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.task.service.TaskSyncService;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

public class SyncingActivity extends AppCompatActivity implements Synchrotron {
    private static final String TAG = "SyncingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogWrapper.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncing);
        Thread synchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronizeTasks();
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
        LogWrapper.d(TAG, "Thread starting background thread: " + Thread.currentThread().toString());
        synchThread.start();
    }

    @Override
    public void synchronizeTasks() {
        LogWrapper.d(TAG, "Started background thread: " + Thread.currentThread().toString());
        TaskSyncService taskSyncService = TaskService.getInstance(this).getTaskSyncService();
        try {
            taskSyncService.synchronizeTasks(this);
        } catch (final Exception e) {
            LogWrapper.e(TAG, "exception during task sync");
            e.printStackTrace();
            completeSynchronization();
            SyncingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UiUtils.messageBox(SyncingActivity.this, "processSynchronization()", e.toString());
                }
            });
        }
    }

    @Override
    public void synchronizeRecords() {
        RecordSyncService recordSyncService = RecordService.getInstance(this).getRecordSyncService();
        try {
            recordSyncService.synchronizeRecords(this);
        } catch (final Exception e) {
            e.printStackTrace();
            completeSynchronization();
            SyncingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UiUtils.messageBox(SyncingActivity.this, "processSynchronization()", e.toString());
                }
            });
        }
    }

    @Override
    public void completeSynchronization() {
        LogWrapper.d(TAG, "SyncingActivity completeSynchronization() called");
        Intent intent = new Intent(this, TasksListActivity.class);
        startActivity(intent);
    }
}