package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

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
    Button selectRangeButton;
    private int visibleDays = 5;
    private int maxDays = 0;
    DateTime earliestDay;
    DateTime latestDay;
    public static final int MAXIMUM_DAYS = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_data);
        UiUtils.loadLanguage(this);
        this.graphicView = (GraphicView) findViewById(R.id.graphicViewElement);
        this.sliderTitleTextView = (TextView) findViewById(R.id.sliderTitleTextView);
        this.selectRangeButton = (Button) findViewById(R.id.dateRangeButton);
        this.graphicView.setGraphicController(new GraphicController(getHoursArrayAsync(this)));
        this.maxDays = this.graphicView.getGraphicController().getMaxDays();
        this.earliestDay = graphicView.getGraphicController().getEarliestDay();
        this.latestDay = graphicView.getGraphicController().getLatestDay();
        if (maxDays > 5) {
            this.visibleDays = 5;
        } else {
            visibleDays = maxDays;
        }
        this.minSelectedValue = this.maxDays - 5;
        if (minSelectedValue <= 0) {
            this.minSelectedValue = 1;
        }
        graphicView.getGraphicController().setDayRangeToDraw(this.minSelectedValue, this.maxDays);
        setUpRangeButton();
        graphicView.getGraphicController().setVisibleDays(visibleDays);
        setVisibleDaysText();
        if (savedInstanceState == null) {
            composeLegend();
        }
        this.graphicView.invalidate();
    }

    int minSelectedValue;

    public void composeLegend() {
        TaskService taskService = TaskService.getInstance(this);
        LogWrapper.d(TAG, "composeLegend()");
        try {
            List<Task> tasks = taskService.getActiveTasks();
            LogWrapper.d(TAG, "List<Task> tasks = " + tasks.toString());
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Task task : tasks) {
                TaskFragment taskFragment = new TaskFragment();
                taskFragment.setColorAndName(task.getColor(), task.getName());
                fragmentTransaction.add(R.id.tasksLegendLinearLayout, taskFragment, task.getName());
            }
            LogWrapper.d(TAG, "fragmentTransaction = " + fragmentTransaction.toString());
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            messageBox(this, "Exception", e.toString());
        }
    }

    private void setUpRangeButton() {
        this.selectRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmoothDateRangePickerFragment rangePicker =
                        SmoothDateRangePickerFragment.newInstance(getRangeListener(),
                                earliestDay.getYear(), earliestDay.getMonthOfYear(), earliestDay.getDayOfMonth());
                rangePicker.setMinDate(earliestDay.toCalendar(Locale.getDefault()));
                rangePicker.setMaxDate(latestDay.toCalendar(Locale.getDefault()));
                rangePicker.show(getFragmentManager(), "rangePicker");
            }
        });
    }

    private SmoothDateRangePickerFragment.OnDateRangeSetListener getRangeListener() {
        return new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
            @Override
            public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                       int yearStart, int monthStart, int dayStart,
                                       int yearEnd, int monthEnd, int dayEnd) {
                DateTime startDate = new DateTime(yearStart, monthStart + 1, dayStart, 0 , 0, 0);
                DateTime endDate = new DateTime(yearEnd, monthEnd + 1, dayEnd, 0 , 0, 0);
                DateTime zeroedEarliestDay = earliestDay.withTimeAtStartOfDay();
                int earliestDayOffset = Days.daysBetween(zeroedEarliestDay.toLocalDate(), startDate.toLocalDate()).getDays();
                int latestDayOffset = Days.daysBetween(zeroedEarliestDay.toLocalDate(),  endDate.toLocalDate()).getDays();
                LogWrapper.d(TAG, "earliestDayOffset " +earliestDayOffset +" latestDayOffset " +latestDayOffset);
                visibleDays = latestDayOffset - earliestDayOffset;
                graphicView.getGraphicController().setDayRangeToDraw(earliestDayOffset, latestDayOffset);
                setVisibleDaysText();
                graphicView.invalidate();
                setButtonRangeText(startDate, endDate);
            }
        };
    }

    private void setVisibleDaysText() {
        String basicSliderText = getResources().getString(R.string.sliderTitleText) + ": " + visibleDays;
        this.sliderTitleTextView.setText(basicSliderText);
    }

    private void setButtonRangeText(DateTime startDate, DateTime endDate) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yy");
        String startString = fmt.print(startDate);
        String endString = fmt.print(endDate);
        this.selectRangeButton.setText(startString + " - " + endString);
    }
}
