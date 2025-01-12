package com.car.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {


    public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat Y_M_D = new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat HH_MM_SS = new SimpleDateFormat("HH:mm:ss");



    public static Date strToDate(String dateStr, DateFormat fmt) {
        try {
            if (StringUtils.isNotEmpty(dateStr)) {
                Date date = null;
                date = fmt.parse(dateStr);
                return date;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 字符串转日期最后时刻<br/>
     * 2019-04-13-> 2019-04-13 23:59:59
     *
     * @param dateStr
     * @param fmt
     * @return
     * @throws Exception
     */
    public static Date strToDateLast(String dateStr, DateFormat fmt) throws Exception {
        if (StringUtils.isNotEmpty(dateStr)) {
            Date date = null;
            date = fmt.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return cal.getTime();
        } else {
            return null;
        }
    }

    /**
     * 日期转化字符串
     *
     * @param date 日期
     * @param fmt  格式化
     * @return String（fmt）
     */
    public static String dateToStr(Date date, DateFormat fmt) {
        return fmt.format(date);
    }

    public static String dateToStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return fmt.format(date);
    }

    public synchronized static boolean isRightDateStr(String dateStr, SimpleDateFormat dateFormat) {
        try {
            //采用严格的解析方式，防止类似 “2017-05-35” 类型的字符串通过
            dateFormat.setLenient(false);
            dateFormat.parse(dateStr);
            Date date = dateFormat.parse(dateStr);
            //重复比对一下，防止类似 “2017-5-15” 类型的字符串通过
            String newDateStr = dateFormat.format(date);
            return dateStr.equals(newDateStr);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 计算两个日期相差得秒数
     *
     * @param beforeDate 之前日期
     * @param afterDate  之后日期
     * @return
     */
    public static int calLastedTime(Date beforeDate, Date afterDate) {
        long beforeLong = beforeDate.getTime();
        long afterLong = afterDate.getTime();
        int seconds = (int) ((afterLong - beforeLong) / 1000);
        return seconds;
    }

    /**
     * 计算两个日期相差的分钟数
     *
     * @param beforeDate 之前日期
     * @param afterDate  之后日期
     * @return
     */
    public static int calLastedMinuteTime(Date beforeDate, Date afterDate) {
        long beforeLong = beforeDate.getTime();
        long afterLong = afterDate.getTime();
        int minute = (int) ((afterLong - beforeLong) / 1000 / 60);
        return minute;
    }


    /**
     * 设置给定时间的时分秒
     *
     * @param dateTime 时间
     * @param hour     时
     * @param minute   分
     * @param second   秒
     * @return
     */
    public static Date operationTime(LocalDateTime dateTime, Integer hour, Integer minute, Integer second) {
        Date date = operationTime(dateTime.getYear(), dateTime.getMonth().getValue(), dateTime.getDayOfMonth(), hour, minute, second);
        return date;
    }

    /**
     * 设置时间 年月日 时分秒
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param secount
     * @return
     */
    public static Date operationTime(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer secount) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, secount);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * 设置给定日期的天数
     *
     * @param dateTime 给定的时间
     * @param days     当day值为正数时，表示获取几天后的时间
     *                 当day值为负数时，表示获取几天前的时间
     * @return
     */
    public static Date operationDays(LocalDateTime dateTime, int days) {
        LocalDateTime time = dateTime.plusDays(days);
        Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * 设置给定日期的月份
     *
     * @param dateTime 给定的时间
     * @param month    当month值为正数时，表示获取几月后的时间
     *                 当month值为负数时，表示获取几月前的时间
     * @return
     */
    public static Date operationMonth(LocalDateTime dateTime, int month) {
        LocalDateTime time = dateTime.plusMonths(month);
        Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * 设置给定日期的哪一周的时间
     *
     * @param dateTime 给定的时间
     * @param week     当week值为正数时，表示获取几周后的时间
     *                 当week值为负数时，表示获取几周前的时间
     * @return
     */
    public static Date operationWeeks(LocalDateTime dateTime, int week) {
        LocalDateTime time = dateTime.plusWeeks(week);
        Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static Date operationYears(LocalDateTime dateTime, int year) {
        LocalDateTime time = dateTime.plusYears(year);
        Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }
}
