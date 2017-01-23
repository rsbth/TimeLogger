package com.mprtcz.timeloggerserver.record.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore //for now
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @Column(name = "START_DATETIME")
    private LocalDateTime startDateTime;

    @Column(name = "END_DATETIME")
    private LocalDateTime endDateTime;

    public Record() {}
}
