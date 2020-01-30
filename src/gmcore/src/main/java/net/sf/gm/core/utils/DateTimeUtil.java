/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//


/**
 * The Class DateTimeUtil.
 */
public class DateTimeUtil {

    /**
     * The short date format.
     */
    static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * The long date format.
     */
    static final SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * The longlong date format.
     */
    static final SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * The short time format.
     */
    static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

    /**
     * The long time format.
     */
    static final SimpleDateFormat formatMilliseconds = new SimpleDateFormat("HH:mm:ss.SSS");

    /**
     * Java date to iso date.
     *
     * @param date the date
     * @return the string
     */
    public static String javaDateToIsoDateTime(Date date) {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        if (cal.get(Calendar.HOUR_OF_DAY) != 0 || cal.get(Calendar.MINUTE) != 0 || cal.get(Calendar.SECOND) != 0
            || cal.get(Calendar.MILLISECOND) != 0) {

            if (cal.get(Calendar.MILLISECOND) != 0)
                return formatTimestamp.format(date);
            return formatDateTime.format(date);
        }
        return formatDate.format(date);
    }

    /**
     * Java time to iso time.
     *
     * @param date the date
     * @return the string
     */
    public static String javaTimeToIsoTime(Time date) {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        if (cal.get(Calendar.MILLISECOND) != 0)
            return formatMilliseconds.format(date);
        return formatTime.format(date);
    }

    /**
     * Java date to iso timestamp.
     *
     * @param date the date
     * @return the string
     */
    public static String javaDateToIsoTimestamp(Date date) {

        return formatTimestamp.format(date);
    }

    /*
     *
     * "1964-05-30T14:25:36.478+01:00"
     *
     * allowed formats:
     *
     * date part: 1964 year 1964-05 year and month 1964-05-30 year and month and day
     * 1964-152 year and "day in year" 1964-W22 year and "week in year" 1964-W22-6
     * year and "week in year" and "day in week" 196405 year and month 19640530 year
     * and month and day 1964152 year and "day in year" 1964W22 year and
     * "week in year" 1964W226 year amd "week in year" and "day in week"
     *
     * delimiter ' ' or 'T' ( 'T' preferred )
     *
     * optional time part: 13 hour 13.5 hour and fraction of hour 13:45 hour and
     * minute 13:45.5 hour and minute and fraction of minute 13:45:52 hour and
     * minute and second 13:45:52.007 hour and minute and second and fraction of
     * second 1345 hour and minute 134552 hour and minute and second 134552.007 hour
     * and minute and second and fraction of second
     *
     * optional delimiter ' '
     *
     * optional time-zone part: Z UTC +01:00 Central European Time (CET) +0100
     * Central European Time (CET) -05:00 U.S./Canadian Eastern Standard Time (EST)
     * -0500 U.S./Canadian Eastern Standard Time (EST)
     */
    /**
     * The date pattern.
     */
    static final Pattern datePattern;

    /**
     * The DAT e_ GR p_ YEAR.
     */
    static final int DATE_GRP_YEAR;

    /**
     * The DAT e_ GR p_ MONTH.
     */
    static final int DATE_GRP_MONTH;

    /**
     * The DAT e_ GR p_ MDAY.
     */
    static final int DATE_GRP_MDAY;

    /**
     * The DAT e_ GR p_ WEEK.
     */
    static final int DATE_GRP_WEEK;

    /**
     * The DAT e_ GR p_ WDAY.
     */
    static final int DATE_GRP_WDAY;

    /**
     * The DAT e_ GR p_ HOUR.
     */
    static final int DATE_GRP_HOUR;

    /**
     * The DAT e_ GR p_ MINUTE.
     */
    static final int DATE_GRP_MINUTE;

    /**
     * The DAT e_ GR p_ SECONDS.
     */
    static final int DATE_GRP_SECONDS;

    /**
     * The DAT e_ GR p_ FRAC.
     */
    static final int DATE_GRP_FRAC;

    /**
     * The DAT e_ GR p_ TIMEZONE.
     */
    static final int DATE_GRP_TIMEZONE;

    /**
     * The time pattern.
     */
    static final Pattern timePattern;

    /**
     * The TIM e_ GR p_ HOUR.
     */
    static final int TIME_GRP_HOUR;

    /**
     * The TIM e_ GR p_ MINUTE.
     */
    static final int TIME_GRP_MINUTE;

    /**
     * The TIM e_ GR p_ SECONDS.
     */
    static final int TIME_GRP_SECONDS;

