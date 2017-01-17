package timelogger.mprtcz.com.timelogger.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import timelogger.mprtcz.com.timelogger.R;

/**
 * Created by Azet on 2017-01-17.
 */

public class ActivitiesAdapter extends ArrayAdapter<Activity> {
    public ActivitiesAdapter(Context context, List<Activity> objects) {
        super(context, R.layout.activities_row_view, objects);
    }

    Toast toast;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Activity activity = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.activities_row_view, parent, false);
        }

        final TextView nameTextView = (TextView) convertView.findViewById(R.id.activityNameTextView);
        TextView colorTextView = (TextView) convertView.findViewById(R.id.colorTextView);

        nameTextView.setText(activity.getName());
        colorTextView.setText("     ");

        colorTextView.setBackgroundColor(Color.parseColor(activity.getColor()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Activity " + activity.toString());
                if(toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(
                        getContext(), activity.getDescription(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return convertView;
    }
}
