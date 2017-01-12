package com.mprtcz.timeloggerdesktop.backend.settings.model;

import lombok.Getter;

import java.util.Locale;

/**
 * Created by mprtcz on 2017-01-10.
 */
@Getter
public enum Language {
    POLISH("Polish", new Locale("pl", "PL")),
    ENGLISH("English", new Locale("en", "US"));

    private String name;
    private Locale locale;

    Language(String name, Locale locale) {
        System.out.println("name = " + name);
        System.out.println("locale = " + locale);
        this.name = name;
        this.locale = locale;
    }
}
