package com.mprtcz.timeloggerdesktop.backend.settings.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.j256.ormlite.field.DataType.ENUM_STRING;

/**
 * Created by mprtcz on 2017-01-10.
 */
@Getter
@Setter
@ToString
@DatabaseTable(tableName = "settings")
public class AppSettings {
    public static final int MAX_VISIBLE_DAYS = 15;
    public static final int MIN_VISIBLE_DAYS = 5;

    @DatabaseField(generatedId = true)
    Long id;

    @DatabaseField(canBeNull = false, dataType = ENUM_STRING)
    private Language language;

    @DatabaseField(canBeNull = false)
    private int numberOfVisibleDays;

    @DatabaseField(canBeNull = false)
    private boolean isGraphicVisible;

    @DatabaseField(canBeNull = false)
    private boolean headersVisible;

    public AppSettings() {}

    public AppSettings(Language language, int numberOfVisibleDays, boolean isGraphicVisible, boolean headersVisible) {
        this.language = language;
        this.numberOfVisibleDays = numberOfVisibleDays;
        this.isGraphicVisible = isGraphicVisible;
        this.headersVisible = headersVisible;
    }

    public static AppSettings getDefaultInstance() {
        Language BASIC_LOCALE = Language.ENGLISH;
        int DAYS_VISIBLE = 10;
        boolean IS_GRAPHIC_VISIBLE = true;
        boolean ARE_HEADERS_VISIBLE = true;
        return new AppSettings(BASIC_LOCALE, DAYS_VISIBLE, IS_GRAPHIC_VISIBLE, ARE_HEADERS_VISIBLE);
    }
}
