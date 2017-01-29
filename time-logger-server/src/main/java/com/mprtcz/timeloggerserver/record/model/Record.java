package com.mprtcz.timeloggerserver.record.model;

import com.mprtcz.timeloggerserver.task.model.Task;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Getter
@Setter
@Entity
@Table(name="records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @Column(name = "START_DATETIME", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "END_DATETIME", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "CREATION_DATETIME")
    private LocalDateTime creationDate;

    @Column(name = "SYNCH_DATETIME", nullable = false)
    private LocalDateTime synchronizationDate;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean active;

    @Column(name = "UUID", nullable = false)
    private String uuId;

    public Record() {}

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", taskID=" + task.getId() +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", creationDate=" + creationDate +
                ", active=" + active +
                ", uuid=" + uuId +
                ", synchronizationDate=" + synchronizationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (active != record.active) return false;
        if (task != null ? !task.equals(record.task) : record.task != null) return false;
        if (startDateTime != null ? !startDateTime.equals(record.startDateTime) : record.startDateTime != null)
            return false;
        if (endDateTime != null ? !endDateTime.equals(record.endDateTime) : record.endDateTime != null) return false;
        if (creationDate != null ? !creationDate.equals(record.creationDate) : record.creationDate != null)
            return false;
        if (synchronizationDate != null ? !synchronizationDate.equals(record.synchronizationDate) : record.synchronizationDate != null)
            return false;
        return uuId != null ? uuId.equals(record.uuId) : record.uuId == null;

    }

    @Override
    public int hashCode() {
        int result = task != null ? task.hashCode() : 0;
        result = 31 * result + (startDateTime != null ? startDateTime.hashCode() : 0);
        result = 31 * result + (endDateTime != null ? endDateTime.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (synchronizationDate != null ? synchronizationDate.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (uuId != null ? uuId.hashCode() : 0);
        return result;
    }
}
