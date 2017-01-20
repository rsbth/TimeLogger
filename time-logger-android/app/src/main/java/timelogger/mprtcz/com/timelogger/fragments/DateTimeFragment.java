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

    @Getter
    DateTimeValues dateTimeValues = new DateTimeValues();
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
        this.timeEditText = (EditText) getView().findViewById(R.id.hourEditText);
        this.dateEditText = (EditText) getView().findViewById(R.id.dateEditText);
        this.dateEditText.setText(modelDateString(
                this.dateTimeValues.getDayOfMonth(),
                (this.dateTimeValues.getMonth() + 1),
                this.dateTimeValues.getYear()));
        this.timeEditText.setText(modelTimeString(this.dateTimeValues.getHourOfDay(),
                this.dateTimeValues.getMinute()));
    }

    private String modelDateString(int day, int month, int year) {
        String dayString = String.valueOf(day);
        String monthString = String.valueOf(month);
        String yearString = String.valueOf(year);

        if (day < 10) {
            dayString = "0" + day;
        }
        if (month < 10) {
            monthString = "0" + month;
        }
        return dayString + "." + monthString + "." + yearString;
    }

    private String modelTimeString(int hour, int minute) {
        String hourString = String.valueOf(hour);
        String minuteString = String.valueOf(minute);
        if (hour < 10) {
            hourString = "0" + hour;
        }
        if (minute < 10) {
            minuteString = "0" + minute;
        }
        return hourString + ":" + minuteString;
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
        this.dateTimeValues.setListener(summaryListener);
        this.setUpEditTextValues();
    }

    public void setInitialDateTime(DateTime dateTime) {
        this.dateTimeValues = new DateTimeValues().parseTo(dateTime,
                this.dateTimeValues.getListener());
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
                dateTimeValues.getYear(),
                (dateTimeValues.getMonth()),
                dateTimeValues.getDayOfMonth());
        datePickerDialog.show();
    }

    public void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(
                getActivity(), getTimeListener(),
                this.dateTimeValues.getHourOfDay(), this.dateTimeValues.getMinute(), true);
        timePicker.show();
    }

    private DatePickerDialog.OnDateSetListener getDateListener() {
        System.out.println("DateTimeFragment.getDateListener");
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateEditText.setText(modelDateString(dayOfMonth, (month + 1), year));
                dateTimeValues.setDate(year, month, dayOfMonth);
            }
        };
    }

    private TimePickerDialog.OnTimeSetListener getTimeListener() {
        System.out.println("DateTimeFragment.getTimeListener");

        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeEditText.setText(modelTimeString(hourOfDay, minute));
                dateTimeValues.setTime(hourOfDay, minute);
            }
        };
    }

    public interface OnChangeListener {
        void onChange(DateTime dateTimeObject);
    }

    @Getter
    public class DateTimeValues {
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
            Log.d("DateTimeFrag", "year = " + year + "month = " + month + "dayOfMonth = " + dayOfMonth);
            this.year = year;
            this.month = month;
            this.dayOfMonth = dayOfMonth;
            if (this.listener != null) {
                this.listener.onChange(this.parseDate());
            }
        }

        public void setTime(int hourOfDay, int minute) {
            Log.d("DateTimeFrag", "hourOfDay = " + hourOfDay + " minute = " + minute);
            this.hourOfDay = hourOfDay;
            this.minute = minute;
            if (this.listener != null) {
                this.listener.onChange(this.parseDate());
            }
        }

        public DateTime parseDate() {
            Log.d("parsingDate", "" +
                    "this.year = " + this.year +
                    "\nthis.month = " + this.month +
                    "\nthis.dayOfMonth = " + this.dayOfMonth +
                    "\nthis.hourOfDay = " + this.hourOfDay +
                    "\nthis.minute = " + this.minute);
            return new DateTime(
                    this.year,
                    (this.month + 1),
                    this.dayOfMonth,
                    this.hourOfDay,
                    this.minute);
        }

        public DateTimeValues parseTo(DateTime dateTime, OnChangeListener listener) {
            DateTimeValues dateTimeValues = new DateTimeValues();
            dateTimeValues.setTime(dateTime.hourOfDay().get(), dateTime.getMinuteOfHour());
            dateTimeValues.setDate(dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
            dateTimeValues.setListener(listener);
            return dateTimeValues;
        }
    }
}
