package com.mprtcz.timeloggerdesktop.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-05.
 */
@Getter
public class DataRepresentation {

    private List<Activity> allActivities;
    private List<Record> allRecords;
    private LocalDateTime earliest;
    private LocalDateTime latest;

    private List<Hour> hours = new ArrayList<>();

    public DataRepresentation(List<Activity> allActivities) {
        this.allActivities = allActivities;
        this.getAllRecords();
        this.setExtremeRecords();
        this.createHoursList();
        this.fillHoursWithActivities();
    }

    private void getAllRecords() {
        this.allRecords = new ArrayList<>();
        this.allActivities.stream().filter(a -> a.getActivityRecords() != null)
                .forEach(a -> allRecords.addAll(a.getActivityRecords()));
    }

    private void setExtremeRecords() {
        Date earliestRecord = new Date(Long.MAX_VALUE);
        Date latestRecord = new Date(Long.MIN_VALUE);
        for (Record record : allRecords) {
            if (record.getStartDateTime().before(earliestRecord)) {
                earliestRecord = record.getStartDateTime();
            }
            if (record.getEndDateTime().after(latestRecord)) {
                latestRecord = record.getEndDateTime();
            }
        }

        this.earliest = LocalDateTime.ofInstant(earliestRecord.toInstant(), ZoneId.systemDefault());
        this.latest = LocalDateTime.ofInstant(latestRecord.toInstant(), ZoneId.systemDefault());
    }

    private void createHoursList() {
        long hoursDelta = earliest.until(latest, ChronoUnit.HOURS);
        LocalDateTime currentHour = earliest;
        this.hours = new ArrayList<>();
        for (int i = 0; i < hoursDelta; i++) {
            this.hours.add(new Hour(currentHour));
            currentHour = currentHour.plusHours(1L);
        }
    }

    private void fillHoursWithActivities() {
        for (Record record : this.allRecords) {
            LocalDateTime start = LocalDateTime.ofInstant(record.getStartDateTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.ofInstant(record.getEndDateTime().toInstant(), ZoneId.systemDefault());
            long duration = start.until(end, ChronoUnit.HOURS);
            int position = getListPositionOfSpecificHour(start);
            for (int i = 0; i < duration; i++) {
                Hour hour = this.hours.get(position);
                hour.getActivitiesDuringThisHour().add(record.getActivity());
                position++;
            }
        }
    }

    @Getter
    @EqualsAndHashCode
    public class Hour {
        List<Activity> activitiesDuringThisHour;
        LocalDateTime datetime;

        Hour(LocalDateTime datetime) {
            this.datetime = datetime;
            this.activitiesDuringThisHour = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "\nHour{\n" +
                    ", datetime=" + datetime +
                    ", num of activities =" + activitiesDuringThisHour.size() +
                    "activitiesDuringThisHour=" + activitiesDuringThisHour +
                    "}";
        }
    }

    private int getListPositionOfSpecificHour(LocalDateTime hourToFind) {
        for (Hour hour :
                this.hours) {
            if(hourToFind.equals(hour.getDatetime())) {
                return this.hours.indexOf(hour);
            }
        }
        return -1;
    }

    public void drawOnCanvas(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        int unit = (int) width/25;
        drawHeader(graphicsContext, unit);
        for (int i = 0; i < this.hours.size(); i++) {
            String color;
            if(this.hours.get(i).getActivitiesDuringThisHour().size() > 0) {
                color = this.hours.get(i).getActivitiesDuringThisHour().get(0).getColor();
            } else {
                color = "#ffffff";
            }
            long dayDelta = earliest.until(this.hours.get(i).getDatetime(), ChronoUnit.DAYS);
            long hour = this.hours.get(i).getDatetime().getHour();
            graphicsContext.setFill(Paint.valueOf(color));
            graphicsContext.fillRect( unit*(hour + 1), 10*(dayDelta +1), unit, 10);
            String day = this.hours.get(i).getDatetime().getDayOfMonth() +"." + this.hours.get(i).getDatetime().getMonthValue();
            graphicsContext.setFill(Paint.valueOf("black"));
            graphicsContext.fillText(day, 0, (dayDelta + 2) * 10);
        }
    }

    private void drawHeader(GraphicsContext graphicsContext, int unit) {
        graphicsContext.setFill(Paint.valueOf("black"));
        for (int i = 1; i < 25; i++) {
            graphicsContext.fillText(String.valueOf(i - 1), unit * i, 10, 10);
        }
    }

    public static void main(String[] args) {
        System.out.println(ZoneId.getAvailableZoneIds());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = LocalDateTime.now(ZoneId.of("Asia/Aqtau"));

        System.out.println("past = " + past);
        System.out.println("now = " + now);

        long hours = now.until(past, ChronoUnit.HOURS);
        System.out.println(hours);
    }

}
