package org.md2k.mcerebrumapi.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class DateTime {
    private static final String NOW = "NOW";
    private static final String TODAY = "TODAY";
    private static final String SUNDAY = "SUNDAY";
    private static final String MONDAY = "MONDAY";
    private static final String TUESDAY = "TUESDAY";
    private static final String WEDNESDAY = "WEDNESDAY";
    private static final String THURSDAY = "THURSDAY";
    private static final String FRIDAY = "FRIDAY";
    private static final String SATURDAY = "SATURDAY";

    /**
     * Number of milliseconds in a second
     */
    public final static long SECOND_IN_MILLIS = 1000L;

    /**
     * Number of milliseconds in a minute
     */
    public final static long MINUTE_IN_MILLIS = 60L * 1000L;

    /**
     * Number of milliseconds in an hour
     */
    public final static long HOUR_IN_MILLIS = 60L * 60 * 1000L;

    /**
     * Number of milliseconds in a day
     */
    public final static long DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    public final static String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String FORMAT_MONTH_DAY_YEAR = "MM-dd-yyyy";
    public final static String FORMAT_HOUR_MINUTE_SECOND_MILLISECOND = "hh:mm:ss.SSS";
    public final static String FORMAT_HOUR_MINUTE_SECOND = "hh:mm:ss";
    public final static String FORMAT_HOUR_MINUTE = "hh:mm";

    // https://www.iso.org/iso-8601-date-and-time-format.html

    public final static String FORMAT_DATE_DEFAULT = FORMAT_YEAR_MONTH_DAY;
    public final static String FORMAT_TIME_DEFAULT = FORMAT_HOUR_MINUTE_SECOND_MILLISECOND;
    public final static String FORMAT_DATETIME_DEFAULT = FORMAT_DATE_DEFAULT + " " + FORMAT_TIME_DEFAULT;

    private DateTime() {
    }

    /**
     * @return The current time in milliseconds.
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static long getToday() {
        return getTodayAt("00:00:00");
    }

    public static long getTodayAt(String time) {
        String[] s = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(s[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(s[2]));
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getYesterday() {
        return getToday() - DAY_IN_MILLIS;
    }

    public static long getYesterdayAt(String time) {
        return getTodayAt(time) - DAY_IN_MILLIS;
    }

    public static long getTomorrow() {
        return getToday() + DAY_IN_MILLIS;
    }

    public static long getTomorrowAt(String time) {
        return getTodayAt(time) + DAY_IN_MILLIS;
    }

    public static String toString(long timestamp) {
        return toString(timestamp, FORMAT_DATETIME_DEFAULT);

    }

    public static String toString(long timestamp, String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            Date currentTimeZone = calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * Extracts the day of the week from the given timestamp.
     *
     * @return The day of the week.
     */
    public static int getDayOfWeek() {
        return getDayOfWeek(getCurrentTime());
    }

    public static int getDayOfWeek(long timestamp) {
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(timestamp);
        return calender.get(Calendar.DAY_OF_WEEK);
    }

    public static long getTime(String value, String format) {
        try {
            DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            Date date = formatter.parse(value);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long getTime(String timeStr) {
        String value = timeStr.trim().toUpperCase();
        if (NOW.equals(value))
            return getCurrentTime();
        else if (TODAY.equals(value))
            return getToday();
        else if (value.contains(":") && value.contains("/"))
            return getTime(value, FORMAT_YEAR_MONTH_DAY + " " + FORMAT_HOUR_MINUTE_SECOND);
        else if (value.contains(":"))
            return getTime(value, FORMAT_HOUR_MINUTE_SECOND);
        else if (value.contains("/"))
            return getTime(value, FORMAT_YEAR_MONTH_DAY);
        else if (value.equals(SUNDAY))
            return Calendar.SUNDAY;
        else if (value.equals(MONDAY))
            return Calendar.MONDAY;
        else if (value.equals(TUESDAY))
            return Calendar.TUESDAY;
        else if (value.equals(WEDNESDAY))
            return Calendar.WEDNESDAY;
        else if (value.equals(THURSDAY))
            return Calendar.THURSDAY;
        else if (value.equals(FRIDAY))
            return Calendar.FRIDAY;
        else if (value.equals(SATURDAY))
            return Calendar.SATURDAY;
        return -1;
    }

}
