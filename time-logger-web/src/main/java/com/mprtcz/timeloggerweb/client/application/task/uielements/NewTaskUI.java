package com.mprtcz.timeloggerweb.client.application.task.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskOverlay;
import com.mprtcz.timeloggerweb.client.utils.ColorTranslator;
import gwt.material.design.addins.client.window.MaterialWindow;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.*;

import java.util.Date;

import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class NewTaskUI {
    private static final int FORM_MARGINS = 10;
    private static final int FORM_WIDTH = 280;
    private static final int LABEL_WIDTH = FORM_WIDTH - 2 * FORM_MARGINS;
    private static final int WINDOW_WIDTH = 320;
    private static final int WINDOW_HEIGHT = 288;
    private static final String BACKGROUND_COLOR = "white";
    private static final int BUTTON_WIDTH = 110;

    private MaterialTextBox taskName = new MaterialTextBox();
    private MaterialTextBox taskDescription = new MaterialTextBox();
    private MaterialButton dropdownActivatorButton = new MaterialButton("Select Color");
    private MaterialDropDown colorsDropdown = new MaterialDropDown();
    private MaterialButton confirmButton = new MaterialButton("Add");
    private MaterialButton cancelButton = new MaterialButton("Cancel");
    private MaterialWindow materialWindow;
    private Color selectedColor;

    public NewTaskUI(MaterialWindow materialWindow) {
        this.materialWindow = materialWindow;
        populateColorDropdown();
    }

    public MaterialPanel constructUI() {
        MaterialPanel materialPanel = new MaterialPanel();
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.getElement().getStyle().setWidth(FORM_WIDTH, PX);
        verticalPanel.getElement().getStyle().setBackgroundColor(BACKGROUND_COLOR);
        verticalPanel.setStyleName("add-task-panel");
        initializeUiElements();
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.getElement().getStyle().setMargin(FORM_MARGINS, PX);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
        verticalPanel.add(dropdownActivatorButton);
        verticalPanel.add(colorsDropdown);
        verticalPanel.add(taskName);
        verticalPanel.add(taskDescription);
        verticalPanel.add(buttonsPanel);
        materialPanel.add(verticalPanel);
        return materialPanel;
    }

    private void initializeUiElements() {
        this.taskDescription.setLabel("Description");
        this.taskName.setLabel("Name");
        this.dropdownActivatorButton.setActivates("colorsDropdown");
        this.colorsDropdown.setActivator("colorsDropdown");
        GWT.log(this.colorsDropdown.getActivator());
        this.colorsDropdown.setTextColor(Color.WHITE);
        this.taskDescription.getElement().getStyle().setMargin(FORM_MARGINS, PX);
        this.taskName.getElement().getStyle().setMargin(FORM_MARGINS, PX);
        this.dropdownActivatorButton.getElement().getStyle().setMargin(FORM_MARGINS, PX);
        this.dropdownActivatorButton.getElement().getStyle().setWidth(LABEL_WIDTH, PX);
        this.cancelButton.getElement().getStyle().setWidth(BUTTON_WIDTH, PX);
        this.confirmButton.getElement().getStyle().setWidth(BUTTON_WIDTH, PX);
        materialWindow.getElement().getStyle().setWidth(WINDOW_WIDTH, PX);
        materialWindow.getElement().getStyle().setHeight(WINDOW_HEIGHT, PX);
        materialWindow.getElement().getStyle().setBackgroundColor(BACKGROUND_COLOR);
        materialWindow.getContent().getElement().getStyle().setBackgroundColor(BACKGROUND_COLOR);
        addButtonsListeners();
    }

    private void addButtonsListeners() {
        this.cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Adding clock listener");
                materialWindow.close();
            }
        });

        this.confirmButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String colorCode  = ColorTranslator.getCodeOfColor(selectedColor);

                if(!validateNewTask()) {return;}

                TaskOverlay taskOverlay = JavaScriptObject.createObject().cast();

                taskOverlay.setName(taskName.getText());
                taskOverlay.setDescription(taskDescription.getText());
                taskOverlay.setColor(colorCode);
                taskOverlay.setLastModified(new Date());

                GWT.log(taskOverlay.getStringInfo());
            }
        });
    }

    private MaterialLink getColoredLink(Color color) {
        GWT.log("NewTaskUI.getColoredLink");
        MaterialLink materialLink = new MaterialLink(color.name());
        materialLink.setIconType(IconType.COLOR_LENS);
        materialLink.setIconColor(color);
        return materialLink;
    }

    private void populateColorDropdown() {
        GWT.log("NewTaskUI.populateColorDropdown");
        Color[] colors = Color.values();
        for (Color color : colors) {
            GWT.log("Color = " + color);
            if(color == Color.DEFAULT) {continue;}
            if(color == Color.TRANSPARENT) {continue;}
            this.colorsDropdown.add(getColoredLink(color));
        }

        this.colorsDropdown.addSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(SelectionEvent<Widget> event) {
                if (event.getSelectedItem() instanceof MaterialLink) {
                    Color c = Color.valueOf(((MaterialLink) event.getSelectedItem()).getText());
                    selectedColor = c;
                    GWT.log("Color = " + c.name());
                    dropdownActivatorButton.setBackgroundColor(c);
                    if(isColorDim(c)) {
                        dropdownActivatorButton.setTextColor(Color.GREY_LIGHTEN_3);
                    } else {
                        dropdownActivatorButton.setTextColor(Color.GREY_DARKEN_3);
                    }
                }
            }
        });

    }

    private boolean isColorDim(Color color) {
        return color.name().toLowerCase().contains("darken") || color.name().toLowerCase().contains("black");
    }

    private boolean validateNewTask() {
        boolean result = true;
        if(this.taskName.getText() == null || this.taskName.getText().equals("")) {
            this.taskName.setError("Missed a value here");
            result = false;
        }
        if(selectedColor == null) {
            this.dropdownActivatorButton.setTextColor(Color.RED);
            result = false;
        }
        return result;
    }
}
