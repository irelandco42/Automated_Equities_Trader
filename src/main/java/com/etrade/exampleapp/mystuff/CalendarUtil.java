package com.etrade.exampleapp.mystuff;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

    static String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "June", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String getDateTimeString() {
        return new Date().toString();
    }

    public static String getTime() {
        return getDateTimeString().substring(11, 19);
    }

    public static String getDate() {
        String raw = new Date().toString();
        String month_num = Integer.toString(Arrays.asList(months).indexOf(raw.substring(4, 7)));
        String day = raw.substring(8, 10);
        String year = raw.substring(24);

        return (month_num.length() == 1 ? "0" + month_num : month_num) + "/" + day + "/" + year;
    }

    public static boolean isWeekday(String date) {
        Date d1 = new Date(date);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        return (c1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) && c1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
    }

    public static boolean isWeekday() {
        return isWeekday(getDate());
    }
}
