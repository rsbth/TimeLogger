package timelogger.mprtcz.com.timelogger.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.fragments.DateTimeFragment;
import timelogger.mprtcz.com.timelogger.record.controller.RecordController;

public class AddRecordActivity extends AppCompatActivity {
    DateTimeFragment startFragment;
    DateTimeFragment endFragment;
    RecordController recordController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        this.startFragment = (DateTimeFragment) getSupportFragmentManager().findFragmentById(R.id.startDatetimeFragment);
        this.endFragment = (DateTimeFragment) getSupportFragmentManager().findFragmentById(R.id.endDatetimeFragment);
        this.recordController = new RecordController(this, startFragment, endFragment);
    }

    public void onChooseDateButtonClicked(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                System.out.println("year = " + year + " month = " +month + " day = " + dayOfMonth);
            }
        }, 2017, 1, 1);
        datePickerDialog.show();
    }

    public void onChooseTimeButtonClicked(View view) {
        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                System.out.println("hour: " + hourOfDay + " minute = " + minute);
            }
        }, 0, 0, true);
        timePicker.show();
    }
}
