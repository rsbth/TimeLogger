package timelogger.mprtcz.com.timelogger.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import lombok.Getter;
import lombok.Setter;
import timelogger.mprtcz.com.timelogger.R;

public class DateTimeFragment extends Fragment {

    DateTimeValues dateTime = new DateTimeValues();
    EditText timeEditText;
    EditText dateEditText;

    public DateTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_time, container, false);
    }

    private void setUpEditTextValues() {
        this.timeEditText =(EditText) getView().findViewById(R.id.hourEditText);
        this.dateEditText =  (EditText) getView().findViewById(R.id.dateEditText);
        this.dateEditText.setText(this.initialDatetime.dayOfMonth().get() + "."
                + this.initialDatetime.monthOfYear().get() + "."
                + this.initialDatetime.year().get());
        this.timeEditText.setText(this.initialDatetime.getHourOfDay() + ":"
                + this.initialDatetime.getMinuteOfHour());
    }

    public void setUpListeners(OnChangeListener summaryListener) {
        getDateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        getTimeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        this.dateTime.setListener(summaryListener);
        this.setUpEditTextValues();
    }

    DateTime initialDatetime = new DateTime(2017, 1, 1, 0, 0);

    public void setInitialDateTime(DateTime dateTime) {
        this.initialDatetime = dateTime;
    }

    public TextView getTitleLabel() {
        return (TextView) getView().findViewById(R.id.dateTimeFragmentHeader);
    }

    public Button getDateButton() {
        return (Button) getView().findViewById(R.id.dateDialogButton);
    }

    public Button getTimeButton() {
        return (Button) getView().findViewById(R.id.timeDialogButton);
    }

    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                getDateListener(),
                initialDatetime.getYear(),
                (initialDatetime.getMonthOfYear() - 1), //srsly, datepicker counts months from 0 to 11
                initialDatetime.getDayOfMonth());
        datePickerDialog.show();
    }

    public void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(
                getActivity(), getTimeListener(),
                this.initialDatetime.getHourOfDay(), this.initialDatetime.getMinuteOfDay(), true);
        timePicker.show();
    }

    private DatePickerDialog.OnDateSetListener getDateListener() {
        System.out.println("DateTimeFragment.getDateListener");
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateEditText.setText(dayOfMonth + "." + (month + 1) + "." + year);
                dateTime.setDate(year, month, dayOfMonth);
            }
        };
    }

    private TimePickerDialog.OnTimeSetListener getTimeListener() {
        System.out.println("DateTimeFragment.getTimeListener");

        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeEditText.setText(hourOfDay + ":" + minute);
                dateTime.setTime(hourOfDay, minute);
            }
        };
    }

    public interface OnChangeListener {
        void onChange(DateTime dateTimeObject);
    }

    @Getter
    class DateTimeValues {
        private int year;
        private int month;
        private int dayOfMonth;
        private int hourOfDay;
        private int minute;

        @Setter
        public OnChangeListener listener;

        public DateTimeValues() {
            this.year = 2017;
            this.month = 1;
            this.dayOfMonth = 1;
            this.hourOfDay = 0;
            this.minute = 0;
        }

        public void setDate(int year, int month, int dayOfMonth) {
            Log.d("DateTimeFrag",  "year = " +year + "month = " +month + "dayOfMonth = " +dayOfMonth);
            this.year = year;
            this.month = month;
            this.dayOfMonth = dayOfMonth;
            this.listener.onChange(this.parseDate());
        }

        public void setTime(int hourOfDay, int minute) {
            Log.d("DateTimeFrag", "hourOfDay = " +hourOfDay + " minute = " +minute);
            this.hourOfDay = hourOfDay;
            this.minute = minute;
            this.listener.onChange(this.parseDate());
        }

        public DateTime parseDate() {
            return new DateTime(
                    this.year,
                    (this.month + 1),
                    this.dayOfMonth,
                    this.hourOfDay,
                    this.minute);
        }
    }
}
