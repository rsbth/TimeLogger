package timelogger.mprtcz.com.timelogger.record.controller;

import android.app.Activity;

import org.joda.time.DateTime;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.fragments.DateTimeFragment;

/**
 * Created by Azet on 2017-01-20.
 */

public class RecordController {

    private DateTimeFragment startFragment;
    private DateTimeFragment endFragment;
    private Activity parentActivity;

    public RecordController(Activity parent, DateTimeFragment startFragment,
            DateTimeFragment endFragment) {
        this.parentActivity = parent;
        this.startFragment = startFragment;
        this.endFragment = endFragment;
        setListeners();
        setUpHeaders();
    }

    private void setListeners() {
        this.startFragment.setUpListeners(getStartSummaryListener());
        this.endFragment.setUpListeners(getEndSummaryListener());
    }

    private void setUpHeaders() {
        this.startFragment.getTitleLabel().setText(
                parentActivity.getResources().getString(R.string.startFragmentHeaderText));
        this.endFragment.getTitleLabel().setText(
                parentActivity.getResources().getString(R.string.endFragmentHeaderText));
    }

    private DateTimeFragment.OnChangeListener getStartSummaryListener() {
        return new DateTimeFragment.OnChangeListener() {
            @Override
            public void onChange(DateTime dateTimeObject) {
                RecordController.this.updateSummaryTextView(dateTimeObject);
            }
        };
    }

    private DateTimeFragment.OnChangeListener getEndSummaryListener() {
        return new DateTimeFragment.OnChangeListener() {
            @Override
            public void onChange(DateTime dateTimeObject) {
                RecordController.this.updateSummaryTextView(dateTimeObject);
            }
        };
    }

    private void updateSummaryTextView(DateTime datetime) {
        System.out.println("Updating datetime: " +datetime.toString());
    }
}
