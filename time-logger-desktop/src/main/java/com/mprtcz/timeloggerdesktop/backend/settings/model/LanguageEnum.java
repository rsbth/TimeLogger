package com.mprtcz.timeloggerdesktop.backend.settings.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mprtcz on 2017-01-10.
 */
@Getter
public enum LanguageEnum {
    POLISH("Polish", new Locale("pl", "PL")),
    ENGLISH("English", new Locale("en", "US"));

    private String name;
    private Locale locale;

    LanguageEnum(String name, Locale locale) {
        this.name = name;
        this.locale = locale;
    }

    private static final Map<String, LanguageEnum> map = new HashMap<>(values().length, 1);
    static {
        for (LanguageEnum c : values()) map.put(c.name, c);
    }

    public static LanguageEnum of(String name) {
        LanguageEnum result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("Invalid category name: " + name);
        }
        return result;
    }
}