    /**
     * The TIM e_ GR p_ FRAC.
     */
    static final int TIME_GRP_FRAC;

    /**
     * The TIM e_ GR p_ TIMEZONE.
     */
    static final int TIME_GRP_TIMEZONE;

    static {
        String dateRegex = "^\\s*"
            + "(" // date part: begin
            + "(\\d{4})" // date part: year
            + "(" // date part: month- or week- part: begin
            + "(-?(\\d{2})-?(\\d{2})?)" // date part: month- or week- part: month
            // and optional "day in month"
            + "|" // date part: month- or week- part: or
            + "(-?W(\\d{2})-?(\\d)?)" // date part: month- or week- part: week and
            // optional "day in week"
            + ")?" // date part: month- or week- part: end
            + ")" // date part: end
            + "(" // time part: begin
            + "(\\s*[\\sT.]\\s*)" // time part: delimiter
            + "(\\d{2})(:?(\\d{2})(:?(\\d{2}))?)?([.,](\\d+))?" // time part: info
            + ")?" // time part: end ( time part is optional )
            + "(" // time zone: begin
            + "\\s*" // time zone: delimiter (optional)
            + "(Z|[+-]\\d{2}:?(\\d{2})?)" // time zone: info
            + ")?" // time zone: end ( time zone is optional )
            + "\\s*$";
        datePattern = Pattern.compile(dateRegex);

        DATE_GRP_YEAR = 2;
        DATE_GRP_MONTH = 5;
        DATE_GRP_MDAY = 6;
        DATE_GRP_WEEK = 8;
        DATE_GRP_WDAY = 9;
        DATE_GRP_HOUR = 12;
        DATE_GRP_MINUTE = 14;
        DATE_GRP_SECONDS = 16;
        DATE_GRP_FRAC = 18;
        DATE_GRP_TIMEZONE = 20;

        String timeRegex = "^\\s*"
            + "(" // time part: begin
            + "(\\s*[\\sT.]\\s*)?" // time part: ? delimiter (optional)
            + "(\\d{2})(:?(\\d{2})(:?(\\d{2}))?)?([.,](\\d+))?" // time part: info
            + ")" // time part: end
            + "(" // time zone: begin
            + "\\s*" // time zone: delimiter (optional)
            + "(Z|[+-]\\d{2}:?(\\d{2})?)" // time zone: info
            + ")?" // time zone: end ( time zone is optional )
            + "\\s*$";
        timePattern = Pattern.compile(timeRegex);

        TIME_GRP_HOUR = 3;
        TIME_GRP_MINUTE = 5;
        TIME_GRP_SECONDS = 7;
        TIME_GRP_FRAC = 9;
        TIME_GRP_TIMEZONE = 11;
    }

    /**
     * Iso date to java date.
     *
     * @param isoString the iso string
     * @return the date
     * @throws ParseException the parse exception
     */
    public static Date isoDateToJavaDate(final String isoString) throws ParseException {

        return isoDateToCalendar(isoString).getTime();
    }

