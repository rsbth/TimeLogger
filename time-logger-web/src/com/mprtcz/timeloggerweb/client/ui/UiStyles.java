package com.mprtcz.timeloggerweb.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by mprtcz on 2017-02-15.
 */
public class UiStyles {

    public static Label getColoredLabel(String color) {
        Label label = new Label("____");
        label.getElement().getStyle().setBackgroundColor(color);
        label.getElement().getStyle().setColor(color);
        return label;
    }

    public static Button getEditButton() {
        Button button = new Button("Edit");
        button.getElement().getStyle().setPadding(5, Style.Unit.PX);
        button.getElement().getStyle().setMargin(5, Style.Unit.PX);
        button.getElement().getStyle().setBackgroundColor("#2196F3");
        return button;
    }

    public static Button getRemoveButton() {
        Button button = new Button("Remove");
//        button.getElement().getStyle().setPadding(5, Style.Unit.PX);
//        button.getElement().getStyle().setMargin(5, Style.Unit.PX);
//        button.getElement().getStyle().setBackgroundColor("#EC407A");
        button.addStyleName("my-Button");
        return button;
    }
}
