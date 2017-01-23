package com.mprtcz.timeloggerserver.record.repository;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements
        AttributeConverter<LocalDate, Date> {
    @Override
    public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
        return java.sql.Date.valueOf(entityValue);
    }

    @Override
    public LocalDate convertToEntityAttribute(java.sql.Date databaseValue) {
        return databaseValue.toLocalDate();
    }
}
