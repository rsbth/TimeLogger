package com.mprtcz.timeloggerdesktop.frontend.utils;

import lombok.Getter;

import java.util.Random;

/**
 * Created by mprtcz on 2017-01-13.
 */
@Getter
public enum CustomColor {
    POMEGRANATE("#f44336"),
    SEANCE("#9c27b0"),
    SAN_MARINO("#3f51b5"),
    CERULEAN("#03a9f4"),
    PERSIAN_GREEN("#009688"),
    SUSHI("#8bc34a"),
    GORSE("#ffeb3b"),
    ORANGE_PEEL("#ff9800"),
    ROMAN_COFFEE("#795548"),
    LYNCH("#607d8b"),
    AMARANTH("#e91e63"),
    PURPLE_HEART("#673ab7"),
    DODGER_BLUE("#2196f3"),
    ROBINS_EGG_BLUE("#00bcd4"),
    FRUIT_SALAD("#4caf50"),
    PEAR("#cddc39"),
    AMBER("#ffc107"),
    ORANGE("#ff5722"),
    SILVER_CHALICE("#9e9e9e"),
    ;

    private String colorCode;

    CustomColor(String colorCode) {
        this.colorCode = colorCode;
    }

    public static CustomColor getRandomColor() {
        CustomColor[] colors = CustomColor.values();
        Random random = new Random();
        int randomIndex = random.nextInt(colors.length);
        return colors[randomIndex];
    }
}
