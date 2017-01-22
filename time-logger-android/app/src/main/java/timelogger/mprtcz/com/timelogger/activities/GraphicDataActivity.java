package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.graphics.controllers.GraphicController;
import timelogger.mprtcz.com.timelogger.graphics.views.GraphicView;
import timelogger.mprtcz.com.timelogger.task.model.HoursData;

import static timelogger.mprtcz.com.timelogger.utils.UiUtils.getAllTasksFromBackendAsync;

public class GraphicDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_data);
        GraphicView graphicView = (GraphicView) findViewById(R.id.graphicViewElement);
        HoursData hoursData = new HoursData(getAllTasksFromBackendAsync(this));
        graphicView.setGraphicController(new GraphicController(hoursData.getHoursArray()));
        graphicView.invalidate();
    }
}
