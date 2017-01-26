package com.mprtcz.timeloggerdesktop.backend.activity.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@Setter
@XmlRootElement(name="Activity")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "activities")
public class Activity {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private Long uuId;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String description;

    @DatabaseField(canBeNull = false)
    private String color;

    @DatabaseField(canBeNull = false)
    private Date lastModified;

    @DatabaseField(canBeNull = false)
    private boolean active;

    @ForeignCollectionField(eager = true)
    @XmlElement(name="activityRecord")
    Collection<Record> activityRecords;

    public Activity() {}

    public Activity(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.activityRecords = new ArrayList<>();
    }

    public Activity(Long id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public void addRecord(Record record) {
        if(this.activityRecords == null) {
            this.activityRecords = new ArrayList<>();
        }
        this.activityRecords.add(record);
    }

    @Override
    public String toString() {
        return "\nActivity{\n" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uuId='" + uuId + '\'' +
                ", color='" + color + '\'' +
                ", active='" + active + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", activityRecords=" + activityRecords +
                '}';
    }
}
