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
    public ColorButton(Context context) {
        super(context);
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        System.out.println("color = " + color);
        this.changeTextColor(color);
    }

    private void changeTextColor(int color) {
        System.out.println("Changing color");
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        double Y = 0.299 * red + 0.587 * green + 0.114 * blue;
        Log.d("changeColorText", "Y = " + Y);
        if(Y < 100) {
            super.setTextColor(Color.WHITE);
        } else {
            super.setTextColor(Color.BLACK);
        }
    }
}
