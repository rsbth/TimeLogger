package timelogger.mprtcz.com.timelogger.task.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Azet on 2017-01-30.
 */
@Getter
@Setter
@ToString
public class TaskDto {

    private Long serverId;

    private String name;

    private String description;

    private String color;

    private Date lastModified;

    private boolean active;
}
