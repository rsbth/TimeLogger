package com.mprtcz.timeloggerdesktop.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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

    private static final boolean DRAW_HEADERS = true;
    private static final String FONT = "Roboto";
    private static final String FONT_COLOR = "darkgray";
    private static final int BASIC_CELL_HEIGHT = 10;
    private static final int VISIBLE_DAYS = 7;

    private List<Activity> allActivities;
    private List<Record> allRecords;
    private LocalDateTime earliest;
    private LocalDateTime latest;
    private int basicCellHeight = 10;
    private int headerHeight = 10;

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
        System.out.println("hoursDelta = " + hoursDelta);
        LocalDateTime currentHour = earliest;
        this.hours = new ArrayList<>();
        for (int i = 0; i < hoursDelta; i++) {
            Hour hour = new Hour(currentHour);
            this.hours.add(hour);
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
    public static class Hour {
        List<Activity> activitiesDuringThisHour;
        LocalDateTime datetime;

        public Hour(LocalDateTime datetime) {
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
            if (hourToFind.equals(hour.getDatetime())) {
                return this.hours.indexOf(hour);
            }
        }
        return -1;
    }

    public void calculatePositionsAndDraw(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        this.basicCellHeight = (int) (canvas.getHeight() - headerHeight) / VISIBLE_DAYS;
        int unitWidth = (int) width / 26;
        if (DRAW_HEADERS) {
            drawHeader(graphicsContext, unitWidth);
        }
        for (int i = (calculateDrawingStartingPoint()); i < this.hours.size(); i++) {
            Hour hourObject = this.hours.get(i);
            drawOnCanvas(graphicsContext, hourObject, unitWidth);
        }
    }

    private void drawOnCanvas(GraphicsContext graphicsContext, Hour hourObject, int cellWidth) {
        String color = determineCellColor(hourObject);
        long hour = hourObject.getDatetime().getHour();
        long dayDelta = getDayDelta(hourObject);
        graphicsContext.setFill(Paint.valueOf(color));
        graphicsContext.fillRect(cellWidth * (hour + 2), basicCellHeight * (dayDelta + 1) + headerHeight, cellWidth, basicCellHeight);
        String day = hourObject.getDatetime().getDayOfMonth() + "." + hourObject.getDatetime().getMonthValue();
        graphicsContext.setFill(Paint.valueOf(FONT_COLOR));
        if (DRAW_HEADERS) {
            graphicsContext.setFont(Font.font(FONT));
            graphicsContext.fillText(day, 0, (dayDelta + 2) * basicCellHeight + headerHeight);
        }
    }

    private long getDayDelta(Hour hourObject) {
        LocalDateTime earliestModulus = this.hours.get(calculateDrawingStartingPoint()).getDatetime();
        earliestModulus = earliestModulus.minusHours(earliestModulus.getHour());
        return earliestModulus.until(hourObject.getDatetime(), ChronoUnit.DAYS);
    }

    private String determineCellColor(Hour hour) {
        if (hour.getActivitiesDuringThisHour().size() > 0) {
            return hour.getActivitiesDuringThisHour().get(0).getColor();
        } else {
            return "#ffffff";
        }
    }

    private void drawHeader(GraphicsContext graphicsContext, int unit) {
        graphicsContext.setFill(Paint.valueOf(FONT_COLOR));
        graphicsContext.setFont(Font.font(FONT));
        int leftOffset = 2;
        for (int i = leftOffset; i < 26; i++) {
            graphicsContext.fillText(String.valueOf(i - leftOffset), unit * i, headerHeight, (unit - 2));
        }
    }

    public int calculateDrawingStartingPoint() {
        if(this.hours.size() < (VISIBLE_DAYS * 24)) {
            return 0;
        } else {
            int surplusHours = this.hours.size() % 24;
            if(surplusHours == 0) {
                return this.hours.size() - (VISIBLE_DAYS * 24) - 2;
            } else {
                return this.hours.size() - surplusHours - ((VISIBLE_DAYS - 1) * 24) - 2;
            }
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
