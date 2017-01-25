package com.mprtcz.timeloggerdesktop.web.record.service;

import com.mprtcz.timeloggerdesktop.backend.record.model.Record;
import com.mprtcz.timeloggerdesktop.web.record.model.RecordDto;
import com.mprtcz.timeloggerdesktop.web.record.model.converter.RecordConverter;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class RecordDtoService {

    RecordConverter recordConverter;

    public RecordDtoService(RecordConverter recordConverter) {
        this.recordConverter = recordConverter;
    }

    public void sendRecord(Record record) {
        RecordDto recordDto = this.recordConverter.toDto(record);
    }
}
