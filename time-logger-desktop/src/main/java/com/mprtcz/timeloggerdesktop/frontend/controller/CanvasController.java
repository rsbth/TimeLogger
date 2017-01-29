package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class CanvasController {
    private Logger logger = LoggerFactory.getLogger(CanvasController.class);


    private boolean areHeadersEnabled = true;
    private static final String FONT = "Roboto";
    private static final String FONT_COLOR = "darkgray";
    private static final int BASIC_CELL_HEIGHT = 10;
    private static final int MAX_CELL_HEIGHT = 15;

    private int visibleDays = 5;
    private int basicCellHeight = 10;
    private int headerHeight = 0;
    private int cellWidth = 0;
    private int leftLegendWidth = 0;

    private HoursData.Hour[][] trimmedHourArray;

    public void drawArrayOnCanvas(HoursData.Hour[][] hoursArray, Canvas canvas) {
        logger.info("areHeadersEnabled = {}", areHeadersEnabled);
        logger.info("hours array = " + Arrays.deepToString(hoursArray));
        this.setCanvasTooltip(canvas);
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int startingDayIndex = getDrawStartingDay(hoursArray.length);

        int xOffset = 0;
        int yOffset = 0;
        this.cellWidth = (int) (canvas.getWidth() / 24);

        this.setTrimmedArray(hoursArray);
        basicCellHeight = (int) (canvas.getHeight() / this.trimmedHourArray.length);
        if(basicCellHeight > MAX_CELL_HEIGHT) {
            basicCellHeight = MAX_CELL_HEIGHT;
        }

        if (areHeadersEnabled) {
            this.cellWidth = (int) (canvas.getWidth() / 27);
            xOffset = 3;
            yOffset = 1;
            leftLegendWidth = xOffset * cellWidth;
            headerHeight = yOffset * BASIC_CELL_HEIGHT;
            basicCellHeight = (int) ((canvas.getHeight() - (yOffset * BASIC_CELL_HEIGHT)) / this.trimmedHourArray.length);
            logger.info("basicCellHeight = {}", basicCellHeight);
            if(basicCellHeight > MAX_CELL_HEIGHT) {
                basicCellHeight = MAX_CELL_HEIGHT;
            }
            drawHeader(canvas.getGraphicsContext2D(), cellWidth, xOffset);
        }

        logger.info("startingDay = {}", startingDayIndex);
        for (int i = 0; i < this.trimmedHourArray.length; i++) {
            for (int j = 0; j < this.trimmedHourArray[i].length; j++) {
                drawArrayCellOnCanvas(canvas, this.trimmedHourArray[i][j], j, i, xOffset, yOffset, cellWidth);
            }
        }
    }

    private int getDrawStartingDay(int arrayLength) {
        logger.debug("arrayLength = {}", arrayLength);
        logger.debug("visibleDays = {}", visibleDays);
        if (visibleDays > arrayLength) {
            return 0;
        } else {
            return arrayLength - visibleDays;
        }
    }

    private void setTrimmedArray(HoursData.Hour[][] hourArray) {
        this.trimmedHourArray = new HoursData.Hour[hourArray.length - getDrawStartingDay(hourArray.length)][hourArray[0].length];
        int index = 0;
        for (int i = getDrawStartingDay(hourArray.length); i < hourArray.length; i++) {
            this.trimmedHourArray[index] = hourArray[i];
            index++;
        }
    }

    private void drawArrayCellOnCanvas(Canvas canvas, HoursData.Hour hour,
                                       int xCoordinate, int yCoordinate,
                                       int xOffset, int yOffset, int cellWidth) {

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        String color = determineCellColor(hour);
        graphicsContext.setFill(Paint.valueOf(color));
        graphicsContext.fillRect((cellWidth * xCoordinate) + (xOffset * cellWidth),
                (basicCellHeight * yCoordinate) + (yOffset * BASIC_CELL_HEIGHT),
                cellWidth, basicCellHeight);
        if (areHeadersEnabled && (yCoordinate % 2 == 0) && hour != null) {
            String dayString = hour.getDatetime().getDayOfMonth() + "." + getMonthValue(hour);
            graphicsContext.setFont(Font.font(FONT));
            graphicsContext.setFill(Paint.valueOf(FONT_COLOR));
            graphicsContext.fillText(dayString, 0, (yCoordinate + 1) * basicCellHeight + (headerHeight));
        }
    }

    private String getMonthValue(HoursData.Hour hour) {
        int month = hour.getDatetime().getMonthValue();
        if(month > 9) {
            return String.valueOf(month);
        } else {
            return "0" + String.valueOf(month);
        }
    }

    private String determineCellColor(HoursData.Hour hour) {
        if (hour == null) {
            return "#ffffff";
        }
        if (hour.getActivitiesDuringThisHour().size() > 0) {
            return hour.getActivitiesDuringThisHour().get(0).getColor();
        } else {
            return "#ffffff";
        }
    }

    private void drawHeader(GraphicsContext graphicsContext, int unit, int leftOffset) {
        logger.info("leftOffset = {}", leftOffset);
        graphicsContext.setFill(Paint.valueOf(FONT_COLOR));
        graphicsContext.setFont(Font.font(FONT));
        int tick = 2;
        for (int i = leftOffset; i < (24 + leftOffset); i = (i + tick)) {
            graphicsContext.fillText(String.valueOf(i - leftOffset), unit * i, headerHeight, (unit - 2));
        }
    }


    public void setVisibleDays(int visibleDays) {
        logger.info("visibleDays = {}", visibleDays);
        this.visibleDays = visibleDays;
    }

    private void setCanvasTooltip(Canvas canvas) {
        Tooltip tooltip = new Tooltip("Activity info");
        tooltip.setFont(Font.font(FONT));
        hackTooltipStartTiming(tooltip);
        canvas.setOnMouseMoved(event -> {
            int x;
            int y;
            if(areHeadersEnabled) {
                x = (int) (event.getX() - leftLegendWidth)/ cellWidth;
                y = (int) (event.getY() - headerHeight)/ basicCellHeight;
            } else {
                x = (int) event.getX() / cellWidth;
                y = (int) event.getY() / basicCellHeight;
            }
            if(x >= trimmedHourArray[0].length || x < 0) {
                tooltip.hide();
                return;}
            if(y >= trimmedHourArray.length || y < 0) {
                tooltip.hide();
                return;}
            if (trimmedHourArray[y][x] == null) {
                tooltip.hide();
                return;}
            String activityName;
            if(trimmedHourArray[y][x].getActivitiesDuringThisHour().size() == 0) {
                activityName = "no activities";
            } else {
                activityName = trimmedHourArray[y][x].getActivitiesDuringThisHour().get(0).getName();
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm");
            String result = trimmedHourArray[y][x].getDatetime().format(formatter);
            tooltip.setText(activityName + "\n" + result);
        });
        canvas.setOnMouseExited(event -> tooltip.hide());
        Tooltip.install(canvas, tooltip);
    }

    public void setHeadersVisibility(boolean headersVisible) {
        logger.info("setting headersVisible = {}", headersVisible);
            this.areHeadersEnabled = headersVisible;
    }

    public static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(10)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
