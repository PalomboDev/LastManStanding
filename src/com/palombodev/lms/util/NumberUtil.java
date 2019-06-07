package com.palombodev.lms.util;

public class NumberUtil {

    public static boolean isStringInteger(String string) {
        try {
            Integer.valueOf(string);
        } catch (NumberFormatException exception) {
            return false;
        }

        return true;
    }

    public static String formatTime(int time) {
        int hours = time / 3600;
        int remainder = time % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;
        String disHour = (hours < 10 ? "0" : "") + hours;
        String disMinu = (minutes < 10 ? "0" : "") + minutes;
        String disSeco = (seconds < 10 ? "0" : "") + seconds;
        String formatted = "";

        if (hours != 0) formatted += disHour + " hours ";
        if (minutes != 0) formatted += disMinu + " minutes ";
        if (seconds != 0) formatted += disSeco + " seconds";

        if (time <= 60) {

            if (time == 1) {
                return "1 second";
            }

            return time + " seconds";

        }

        return formatted;
    }
}