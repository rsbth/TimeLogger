package timelogger.mprtcz.com.timelogger.utils;

import android.app.Activity;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Azet on 2017-01-21.
 */

public class OrmBaseClass {
    private static OrmBaseClass ormBaseClass;
    private DatabaseHelper databaseHelper = null;

    public DatabaseHelper getHelper(Activity activity) {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(activity, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void terminate() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public static OrmBaseClass getInstance() {
        if (ormBaseClass == null) {
            ormBaseClass = new OrmBaseClass();
        }
        return ormBaseClass;
    }
}
