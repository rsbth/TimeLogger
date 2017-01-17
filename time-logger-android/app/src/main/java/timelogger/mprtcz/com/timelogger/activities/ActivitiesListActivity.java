package timelogger.mprtcz.com.timelogger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.model.ActivitiesAdapter;
import timelogger.mprtcz.com.timelogger.model.Activity;

public class ActivitiesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        ListView listView = (ListView) findViewById(R.id.activitiesList);
        ActivitiesAdapter adapter = new ActivitiesAdapter(this, Activity.activities);
        listView.setAdapter(adapter);
    }
}
