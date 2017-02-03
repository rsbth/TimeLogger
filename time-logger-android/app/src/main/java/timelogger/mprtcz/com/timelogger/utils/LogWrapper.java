package timelogger.mprtcz.com.timelogger.utils;

import android.util.Log;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;

/**
 * Created by Azet on 2017-02-03.
 */

public class LogWrapper {
    private static final int LEVEL = VERBOSE;

/*
ASSERT: Constant Value: 7 (0x00000007)
ERROR: Constant Value: 6 (0x00000006)
WARN: Constant Value: 5 (0x00000005)
INFO: Constant Value: 4 (0x00000004)
DEBUG: Constant Value: 3 (0x00000003)
VERBOSE: Constant Value: 2 (0x00000002)
*/

    public static void d(String TAG, String message) {
        if (LEVEL <= DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void w(String TAG, String message) {
        if (LEVEL <= WARN) {
            Log.w(TAG, message);
        }
    }

    public static void i(String TAG, String message) {
        if (LEVEL <= INFO) {
            Log.i(TAG, message);
        }
    }

    public static void e(String TAG, String message) {
        if (LEVEL <= ERROR) {
            Log.e(TAG, message);
        }
    }

    public static void e(String TAG, String message, Throwable t) {
        if (LEVEL <= ERROR) {
            Log.e(TAG, message, t);
        }
    }
}
