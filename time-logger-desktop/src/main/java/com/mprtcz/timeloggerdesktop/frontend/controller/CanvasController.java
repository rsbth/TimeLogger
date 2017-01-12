package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class CanvasController {
    private Logger logger = LoggerFactory.getLogger(CanvasController.class);


    private static final boolean DRAW_HEADERS = true;
    private static final String FONT = "Roboto";
    private static final String FONT_COLOR = "darkgray";
    private static final int BASIC_CELL_HEIGHT = 10;

    private int visibleDays = 5;
    private int basicCellHeight = 10;
    private int headerHeight = 10;

    private List<HoursData.Hour> hours = new ArrayList<>();

    void calculatePositionsAndDraw(List<HoursData.Hour> hours, Canvas canvas) {
        this.setCanvasTooltip(canvas);
        this.hours = hours;
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double width = canvas.getWidth();
        logger.debug("width = {}", width);
        this.basicCellHeight = (int) (canvas.getHeight() - headerHeight) / visibleDays;
        logger.info("basic cell height = {}", basicCellHeight);
        int unitWidth = (int) width / 26;
        logger.info("unitwidth = {} ", unitWidth);
        if (DRAW_HEADERS) {
            logger.info("DRAW_HEADERS = {}", DRAW_HEADERS);
            drawHeader(graphicsContext, unitWidth);
        }
        this.cells = new ArrayList<>();
        for (int i = (calculateDrawingStartingPoint()); i < this.hours.size(); i++) {
            HoursData.Hour hourObject = this.hours.get(i);
            logger.info("hourObject = {}", hourObject.toString());
            drawOnCanvas(graphicsContext, hourObject, unitWidth);
        }
    }

    public void convertHoursDataToHoursArray(HoursData hoursData, Canvas canvas) {
        LocalDateTime earliest = hoursData.getEarliest();
        LocalDateTime latest = hoursData.getLatest();
        long dayDelta = calculateDayDelta(earliest, latest);
        int dayDeltaInt = Math.toIntExact(dayDelta);

        logger.info("dayDeltaInt = {}", dayDeltaInt);

        HoursData.Hour[][] hoursArray = new HoursData.Hour[dayDeltaInt + 1][24];

        for(HoursData.Hour hourObject : hoursData.getHours()) {
            int hour = hourObject.getDatetime().getHour();
            int objectsDayDelta = Math.toIntExact(calculateDayDelta(earliest, hourObject.getDatetime()));
            logger.info("objectsDayDelta = {}", objectsDayDelta);

            hoursArray[objectsDayDelta][hour] = hourObject;
        }


        for (int i = 0; i < hoursArray.length; i++) {
            for (int j = 0; j < hoursArray[i].length; j++) {
                if(hoursArray[i][j] == null) {
                    logger.info("void");
                } else {
                    logger.info(hoursArray[i][j].getDatetime().toString());
                }
            }
        }

        drawArrayOnCanvas(hoursArray, canvas);
    }

    private void drawArrayOnCanvas(HoursData.Hour[][] hoursArray, Canvas canvas) {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int startingDayIndex = getDrawStartingDay(hoursArray.length);
        logger.info("startingDay = {}", startingDayIndex);
        for (int i = startingDayIndex; i < hoursArray.length; i++) {
            for (int j = 0; j < hoursArray[i].length; j++) {
                drawArrayCellOnCanvas(canvas, hoursArray[i][j], j , i);
            }
        }
    }

    private int getDrawStartingDay(int arrayLength) {
        logger.info("arrayLength = {}", arrayLength);
        logger.info("visibleDays = {}", visibleDays);
        if(visibleDays > arrayLength) {
            return 0;
        } else {
            return arrayLength - visibleDays;
        }
    }

    private void drawArrayCellOnCanvas(Canvas canvas, HoursData.Hour hour, int xCoordinate, int yCoordinate) {
        int cellWidth = (int) (canvas.getWidth() / 26);
        int xOffset = 0;
        int yOffset = 0;

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        String color = determineCellColor(hour);
        graphicsContext.setFill(Paint.valueOf(color));
        graphicsContext.fillRect((cellWidth * xCoordinate) + xOffset, (basicCellHeight * yCoordinate) + yOffset,
                cellWidth, basicCellHeight);
    }

    private long calculateDayDelta(LocalDateTime earliest, LocalDateTime current) {
        LocalDateTime earliestModulus = earliest;
        earliestModulus = earliestModulus.minusHours(earliestModulus.getHour());
        return earliestModulus.until(current, DAYS);
    }

    List<CanvasCell> cells;

    private void drawOnCanvas(GraphicsContext graphicsContext, HoursData.Hour hourObject, int cellWidth) {
        logger.info("hourObject = {}", hourObject);
        logger.info("cellWidth = {}", cellWidth);
        String color = determineCellColor(hourObject);
        long hour = hourObject.getDatetime().getHour();
        logger.info("hour = {}", hour);
        long dayDelta = getDayDelta(hourObject);
        graphicsContext.setFill(Paint.valueOf(color));
        cells.add(new CanvasCell(cellWidth * (hour + 2), basicCellHeight * (dayDelta + 1) + headerHeight, cellWidth, basicCellHeight, color));
        graphicsContext.fillRect(cellWidth * (hour + 2), basicCellHeight * (dayDelta + 1) + headerHeight, cellWidth, basicCellHeight);
        logger.info("cellWidth * (hour + 2) = {}, basicCellHeight * (dayDelta + 1) + headerHeight = {}, cellWidth = {}, basicCellHeight = {}",
                cellWidth * (hour + 2), basicCellHeight * (dayDelta + 1) + headerHeight, cellWidth, basicCellHeight);
        String day = hourObject.getDatetime().getDayOfMonth() + "." + hourObject.getDatetime().getMonthValue();
        graphicsContext.setFill(Paint.valueOf(FONT_COLOR));
        if (DRAW_HEADERS && (dayDelta % 2 == 0)) {
            graphicsContext.setFont(Font.font(FONT));
            graphicsContext.fillText(day, 0, (dayDelta + 2) * basicCellHeight + headerHeight);
        }
    }

    private long getDayDelta(HoursData.Hour hourObject) {
        LocalDateTime earliestModulus = this.hours.get(calculateDrawingStartingPoint()).getDatetime();
        earliestModulus = earliestModulus.minusHours(earliestModulus.getHour());
        return earliestModulus.until(hourObject.getDatetime(), DAYS);
    }

    private String determineCellColor(HoursData.Hour hour) {
        if(hour == null) {
            return "#ffffff";
        }
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
        for (int i = leftOffset; i < 26; i=i+2) {
            graphicsContext.fillText(String.valueOf(i - leftOffset), unit * i, headerHeight, (unit - 2));
        }
    }

    private int calculateDrawingStartingPoint() {
        if(this.hours.size() < (visibleDays * 24)) {
            return 0;
        } else {
            int surplusHours = this.hours.size() % 24;
            if(surplusHours == 0) {
                return this.hours.size() - (visibleDays * 24) - 2;
            } else {
                return this.hours.size() - surplusHours - ((visibleDays - 1) * 24) - 2;
            }
        }
    }

    public void setVisibleDays(int visibleDays) {
        logger.info("visibleDays = {}", visibleDays);
        this.visibleDays = visibleDays;
    }

    void setCanvasTooltip(Canvas canvas){
        Tooltip tooltip = new Tooltip("Something");
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tooltip.setText(String.valueOf(event.getY()) + ", " + String.valueOf(event.getX()));
            }
        });
        Tooltip.install(canvas, tooltip); //TODO tooltip
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class CanvasCell {
        double xPosition;
        double yPosition;
        double width;
        double height;
        String color;

        @Override
        public String toString() {
            return "CanvasCell{" +
                    "xPosition=" + xPosition +
                    ", yPosition=" + yPosition +
                    ", width=" + width +
                    ", height=" + height +
                    ", color='" + color + '\'' +
                    "}\n";
        }
    }
}
