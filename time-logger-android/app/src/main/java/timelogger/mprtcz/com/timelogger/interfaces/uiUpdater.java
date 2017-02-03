package timelogger.mprtcz.com.timelogger.interfaces;

import timelogger.mprtcz.com.timelogger.activities.SyncingActivity;

/**
 * Created by Azet on 2017-01-31.
 */

public interface UiUpdater {

    void updateTextView(SyncingActivity.SyncType syncType, String text);
}