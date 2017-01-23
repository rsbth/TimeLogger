package com.mprtcz.timeloggerserver.task.repository;

import com.mprtcz.timeloggerserver.task.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Transactional
public interface TaskRepository extends CrudRepository<Task, Long> {
}
