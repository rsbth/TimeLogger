package com.mprtcz.timeloggerdesktop.web.webstatic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    private Logger logger = LoggerFactory.getLogger(DateDeserializer.class);
    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        String date = jsonElement.getAsString();
        try {
            long timestamp = Long.parseLong(date);
            Date resultDate = new Date(timestamp);
            logger.info("Result date = " +resultDate);
            return resultDate;
        } catch (Exception exp) {
            logger.warn("Exception while parsing date: " + exp);
            exp.printStackTrace();
            return null;
        }
    }
}