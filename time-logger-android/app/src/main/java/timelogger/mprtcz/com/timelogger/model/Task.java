package timelogger.mprtcz.com.timelogger.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Azet on 2017-01-17.
 */
@Getter
@Setter
@ToString
public class Task {

    private Long id;

    private String name;

    private String description;

    private String color;

    Collection<Record> activityRecords;

    public Task(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public Task(Long id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public static List<Task> activities = new ArrayList<>();
    static {
        activities.add(new Task(1L, "name", "description", "#03a9f4"));
        activities.add(new Task(2L, "name2", "long description", "#009688"));
        activities.add(new Task(3L, "name3", "even longer description", "#795548"));
        activities.add(new Task(4L, "name4", "even longer description \nwhich will contain more than 50 " +
                "word necessary to identify whether a string cutting method appears to work correctly", "#2196f3"));
    }

}