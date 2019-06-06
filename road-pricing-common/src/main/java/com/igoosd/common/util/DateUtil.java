package com.igoosd.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    static String FORMAT = "yyyy-MM-dd";

    //2016-04-21T16:29:40+0800
    static String SONAR_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    static String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

    public static Date parseDate(String day) {
        if (day == null || day.length() == 0) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            Date date = sdf.parse(day);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 转换指定时间为 年月日 00:00:00.000 整日时间值
     *
     * @return
     */
    public static Date convertYmdDate(Date date) {
        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.setTime(date);
        oldCalendar.set(Calendar.HOUR_OF_DAY, 0);
        oldCalendar.set(Calendar.MINUTE, 0);
        oldCalendar.set(Calendar.SECOND, 0);
        oldCalendar.set(Calendar.MILLISECOND, 0);
        return oldCalendar.getTime();
    }

    public static Date parseSonarDate(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat(SONAR_FORMAT);
        try {
            return sdf.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @param dateStr yyyy-MM-dd
     * @return
     */
    public static Date parseymdDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 Date 和time 时间 拼接 返回 datetime
     * 存在时区问题
     *
     * @param date yyyy-mm-dd
     * @param time hh:mm:ss.SSS
     * @return
     */
    public static Date splitDateAndTime(Date date, Date time) {
        Calendar calendar1 = Calendar.getInstance();//默认获取系统时区 GMT+8\
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar2.setTime(time);
        calendar1.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
        calendar1.set(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));
        calendar1.set(Calendar.SECOND, calendar2.get(Calendar.SECOND));
        calendar1.set(Calendar.MILLISECOND, calendar2.get(Calendar.MILLISECOND));
        return calendar1.getTime();
    }

    public static String formatDetailDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmssSSS);
        return sdf.format(date);
    }

    public static String formatStandardDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatYmdDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(date);
    }
    public static String formatDate(Date date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 获取日历 指定字段的 数字
     * 第二个字段请传递 Calendar  常量
     */
    public static int getDateNumber(Date date, int calField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(calField);
    }


    /**
     * 获取 date  下一日时间
     *
     * @param date
     * @return
     */
    public static Date getNextDate(Date date) {
        return getDate(date, 1);
    }

    public static Date getLastDate(Date date) {
        return getDate(date, -1);
    }

    public static Date getDate(Date curDate, int dayCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DAY_OF_YEAR, dayCount);
        return calendar.getTime();
    }

    /**
     * 获取当月第一天日期
     *
     * @return
     */
    public static Date getFistDayForCurMonth() {
        Date date = convertYmdDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getMonth(Date date, int monthCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthCount);
        return calendar.getTime();
    }

    public static Date getLastMonth(Date date) {
        return getMonth(date, -1);
    }

    public static Date getNextMonth(Date date) {
        return getMonth(date, 1);
    }


}
