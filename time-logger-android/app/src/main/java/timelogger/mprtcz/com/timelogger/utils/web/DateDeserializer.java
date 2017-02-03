package timelogger.mprtcz.com.timelogger.utils.web;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import timelogger.mprtcz.com.timelogger.utils.LogWrapper;

/**
 * Created by mprtcz on 2017-01-26.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    private static final String TAG = "DateDeserializer";

    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        String date = jsonElement.getAsString();
        try {
            long timestamp = Long.parseLong(date);
            Date resultDate = new Date(timestamp);
            return resultDate;
        } catch (Exception exp) {
            LogWrapper.e(TAG, "Exception while parsing date: " + exp);
            exp.printStackTrace();
            return null;
        }
    }
}