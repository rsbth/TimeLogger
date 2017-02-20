package com.mprtcz.timeloggerweb.client.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.addins.client.timepicker.MaterialTimePicker;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialDatePicker;
import gwt.material.design.client.ui.MaterialRow;

import java.util.Date;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class DateTimePicker {
    public static final Color BACKGROUND_COLOR = Color.BLUE_ACCENT_1;
    
    private MaterialDatePicker datePicker;
    private MaterialTimePicker timePicker;
    private Label dateTimeTextBox;

    public DateTimePicker(String text) {
        this.datePicker = new MaterialDatePicker();
        this.timePicker = new MaterialTimePicker();
        timePicker.setHour24(true);
        timePicker.setWidth("100px");
        datePicker.setWidth("200px");
        this.dateTimeTextBox = new Label(text);
        setErrorClearingOnClick();
    }
    
    public HorizontalPanel createDateTimePanel() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setWidth("0");
        timePicker.setIconType(IconType.ACCESS_TIME);
        timePicker.setTextColor(Color.BLACK);
        timePicker.setTitle("");
        datePicker.setIconType(IconType.TODAY);
        horizontalPanel.add(timePicker);
        horizontalPanel.add(datePicker);
        return horizontalPanel;
    }

    public MaterialRow getCompletePanel() {
        MaterialRow materialRow = new MaterialRow();
        materialRow.setStyleName("code");
        materialRow.getElement().getStyle().setWidth(360, Style.Unit.PX);
        materialRow.getElement().getStyle().setMarginTop(30, Style.Unit.PX);
        materialRow.setPadding(15);
        materialRow.getElement().getStyle().setBackgroundColor("white");
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(dateTimeTextBox);
        verticalPanel.add(createDateTimePanel());
        materialRow.add(verticalPanel);
        return materialRow;
    }

    public Date getSelectedValues() {
        Date date = this.datePicker.getDate();
        Date time  = this.timePicker.getValue();
        if(date == null || time == null) {return null;}
        GWT.log("Date = " + date + " time = " + time);
        DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyyMMdd");
        DateTimeFormat timeFormatter = DateTimeFormat.getFormat("HH");
        DateTimeFormat dateTimeFormatter = DateTimeFormat.getFormat("yyyyMMddHH");
        GWT.log("Formated date = " + dateFormatter.format(date));
        GWT.log("Formated time = " + timeFormatter.format(time));
        String dateTime = dateFormatter.format(date) + timeFormatter.format(time);
        GWT.log("String dateTime = " + dateTime);

        return dateTimeFormatter.parse(dateTime);
    }

    public MaterialDatePicker getDatePicker() {
        return datePicker;
    }

    public MaterialTimePicker getTimePicker() {
        return timePicker;
    }

    private void setErrorClearingOnClick() {
        timePicker.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                timePicker.clearErrorOrSuccess();
            }
        });
        datePicker.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                datePicker.clearErrorOrSuccess();
            }
        });
    }
}
