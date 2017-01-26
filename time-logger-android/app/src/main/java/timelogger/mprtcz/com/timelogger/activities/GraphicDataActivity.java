package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.graphics.controllers.GraphicController;
import timelogger.mprtcz.com.timelogger.graphics.views.GraphicView;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

import static timelogger.mprtcz.com.timelogger.utils.UiUtils.getHoursArrayAsync;

public class GraphicDataActivity extends AppCompatActivity {
    GraphicView graphicView;
    TextView sliderTitleTextView;
    private int visibleDays = 5;
    private int maxDays = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_data);
        UiUtils.loadLanguage(this);
        this.graphicView = (GraphicView) findViewById(R.id.graphicViewElement);
        this.sliderTitleTextView = (TextView) findViewById(R.id.sliderTitleTextView);
        setSeekBarListener();
        this.graphicView.setGraphicController(new GraphicController(getHoursArrayAsync(this)));
        this.maxDays = this.graphicView.getGraphicController().getMaxDays();
        if (maxDays > 5) {
            this.visibleDays = 5;
        } else {
            visibleDays = maxDays;
        }
        graphicView.getGraphicController().setVisibleDays(visibleDays);
        String basicSliderText = getResources().getString(R.string.sliderTitleText) + ": " + visibleDays;
        this.sliderTitleTextView.setText(basicSliderText);
        this.graphicView.invalidate();
    }

    private void setSeekBarListener() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(this.visibleDays);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                graphicView.getGraphicController().setVisibleDays(progress);
                if (progress > 0 && progress <= maxDays) {
                    String basicSliderText = getResources().getString(R.string.sliderTitleText)
                            + ": " + progress;
                    GraphicDataActivity.this.sliderTitleTextView.setText(basicSliderText);
                    graphicView.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
