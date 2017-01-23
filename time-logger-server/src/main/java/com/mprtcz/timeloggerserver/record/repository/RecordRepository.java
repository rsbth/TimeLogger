package com.mprtcz.timeloggerserver.record.repository;

import com.mprtcz.timeloggerserver.record.model.Record;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Transactional
public interface RecordRepository extends CrudRepository<Record, Long> {
}
