package com.mprtcz.timeloggerdesktop;

import com.mprtcz.timeloggerdesktop.model.Activity;
import com.mprtcz.timeloggerdesktop.model.DataRepresentation;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by mprtcz on 2017-01-09.
 */
public class DateRepresentationTest {

    @Test
    public void calculateDrawingStartingPointTest() {
        assertEquals(0, getInstanceWithHours(168).calculateDrawingStartingPoint());
        assertEquals(0, getInstanceWithHours(167).calculateDrawingStartingPoint());
        assertEquals(24, getInstanceWithHours(169).calculateDrawingStartingPoint());
        assertEquals(168, getInstanceWithHours(336).calculateDrawingStartingPoint());
    }

    private DataRepresentation getInstanceWithHours(int number) {
        List<Activity> activities = new ArrayList<>();
        DataRepresentation dataRepresentation = new DataRepresentation(activities);
        LocalDateTime startingHour = LocalDateTime.now();
        for (int i = 0; i < number; i++) {
            dataRepresentation.getHours().add(new DataRepresentation.Hour(startingHour));
            startingHour = startingHour.plusHours(1L);
        }
        return dataRepresentation;
    }


}
