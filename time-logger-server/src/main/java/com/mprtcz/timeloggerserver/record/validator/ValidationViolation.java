package com.mprtcz.timeloggerserver.record.validator;

/**
 * Created by mprtcz on 2017-01-24.
 */
public enum ValidationViolation {
    END_DATE_BEFORE_START_DATE,
    RECORD_EXISTS,
    RECORD_TAKES_0_HOURS;
}
