package timelogger.mprtcz.com.timelogger.record.model.converter;


import java.util.Date;
import java.util.Objects;

import timelogger.mprtcz.com.timelogger.record.model.Record;
import timelogger.mprtcz.com.timelogger.record.model.RecordDto;
import timelogger.mprtcz.com.timelogger.task.model.Task;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class RecordConverter {

    public static Record toEntity(RecordDto recordDto, Task task) {
        Record record = new Record();
        if (Objects.equals(task.getServerId(), recordDto.getTaskServerId())) {
            record.setTask(task);
        }
        record.setStartDateTime(recordDto.getStartDateTime());
        record.setEndDateTime(recordDto.getEndDateTime());
        record.setSynchronizationDate(recordDto.getSynchronizationDate());
        record.setCreationDate(recordDto.getCreationDate());
        record.setActive(recordDto.isActive());
        record.setUuId(recordDto.getUuId());
        return record;
    }

    public static RecordDto toDto(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.setTaskServerId(record.getTask().getServerId()); //translating id to global uuid
        recordDto.setStartDateTime(record.getStartDateTime());
        recordDto.setEndDateTime(record.getEndDateTime());
        recordDto.setCreationDate(record.getCreationDate());
        recordDto.setUuId(record.getUuId());
        return recordDto;
    }
}
