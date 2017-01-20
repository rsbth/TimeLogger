package timelogger.mprtcz.com.timelogger.record.controller;

import android.app.Activity;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.fragments.DateTimeFragment;
import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by Azet on 2017-01-20.
 */

public class RecordController {

    private DateTimeFragment startFragment;
    private DateTimeFragment endFragment;
    private Activity parentActivity;
    private TextView summaryTextView;
    private DateTime initialDateTime;
    private Task rootTask;

    public RecordController(Activity parent, DateTimeFragment startFragment,
                            DateTimeFragment endFragment, Task task,
                            TextView summaryTextView) {
        this.parentActivity = parent;
        this.startFragment = startFragment;
        this.endFragment = endFragment;
        this.summaryTextView = summaryTextView;
        this.initialDateTime = new DateTime(2017, 1, 10, 0, 0);
        this.rootTask = task;
        this.setUpInitialDateTimes();
        setListeners();
        setUpHeaders();
    }

    private void setListeners() {
        this.startFragment.setUpListeners(new DateTimeFragment.OnChangeListener() {
            @Override
            public void onChange(DateTime dateTimeObject) {
                updateSummaryTextView();
            }
        });
        this.endFragment.setUpListeners(new DateTimeFragment.OnChangeListener() {
            @Override
            public void onChange(DateTime dateTimeObject) {
                updateSummaryTextView();
            }
        });
    }

    private void setUpInitialDateTimes() {
        this.startDateTime = initialDateTime;
        this.endDateTime = initialDateTime;
        this.startFragment.setInitialDateTime(this.startDateTime);
        this.endFragment.setInitialDateTime(this.endDateTime);
    }

    private void setUpHeaders() {
        this.startFragment.getTitleLabel().setText(
                parentActivity.getResources().getString(R.string.startFragmentHeaderText));
        this.endFragment.getTitleLabel().setText(
                parentActivity.getResources().getString(R.string.endFragmentHeaderText));
    }

    private DateTime startDateTime;
    private DateTime endDateTime;

    private void updateSummaryTextView() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        endDateTime = endFragment.getDateTimeValues().parseDate();
        startDateTime = startFragment.getDateTimeValues().parseDate();
        String message = this.rootTask.getName() +"\nStart: " + formatter.print(this.startDateTime) +
                "\nEnd: " + formatter.print(this.endDateTime);
        this.summaryTextView.setText(message);
    }
}
