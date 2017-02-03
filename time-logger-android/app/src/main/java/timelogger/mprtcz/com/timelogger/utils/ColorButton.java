package timelogger.mprtcz.com.timelogger.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

/**
 * Created by Azet on 2017-01-26.
 */

public class ColorButton extends Button {
    private static final String TAG = "ColorButton";

    public ColorButton(Context context) {
        super(context);
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        LogWrapper.i(TAG, "color = " + color);
        this.changeTextColor(color);
    }

    public static final int LUMINANCE_BORDER = 100;

    private void changeTextColor(int color) {
        LogWrapper.i(TAG, "Changing color");
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        double luminace = 0.299 * red + 0.587 * green + 0.114 * blue;
        LogWrapper.d(TAG, "Luminace = " + luminace);
        int invLuminace = 255 - (int) luminace;
        if (invLuminace < LUMINANCE_BORDER) {
            super.setTextColor(Color.argb(137, 0, 0, 0));
        } else {
            super.setTextColor(Color.argb(137, 255, 255, 255));
        }
    }
}
