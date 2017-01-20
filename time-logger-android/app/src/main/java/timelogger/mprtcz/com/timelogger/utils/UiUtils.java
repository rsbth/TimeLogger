package timelogger.mprtcz.com.timelogger.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Azet on 2017-01-20.
 */

public class UiUtils {

    public static void messageBox(Context context, String method, String message) {
        Log.d("EXCEPTION: " + method, message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(context);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    public static void displayValidationResult(Activity rootActivity,
                                               ValidationResult result,
                                               String messageBoxHeaderText) {
        if(result.isErrorFree()) {
            rootActivity.finish();
            Toast toast = Toast.makeText(
                    rootActivity,
                    rootActivity.getResources().getString(result.getCustomErrorEnum().getValue()),
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            String message = rootActivity.getResources().getString(result.getCustomErrorEnum().getValue());
            messageBox(rootActivity, messageBoxHeaderText, message);
        }
    }
}
