package com.mprtcz.timeloggerweb.client.model;

/**
 * Created by mprtcz on 2017-02-17.
 */
public class TaskDto {
    private String name;
    private String description;
    private String color;

    public TaskDto(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}
