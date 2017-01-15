package com.mprtcz.timeloggerdesktop.backend.utilities;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mprtcz on 2017-01-15.
 */
public class DateAdapter
        extends XmlAdapter<String, Date> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yy HH:mm");

    @Override
    public Date unmarshal(String s) throws Exception {
        return DATE_FORMAT.parse(s);
    }

    @Override
    public String marshal(Date date) throws Exception {
        return DATE_FORMAT.format(date);
    }
}
