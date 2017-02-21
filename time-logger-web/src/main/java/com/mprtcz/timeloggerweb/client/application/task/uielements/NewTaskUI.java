package com.mprtcz.timeloggerweb.client.application.task.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mprtcz.timeloggerweb.client.application.task.model.TaskDto;
import com.mprtcz.timeloggerweb.client.utils.ColorTranslator;
import gwt.material.design.addins.client.window.MaterialWindow;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTextBox;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class NewTaskUI {
    private static final int FORM_MARGIN = 20;
    private static final int FORM_WIDTH = 280;

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

    public VerticalPanel constructUI() {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.getElement().getStyle().setWidth(FORM_WIDTH, PX);
        verticalPanel.getElement().getStyle().setMargin(FORM_MARGIN, PX);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initializeUiElements();
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
        verticalPanel.add(dropdownActivatorButton);
        verticalPanel.add(colorsDropdown);
        verticalPanel.add(taskName);
        verticalPanel.add(taskDescription);
        verticalPanel.add(buttonsPanel);
        return verticalPanel;
    }

    private void initializeUiElements() {
        this.taskDescription.setLabel("Description");
        this.taskName.setLabel("Name");
        this.dropdownActivatorButton.setActivates("colorsDropdown");
        this.colorsDropdown.setActivator("colorsDropdown");
        GWT.log(this.colorsDropdown.getActivator());
        this.colorsDropdown.setTextColor(Color.WHITE);
        this.taskDescription.getElement().getStyle().setMarginBottom(10, PX);
        this.taskName.getElement().getStyle().setMarginBottom(10, PX);
        this.dropdownActivatorButton.getElement().getStyle().setMarginBottom(10, PX);
        this.dropdownActivatorButton.getElement().getStyle().setWidth(100, PCT);
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
                String color  = ColorTranslator.getCodeOfColor(selectedColor);
                TaskDto taskDto = new TaskDto(taskName.getText(), taskDescription.getText(), color);
                GWT.log("Task name = " +taskDto.getName());

                GWT.log("Task name = " +taskDto.getName());
                GWT.log("Task description = " +taskDto.getDescription());
                GWT.log("Task color = " +taskDto.getColor());
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
        this.colorsDropdown.setConstrainWidth(false);
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
}
