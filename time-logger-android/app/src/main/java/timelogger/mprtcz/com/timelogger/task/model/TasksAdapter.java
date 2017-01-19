package timelogger.mprtcz.com.timelogger.task.model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lombok.Getter;
import timelogger.mprtcz.com.timelogger.R;

/**
 * Created by Azet on 2017-01-17.
 */

public class TasksAdapter extends ArrayAdapter<Task> {
    public TasksAdapter(Context context, List<Task> objects) {
        super(context, R.layout.activities_row_view, objects);
    }

    Toast toast;
    @Getter
    Task selectedTask;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.activities_row_view, parent, false);
        }

        final TextView nameTextView = (TextView) convertView.findViewById(R.id.activityNameTextView);
        TextView colorTextView = (TextView) convertView.findViewById(R.id.colorTextView);

        nameTextView.setText(task.getName());
        colorTextView.setText("     ");

        colorTextView.setBackgroundColor(Color.parseColor(task.getColor()));


        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Task " + task.toString());

                for(int a = 0; a < parent.getChildCount(); a++) {
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }
                finalConvertView.setBackgroundColor(Color.parseColor("#3F51B5"));

                if(toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(
                        getContext(), task.getDescription(), Toast.LENGTH_SHORT);
                toast.show();
                selectedTask = task;
            }
        });

        return convertView;
    }
}
