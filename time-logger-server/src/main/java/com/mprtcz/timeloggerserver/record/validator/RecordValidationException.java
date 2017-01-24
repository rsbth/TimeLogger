package com.mprtcz.timeloggerserver.record.validator;

import lombok.Getter;

import java.util.List;

/**
 * Created by mprtcz on 2017-01-24.
 */
@Getter
public class RecordValidationException extends RuntimeException {
    private StringBuilder violationsMessage;

    public RecordValidationException(List<ValidationViolation> violations) {
        super();
        this.violationsMessage = new StringBuilder();
        for (ValidationViolation v :
                violations) {
            this.violationsMessage.append(v.toString());
            this.violationsMessage.append("\n");
        }
    }
}
