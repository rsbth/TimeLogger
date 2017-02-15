package com.mprtcz.timeloggerweb.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by mprtcz on 2017-02-15.
 */
public class UiStyles {

    public static Label getColoredLabel(String color) {
        Label label = new Label("____");
        label.addStyleName("color-Label mdl-shadow--4dp");
        label.getElement().getStyle().setBackgroundColor(color);
        label.getElement().getStyle().setColor(color);
        return label;
    }

    public static void stylizeTable(FlexTable table) {
        table.addStyleName("mdl-data-table mdl-js-data-table mdl-shadow--2dp");
    }

    public static Button getEditButton() {
        Button button = new Button("Edit");
        button.addStyleName("mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--primary");
        return button;
    }

    public static Button getRemoveButton() {
        Button button = new Button("Remove");
        button.addStyleName("mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent");
        return button;
    }
}
