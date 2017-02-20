package com.mprtcz.timeloggerweb.client.application.task.uielements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTextBox;

/**
 * Created by mprtcz on 2017-02-20.
 */
public class NewTaskUI {

    private MaterialTextBox taskName = new MaterialTextBox();
    private MaterialTextBox taskDescription = new MaterialTextBox();
    private MaterialButton dropdownActivatorButton = new MaterialButton("Select Color");
    private MaterialDropDown colorsDropdown = new MaterialDropDown();
    private MaterialButton confirmButton = new MaterialButton("Add");
    private MaterialButton cancelButton = new MaterialButton("Cancel");


    public CellPanel constructUI() {
        CellPanel cellPanel = new VerticalPanel();
        initializeUiElements();
        cellPanel.add(taskName);
        cellPanel.add(taskDescription);
        cellPanel.add(dropdownActivatorButton);
        cellPanel.add(colorsDropdown);
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
        cellPanel.add(buttonsPanel);
        return cellPanel;
    }

    private void initializeUiElements() {
        this.taskDescription.setHelperText("Description");
        this.taskName.setHelperText("Name");
        this.dropdownActivatorButton.setActivates("colorsDropdown");
        this.colorsDropdown.setActivator("colorsDropdown");
        GWT.log(this.colorsDropdown.getActivator());
        this.colorsDropdown.setTextColor(Color.WHITE);
        populateColorDropdown();
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
            this.colorsDropdown.add(getColoredLink(color));
        }
        this.colorsDropdown.setConstrainWidth(false);
        this.colorsDropdown.addSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(SelectionEvent<Widget> event) {
                if(event.getSelectedItem() instanceof MaterialLink) {
                    Color c = Color.valueOf(((MaterialLink) event.getSelectedItem()).getText());
                    GWT.log("Color = " +c.name());
                    dropdownActivatorButton.setBackgroundColor(c);
                }
            }
        });
    }
}
