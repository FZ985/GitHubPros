package com.kmtlibs.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTime {
    public static final long DAY = 86400000L;
    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT_DATE_TIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String FORMAT_SHORT_TIME = "HH:mm";
    private static final String FORMAT_TIME = "HH:mm:ss";
    public static final long HALF_DAY = 43200000L;
    public static final long WEEKLY = 604800000L;
    private static TimeZone gDefaultTimeZone;
    private static SimpleDateFormat gFormatter = new SimpleDateFormat();
    static {
        gDefaultTimeZone = TimeZone.getTimeZone("GMT+8");
        gFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static String format(String paramString) {
        return format(paramString, new Date());
    }

    public static String format(String paramString, long paramLong) {
        return format(paramString, new Date(paramLong));
    }

    public static String format(String paramString, long paramLong, TimeZone paramTimeZone) {
        return format(paramString, new Date(paramLong), paramTimeZone);
    }

    public static String format(String paramString, Date paramDate) {
        return format(paramString, paramDate, gDefaultTimeZone);
    }

    public static String format(String paramString, Date paramDate, TimeZone paramTimeZone) {
        synchronized (gFormatter) {
            gFormatter.setTimeZone(paramTimeZone);
            gFormatter.applyPattern(paramString);
            String str = gFormatter.format(paramDate);
            return str;
        }
    }

    public static String format(String paramString, TimeZone paramTimeZone) {
        return format(paramString, new Date(), paramTimeZone);
    }

    public static String toDate() {
        return format("yyyy-MM-dd", new Date());
    }

    public static String toDate(long paramLong) {
        return format("yyyy-MM-dd", new Date(paramLong));
    }

    public static String toDate(Date paramDate) {
        return format("yyyy-MM-dd", paramDate);
    }

    public static String toDateTime() {
        return format("yyyy-MM-dd HH:mm:ss", new Date());
    }

    public static String toDateTime(long paramLong) {
        return format("yyyy-MM-dd HH:mm:ss", new Date(paramLong));
    }

    public static String toDateTime(Date paramDate) {
        return format("yyyy-MM-dd HH:mm:ss", paramDate);
    }

    public static String toDateTimeMs() {
        return format("yyyy-MM-dd HH:mm:ss.SSS", new Date());
    }

    public static String toDateTimeMs(long paramLong) {
        return format("yyyy-MM-dd HH:mm:ss.SSS", new Date(paramLong));
    }

    public static String toDateTimeMs(Date paramDate) {
        return format("yyyy-MM-dd HH:mm:ss.SSS", paramDate);
    }

    public static String toShortTime() {
        return format("HH:mm", new Date());
    }

    public static String toShortTime(long paramLong) {
        return format("HH:mm", new Date(paramLong));
    }

    public static String toShortTime(Date paramDate) {
        return format("HH:mm", paramDate);
    }

    public static String toTime() {
        return format("HH:mm:ss", new Date());
    }

    public static String toTime(long paramLong) {
        return format("HH:mm:ss", new Date(paramLong));
    }

    public static String toTime(Date paramDate) {
        return format("HH:mm:ss", paramDate);
    }

    public static String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }
}