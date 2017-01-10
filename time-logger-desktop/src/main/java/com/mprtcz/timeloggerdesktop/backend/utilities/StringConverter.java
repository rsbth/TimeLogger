package com.mprtcz.timeloggerdesktop.backend.utilities;

/**
 * Created by mprtcz on 2017-01-03.
 */
public class StringConverter {

    public static String insertLineSeparator(String input, int maxLength) {
        char[] charArray = input.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        int separator = 0;
        for (int i = 0; i < charArray.length; i++) {
            stringBuilder.append(charArray[i]);
            if(charArray[i] == '\n') {
                separator = 0;
            }
            if(separator > maxLength) {
                if(charArray[i] == ' ') {
                    stringBuilder.append("\n");
                    separator = 0;
                }
            }
            separator++;
        }
        return stringBuilder.toString();
    }
}
