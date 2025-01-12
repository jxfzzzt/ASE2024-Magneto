package com.car.account.web.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final ThreadLocal<SimpleDateFormat> YYYY_MM_dd_HH_mm_ss = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    public static final ThreadLocal<SimpleDateFormat> YYYY_MM_dd = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    public static final ThreadLocal<SimpleDateFormat> YYYYMMdd = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };
    public static final ThreadLocal<SimpleDateFormat> YYYYMMdd_HH_mm_ss = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> YYYYMMddHHmmss = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };
    public static final ThreadLocal<SimpleDateFormat> MMddHHmmss = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MMddHHmmss");
        }
    };

    private static final ThreadLocal<SimpleDateFormat> YYYY_MM_dd_HH_mm_ss_SSS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> SSMMHH = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("ss mm HH");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> HHMMSS = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };


    public static Date strToDate(String dateStr, DateFormat fmt) {
        try {
            if(StringUtils.isNotEmpty(dateStr)){
                Date date = null;
                date = fmt.parse(dateStr);
                return date;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();;
            return null;
        }

    }

    /**
     * 字符串转日期最后时刻<br/>
     * 2019-04-13-> 2019-04-13 23:59:59
     * @param dateStr
     * @param fmt
     * @return
     * @throws Exception
     */
    public static Date strToDateLast(String dateStr, DateFormat fmt) throws Exception {
        if(StringUtils.isNotEmpty(dateStr)){
            Date date = null;
            date = fmt.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return cal.getTime();
        }else{
            return null;
        }
    }

    /**
     * 日期转化字符串
     * @param date  日期
     * @param fmt   格式化
     * @return  String（fmt）
     */
    public static String dateToStr(Date date, DateFormat fmt) {
        if (date == null) {
            return null;
        }
        return fmt.format(date);
    }

    public static boolean isRightDateStr(String dateStr,SimpleDateFormat dateFormat){
        try {
            //采用严格的解析方式，防止类似 “2017-05-35” 类型的字符串通过
            dateFormat.setLenient(false);
            dateFormat.parse(dateStr);
            Date date = dateFormat.parse(dateStr);
            //重复比对一下，防止类似 “2017-5-15” 类型的字符串通过
            String newDateStr = dateFormat.format(date);
            if(dateStr.equals(newDateStr)){
                return true;
            }else {
                //log.info("字符串dateStr:{}， 不是严格的 datePattern:{} 格式的字符串",dateStr, dateFormat.toPattern());
                return false;
            }
        } catch (ParseException e) {
            //log.info("字符串dateStr:{}，不能按照 datePattern:{} 样式转换",dateStr, dateFormat.toPattern());
            return false;
        }
    }

    /**
     * 计算两个日期相差得秒数
     * @param beforeDate    之前日期
     * @param afterDate     之后日期
     * @return
     */
    public static int calLastedTime(Date beforeDate,Date afterDate) {
        long beforeLong = beforeDate.getTime();
        long afterLong = afterDate.getTime();
        int seconds = (int)((afterLong - beforeLong) / 1000);
        return seconds;
    }

    /**
     * 计算两个日期相差的分钟数
     * @param beforeDate    之前日期
     * @param afterDate     之后日期
     * @return
     */
    public static int calLastedMinuteTime(Date beforeDate,Date afterDate) {
        long beforeLong = beforeDate.getTime();
        long afterLong = afterDate.getTime();
        int minute = (int)((afterLong - beforeLong) / 1000 /60);
        return minute;
    }

    /****
     * 传入具体日期 ，返回具体日期减少一天
     * @param date 日期(2017-04-13)
     * @return 2017-04-12
     * @throws ParseException
     */
    public static Date subDay(String date,int cal){
        Date dt1 = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(date);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DAY_OF_MONTH, cal);
            dt1 = rightNow.getTime();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return dt1;
    }

    /**
     * 计算两个日期相差的天数
     * @param beforeDate    之前日期
     * @param afterDate     之后日期
     * @return
     */
    public static int calLastedDayTime(Date beforeDate,Date afterDate) {
        long beforeLong = beforeDate.getTime();
        long afterLong = afterDate.getTime();
        int day = (int)((afterLong - beforeLong) / 1000 /60 /60 /24);
        return day;
    }

    public static void main(String[] args) {
        Date beginTime = DateUtil.strToDate("2020-7-9",DateUtil.YYYY_MM_dd.get());
        Date currDate = new Date();
        if(DateUtil.calLastedDayTime(beginTime,currDate) >= 0 ){
            System.out.println("111");
        }else{
            System.out.println("222");
        }

    }
}
