package com.mprtcz.timeloggerdesktop.frontend.controller;

import com.mprtcz.timeloggerdesktop.backend.activity.model.HoursData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mprtcz on 2017-01-10.
 */
public class CanvasController {

    private static final boolean DRAW_HEADERS = true;
    private static final String FONT = "Roboto";
    private static final String FONT_COLOR = "darkgray";
    private static final int BASIC_CELL_HEIGHT = 10;
    private static final int VISIBLE_DAYS = 7;

    private int basicCellHeight = 10;
    private int headerHeight = 10;


    private List<HoursData.Hour> hours = new ArrayList<>();

    CanvasController(List<HoursData.Hour> hours) {
        this.hours = hours;
    }

    void calculatePositionsAndDraw(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        this.basicCellHeight = (int) (canvas.getHeight() - headerHeight) / VISIBLE_DAYS;
        int unitWidth = (int) width / 26;
        if (DRAW_HEADERS) {
            drawHeader(graphicsContext, unitWidth);
        }
        for (int i = (calculateDrawingStartingPoint()); i < this.hours.size(); i++) {
            HoursData.Hour hourObject = this.hours.get(i);
            drawOnCanvas(graphicsContext, hourObject, unitWidth);
        }
    }

    private void drawOnCanvas(GraphicsContext graphicsContext, HoursData.Hour hourObject, int cellWidth) {
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

    private long getDayDelta(HoursData.Hour hourObject) {
        LocalDateTime earliestModulus = this.hours.get(calculateDrawingStartingPoint()).getDatetime();
        earliestModulus = earliestModulus.minusHours(earliestModulus.getHour());
        return earliestModulus.until(hourObject.getDatetime(), ChronoUnit.DAYS);
    }

    private String determineCellColor(HoursData.Hour hour) {
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

    private int calculateDrawingStartingPoint() {
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
}
