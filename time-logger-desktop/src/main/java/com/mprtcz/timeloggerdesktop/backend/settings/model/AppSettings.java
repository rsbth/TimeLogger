package com.mprtcz.timeloggerdesktop.backend.settings.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mprtcz on 2017-01-10.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class AppSettings {
    public static final int MAX_VISIBLE_DAYS = 15;
    public static final int MIN_VISIBLE_DAYS = 5;

    LanguageEnum languageEnum;
    int numberOfVisibleDays;
    boolean isGraphicVisible;
    boolean headersVisible;
}
