package timelogger.mprtcz.com.timelogger.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import timelogger.mprtcz.com.timelogger.R;
import timelogger.mprtcz.com.timelogger.utils.LogWrapper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    private static final String TAG = "TaskFragment";

    private String color;
    private String name;

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        setRetainInstance(true);
        TextView colorTextView = (TextView) view.findViewById(R.id.taskColorTextView);
        TextView nameTextView = (TextView) view.findViewById(R.id.taskNameTextView);
        LogWrapper.d(TAG, "onCreateView with color = " +color + ", name = " +name);
        colorTextView.setBackgroundColor(Color.parseColor(color));
        colorTextView.setText("     ");
        nameTextView.setText(name);
        return view;
    }

    public void setColorAndName(String color, String name) {
        LogWrapper.d(TAG, "setColorAndName(" + color + ", " +name + ")");
        this.color = color;
        this.name = name;
    }
}