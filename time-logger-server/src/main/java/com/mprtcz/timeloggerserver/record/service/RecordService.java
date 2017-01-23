package com.mprtcz.timeloggerserver.record.service;

import com.mprtcz.timeloggerserver.record.model.Record;
import com.mprtcz.timeloggerserver.record.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Service
public class RecordService {

    @Autowired
    RecordRepository recordRepository;

    public void saveRecord(Record r) {
        this.recordRepository.save(r);
    }
}
