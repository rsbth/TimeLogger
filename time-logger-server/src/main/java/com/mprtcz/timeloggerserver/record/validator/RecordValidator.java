package com.mprtcz.timeloggerserver.record.validator;

import com.mprtcz.timeloggerserver.record.model.Record;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mprtcz on 2017-01-24.
 */
public class RecordValidator {
    private Record record;
    private List<Record> records = new ArrayList<>();
    private List<ValidationViolation> violations = new ArrayList<>();

    public RecordValidator(Record record, Iterable<Record> recordsIterable) {
        this.record = record;
        recordsIterable.forEach(records::add);
        this.validateNewRecord();
    }

    public void validateNewRecord() {
        this.checkUniqueness();
        this.checkDates();
        if(!violations.isEmpty()) {
           throw new RecordValidationException(this.violations);
        }
    }

    private void checkUniqueness() {
        violations.addAll(records.stream().filter(r ->
                r.equals(record)).map(r -> ValidationViolation.RECORD_EXISTS)
                .collect(Collectors.toList()));
    }

    private void checkDates() {
        LocalDateTime startDateTime = this.record.getStartDateTime();
        LocalDateTime endDateTime = this.record.getEndDateTime();

        if (startDateTime == endDateTime) {
            this.violations.add(ValidationViolation.RECORD_TAKES_0_HOURS);
        }

        if (endDateTime.isBefore(startDateTime)) {
            this.violations.add(ValidationViolation.END_DATE_BEFORE_START_DATE);
        }
    }
}
