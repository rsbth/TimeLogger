package com.mprtcz.timeloggerdesktop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@Setter
@DatabaseTable(tableName = "activities")
public class Activity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String description;

    @DatabaseField(canBeNull = false)
    private String color;

    @ForeignCollectionField(eager = true)
    Collection<Record> activityRecords;

    public Activity() {}

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Activity(Long id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public void addRecord(Record record) {
        this.activityRecords.add(record);
    }

    public static List<String> colorCodes = new ArrayList<>();
    static {
        colorCodes.add("#f44336");
        colorCodes.add("#9c27b0");
        colorCodes.add("#3f51b5");
        colorCodes.add("#03a9f4");
        colorCodes.add("#009688");
        colorCodes.add("#8bc34a");
        colorCodes.add("#ffeb3b");
        colorCodes.add("#ff9800");
        colorCodes.add("#795548");
        colorCodes.add("#607d8b");
        colorCodes.add("#e91e63");
        colorCodes.add("#673ab7");
        colorCodes.add("#2196f3");
        colorCodes.add("#00bcd4");
        colorCodes.add("#4caf50");
        colorCodes.add("#cddc39");
        colorCodes.add("#ffc107");
        colorCodes.add("#ff5722");
        colorCodes.add("#9e9e9e");
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", activityRecords=" + activityRecords +
                '}';
    }
}
