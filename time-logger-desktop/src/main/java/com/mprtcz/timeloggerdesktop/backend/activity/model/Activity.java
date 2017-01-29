package com.mprtcz.timeloggerdesktop.backend.activity.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-03.
 */
@Getter
@Setter
@XmlRootElement(name = "Activity")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "activities")
public class Activity {
    private Logger logger = LoggerFactory.getLogger(Activity.class);

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private Long serverId;

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
    @XmlElement(name = "activityRecord")
    Collection<Record> activityRecords;

    public Activity() {
    }

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
        if (this.activityRecords == null) {
            this.activityRecords = new ArrayList<>();
        }
        List<Record> records = new ArrayList<>(this.activityRecords);
        int recordPosition = records.indexOf(record);
        if (recordPosition != -1) {
            logger.info("Record exists at {}, replacing: {}", recordPosition, record);
            records.set(recordPosition, record);
            this.activityRecords = records;
        } else {
            this.activityRecords.add(record);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (active != activity.active) return false;
        if (id != null ? !id.equals(activity.id) : activity.id != null) return false;
        if (serverId != null ? !serverId.equals(activity.serverId) : activity.serverId != null) return false;
        if (name != null ? !name.equals(activity.name) : activity.name != null) return false;
        if (description != null ? !description.equals(activity.description) : activity.description != null)
            return false;
        if (color != null ? !color.equals(activity.color) : activity.color != null) return false;
        return lastModified != null ? lastModified.equals(activity.lastModified) : activity.lastModified == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (serverId != null ? serverId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", taskServerId=" + serverId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", lastModified=" + lastModified +
                ", active=" + active +
                '}';
    }
}
