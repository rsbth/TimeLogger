package com.mprtcz.timeloggerdesktop.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    public Activity() {}

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Activity(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public static List<String> colorCodes = new ArrayList<>();
    static {
        colorCodes.add("#ffcdd2");
        colorCodes.add("#f8bbd0");
        colorCodes.add("#e1bee7");
        colorCodes.add("#d1c4e9");
        colorCodes.add("#c5cae9");
        colorCodes.add("#bbdefb");
        colorCodes.add("#b3e5fc");
        colorCodes.add("#b2ebf2");
        colorCodes.add("#b2dfdb");
        colorCodes.add("#c8e6c9");
        colorCodes.add("#dcedc8");
        colorCodes.add("#f0f4c3");
        colorCodes.add("#fff9c4");
        colorCodes.add("#ffecb3");
        colorCodes.add("#ffe0b2");
        colorCodes.add("#ffccbc");
        colorCodes.add("#d7ccc8");
        colorCodes.add("#f5f5f5");
        colorCodes.add("#cfd8dc");
    }

    @Override
    public String toString() {
        return name;
    }
}
