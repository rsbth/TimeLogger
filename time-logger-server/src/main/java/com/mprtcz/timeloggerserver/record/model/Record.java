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

    public Record() {}
}
