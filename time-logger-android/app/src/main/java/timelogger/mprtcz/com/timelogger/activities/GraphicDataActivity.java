package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.graphics.controllers.GraphicController;
import timelogger.mprtcz.com.timelogger.graphics.views.GraphicView;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

import static timelogger.mprtcz.com.timelogger.utils.UiUtils.getHoursArrayAsync;

public class GraphicDataActivity extends AppCompatActivity {
    GraphicView graphicView;
    public static final int DAYS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_data);
        UiUtils.loadLanguage(this);
        this.graphicView = (GraphicView) findViewById(R.id.graphicViewElement);
        setSeekBarListener();
        this.graphicView.setGraphicController(new GraphicController(getHoursArrayAsync(this)));
        graphicView.getGraphicController().setVisibleDays(DAYS);
        this.graphicView.invalidate();
    }

    private void setSeekBarListener() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(DAYS);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                graphicView.getGraphicController().setVisibleDays(progress);
                graphicView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