    /**
     * Iso date to calendar.
     *
     * @param isoString the iso string
     * @return the calendar
     * @throws ParseException the parse exception
     */
    public static Calendar isoDateToCalendar(final String isoString) throws ParseException {

        if (isoString == null || isoString.length() == 0)
            throw new ParseException("", 0);

        Matcher matcher = datePattern.matcher(isoString);
        if (!matcher.find())
            throw new ParseException(isoString, 0);

        Integer year = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_YEAR);
        Integer month = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_MONTH);
        Integer mday = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_MDAY);
        Integer week = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_WEEK);
        Integer wday = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_WDAY);
        Integer hour = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_HOUR);
        Integer min = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_MINUTE);
        Integer sec = getIntFromMatchedGroup(isoString, matcher, DATE_GRP_SECONDS);
        double frac = getFractionFromMatchedGroup(isoString, matcher, DATE_GRP_FRAC);
        String strTz = getStringFromMatchedGroup(isoString, matcher, DATE_GRP_TIMEZONE);

        if (sec == null) {
            frac *= 60.0;
            if (min == null)
                frac *= 60.0;
        }
        if (hour == null)
            hour = 0;
        if (min == null)
            min = 0;
        if (sec == null)
            sec = 0;

        int milli = (int) (frac * 1000.0);

        TimeZone tz;
        if (strTz == null)
            tz = TimeZone.getDefault();
        else {
            if (strTz.equals("Z"))
                tz = TimeZone.getTimeZone("UTC");
            else
                tz = TimeZone.getTimeZone("GMT" + strTz);
        }
        Calendar calendar = new GregorianCalendar(tz);
        calendar.clear();
        if (year != null)
            calendar.set(Calendar.YEAR, year);
        if (month != null)
            calendar.set(Calendar.MONTH, month - 1);
        if (mday != null)
            calendar.set(Calendar.DAY_OF_MONTH, mday);
        if (week != null)
            calendar.set(Calendar.WEEK_OF_YEAR, week);

        // sunday=1, monday=2,...saturday=7
        if (wday != null)
            calendar.set(Calendar.DAY_OF_WEEK, wday + 1);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MILLISECOND, milli);
        return calendar;
    }

    /**
     * Iso time to java time.
     *
     * @param isoString the iso string
     * @return the time
     * @throws ParseException the parse exception
     */
    public static Time isoTimeToJavaTime(final String isoString) throws ParseException {

        return new Time(isoTimeToCalendar(isoString).getTimeInMillis());
    }

    /**
     * Iso time to calendar.
     *
     * @param isoString the iso string
     * @return the calendar
     * @throws ParseException the parse exception
     */
    public static Calendar isoTimeToCalendar(final String isoString) throws ParseException {

        if (isoString == null || isoString.length() == 0)
            throw new ParseException("", 0);

        Matcher matcher = timePattern.matcher(isoString);
        if (!matcher.find())
            throw new ParseException(isoString, 0);

        Integer hour = getIntFromMatchedGroup(isoString, matcher, TIME_GRP_HOUR);
        Integer min = getIntFromMatchedGroup(isoString, matcher, TIME_GRP_MINUTE);
        Integer sec = getIntFromMatchedGroup(isoString, matcher, TIME_GRP_SECONDS);
        double frac = getFractionFromMatchedGroup(isoString, matcher, TIME_GRP_FRAC);
        String strTz = getStringFromMatchedGroup(isoString, matcher, TIME_GRP_TIMEZONE);

        if (sec == null) {
            frac *= 60.0;
            if (min == null)
                frac *= 60.0;
        }

        if (hour == null)
            hour = 0;
        if (min == null)
            min = 0;
        if (sec == null)
            sec = 0;

        int milli = (int) (frac * 1000.0);

        TimeZone tz;
        if (strTz == null)
            tz = TimeZone.getDefault();
        else {
            if (strTz.equals("Z"))
                tz = TimeZone.getTimeZone("UTC");
            else
                tz = TimeZone.getTimeZone("GMT" + strTz);
        }
        Calendar calendar = new GregorianCalendar(tz);
        calendar.clear();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MILLISECOND, milli);
        return calendar;
    }

    /**
     * Gets the int from matched group.
     *
     * @param groupId the group id
     * @param string  the string
     * @param matcher the matcher
     * @return the int from matched group
     * @throws ParseException the parse exception
     */
    private static Integer getIntFromMatchedGroup(String string, Matcher matcher, int groupId) throws ParseException {

        if (matcher.start(groupId) < 0)
            return null;
        try {
            return Integer.parseInt(string.substring(matcher.start(groupId), matcher.end(groupId)));
        } catch (NumberFormatException e) {
            throw new ParseException(string, matcher.start(groupId));
        }
    }

    /**
     * Gets the fraction from matched group.
     *
     * @param groupId the group id
     * @param string  the string
     * @param matcher the matcher
     * @return the fraction from matched group
     * @throws ParseException the parse exception
     */
    private static double getFractionFromMatchedGroup(String string, Matcher matcher, int groupId) throws ParseException {

        if (matcher.start(groupId) < 0)
            return 0.0;
        try {
            double frac = Integer.parseInt(string.substring(matcher.start(groupId), matcher.end(groupId)));
            while (frac >= 1.0)
                frac /= 10.0;
            return frac;
        } catch (NumberFormatException e) {
            throw new ParseException(string, matcher.start(groupId));
        }
    }

    /**
     * Gets the string from matched group.
     *
     * @param groupId the group id
     * @param string  the string
     * @param matcher the matcher
     * @return the string from matched group
     */
    private static String getStringFromMatchedGroup(String string, Matcher matcher, int groupId) {

        if (matcher.start(groupId) < 0)
            return null;
        return string.substring(matcher.start(groupId), matcher.end(groupId));
    }
}
