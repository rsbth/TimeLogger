package timelogger.mprtcz.com.timelogger.graphics.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import lombok.Getter;
import lombok.Setter;
import timelogger.mprtcz.com.timelogger.graphics.controllers.GraphicController;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;

/**
 * Created by Azet on 2017-01-22.
 */

public class GraphicView extends View {
    private static final String TAG = "GraphicView";

    public GraphicView(Context context) {
        super(context);
    }

    public GraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Setter
    @Getter
    GraphicController graphicController;

    private Paint paint = new Paint();

    @Override
    public void onDraw(Canvas canvas) {
        LogWrapper.i(TAG, "Drawing on canvas");
        graphicController.drawArrayOnCanvas(canvas);
    }
}
