package timelogger.mprtcz.com.timelogger.graphics.controllers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Arrays;

import timelogger.mprtcz.com.timelogger.task.model.HoursData;

/**
 * Created by Azet on 2017-01-21.
 */

public class GraphicController {
    private static final String TAG = "GraphicController";
    private boolean areHeadersEnabled = true;
    private static final String FONT = "Roboto";
    private static final String FONT_COLOR = "darkgray";
    private static final int BASIC_CELL_HEIGHT = 10;
    private static final int MAX_CELL_HEIGHT = 40;

    private int visibleDays = 5;
    private int basicCellHeight = 10;
    private int headerHeight = 0;
    private int cellWidth = 0;
    private int leftLegendWidth = 0;

    private Paint paint;

    private HoursData.Hour[][] trimmedHourArray;
    private HoursData.Hour[][] hoursArray;

    public GraphicController(HoursData.Hour[][] hoursArray) {
        this.hoursArray = hoursArray;
        this.paint = new Paint();
    }

    public void drawArrayOnCanvas(Canvas canvas) {
        Log.d(TAG, "drawArrayOnCanvas");
        Log.d(TAG, "Hours array = " + Arrays.deepToString(this.hoursArray));
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
            xOffset = 1;
            yOffset = 1;
            leftLegendWidth = xOffset * cellWidth;
            headerHeight = yOffset * BASIC_CELL_HEIGHT;
            basicCellHeight = (int) ((canvas.getHeight() - (yOffset * BASIC_CELL_HEIGHT)) / this.trimmedHourArray.length);
            if(basicCellHeight > MAX_CELL_HEIGHT) {
                basicCellHeight = MAX_CELL_HEIGHT;
            }
            drawHeader(canvas, cellWidth, xOffset);
        }

        for (int i = 0; i < this.trimmedHourArray.length; i++) {
            for (int j = 0; j < this.trimmedHourArray[i].length; j++) {
                drawArrayCellOnCanvas(canvas, this.trimmedHourArray[i][j], j, i, xOffset, yOffset, cellWidth);
            }
        }

    }

    private void drawHeader(Canvas canvas, int unit, int leftOffset) {
        int tick = 2;
        paint.setColor(Color.parseColor("#000000"));
        for (int i = leftOffset; i < (24 + leftOffset); i = (i + tick)) {
            canvas.drawText(String.valueOf(i - leftOffset), unit * i, basicCellHeight, paint);
        }
    }

    private int getDrawStartingDay(int arrayLength) {
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
        Log.d(TAG, "drawArrayCellOnCanvas");
        yOffset = 0;
        String color = determineCellColor(hour);

        paint.setColor(Color.parseColor(color));
        DrawingCoordinates coords = new DrawingCoordinates(xCoordinate, xOffset, yCoordinate, yOffset, paint);
        Log.d(TAG, "Coords to string = " +coords.toString());

        canvas.drawRect( coords.X0,coords.Y0,coords.width,coords.height, coords.paint);
        if (areHeadersEnabled &&  hour != null) {
            String dayString = hour.getDatetime().getDayOfMonth() + "." + getMonthValue(hour);
            paint.setColor(Color.parseColor("#000000"));
            paint.setTextSize(20f);
            canvas.drawText(dayString, 0, (yCoordinate + 1) * basicCellHeight + (headerHeight), paint);
        }
    }

    private String getMonthValue(HoursData.Hour hour) {
        int month = hour.getDatetime().getMonthOfYear();
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

    private class DrawingCoordinates {
        float X0;
        float Y0;
        float width;
        float height;
        Paint paint;

        DrawingCoordinates(int xCoordinate, int xOffset,
                           int yCoordinate, int yOffset, Paint paint) {
            this.X0 = (cellWidth * xCoordinate) + (xOffset * cellWidth);
            this.Y0 = (basicCellHeight * yCoordinate) + (yOffset * BASIC_CELL_HEIGHT) +basicCellHeight;
            this.width = X0 + cellWidth;
            this.height = Y0 + basicCellHeight;
            this.paint = paint;
        }

        String getBasics() {
            return String.valueOf(X0) +"+"+ String.valueOf(Y0);
        }

        @Override
        public String toString() {
            return "DrawingCoordinates{" +
                    "X0=" + X0 +
                    ", Y0=" + Y0 +
                    ", width=" + width +
                    ", height=" + height +
                    ", paint=" + paint.getColor() +
                    '}';
        }
    }


}
