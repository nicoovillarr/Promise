package com.nicoovillarr.promise;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class utils {

    public static String getDateString(LocalDate date) {
        Month month = date.getMonth();
        String monthDisplayName = month.getDisplayName(TextStyle.FULL, Locale.getDefault());

        String day;
        switch (date.getDayOfMonth()) {
            case 1:
                day = date.getDayOfMonth() + "st";
                break;
            case 2:
                day = date.getDayOfMonth() + "nd";
                break;
            case 3:
                day = date.getDayOfMonth() + "rd";
                break;
            default:
                day = date.getDayOfMonth() + "th";
                break;
        }

        return String.format("%s %s", monthDisplayName, day);
    }

}
