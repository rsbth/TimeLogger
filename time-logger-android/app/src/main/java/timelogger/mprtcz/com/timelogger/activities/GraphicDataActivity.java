package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.fragments.TaskFragment;
import timelogger.mprtcz.com.timelogger.graphics.controllers.GraphicController;
import timelogger.mprtcz.com.timelogger.graphics.views.GraphicView;
import timelogger.mprtcz.com.timelogger.task.model.Task;
import timelogger.mprtcz.com.timelogger.task.service.TaskService;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

import static timelogger.mprtcz.com.timelogger.utils.UiUtils.getHoursArrayAsync;
import static timelogger.mprtcz.com.timelogger.utils.UiUtils.messageBox;

public class GraphicDataActivity extends AppCompatActivity {
    private static final String TAG = "GraphicDataActivity";
    GraphicView graphicView;
    TextView sliderTitleTextView;
    private int visibleDays = 5;
    private int maxDays = 0;
    public static final int MAXIMUM_DAYS = 31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_data);
        UiUtils.loadLanguage(this);
        this.graphicView = (GraphicView) findViewById(R.id.graphicViewElement);
        this.sliderTitleTextView = (TextView) findViewById(R.id.sliderTitleTextView);
        this.graphicView.setGraphicController(new GraphicController(getHoursArrayAsync(this)));
        this.maxDays = this.graphicView.getGraphicController().getMaxDays();
        if (maxDays > 5) {
            this.visibleDays = 5;
        } else {
            visibleDays = maxDays;
        }
        if(maxDays > MAXIMUM_DAYS) {
            this.maxDays = MAXIMUM_DAYS;
        }
        setSeekBarListener();
        graphicView.getGraphicController().setVisibleDays(visibleDays);
        String basicSliderText = getResources().getString(R.string.sliderTitleText) + ": " + visibleDays;
        this.sliderTitleTextView.setText(basicSliderText);
        if(savedInstanceState == null) {
            composeLegend();
        }
        this.graphicView.invalidate();
    }

    private void setSeekBarListener() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(this.maxDays);
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

    public void composeLegend() {
        TaskService taskService = TaskService.getInstance(this);
        LogWrapper.d(TAG, "composeLegend()");
        try {
            List<Task> tasks = taskService.getActiveTasks();
            LogWrapper.d(TAG, "List<Task> tasks = " +tasks.toString());
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Task task : tasks) {
                TaskFragment taskFragment = new TaskFragment();
                taskFragment.setColorAndName(task.getColor(), task.getName());
                fragmentTransaction.add(R.id.tasksLegendLinearLayout, taskFragment, task.getName());
            }
            LogWrapper.d(TAG, "fragmentTransaction = " +fragmentTransaction.toString());
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            messageBox(this, "Exception", e.toString());
        }

    }
}
