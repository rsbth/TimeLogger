package timelogger.mprtcz.com.timelogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import timelogger.mprtcz.com.timelogger.activities.ActivitiesActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMainActivityButtonCLicked(View view) {
        Intent intent = new Intent(this, ActivitiesActivity.class);
        startActivity(intent);
    }
}
