package timelogger.mprtcz.com.timelogger;

import android.app.Application;
import android.util.Log;

import timelogger.mprtcz.com.timelogger.utils.OrmBaseClass;

/**
 * Created by Azet on 2017-01-21.
 */

public class BaseApplication extends Application {

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("BaseApplication", "Terminating OrmBaseClass");
        OrmBaseClass.getInstance().terminate();
    }
}
