package com.enn.energy.price.common.utils;

import com.enn.energy.price.common.error.ErrorCodeEnum;
import com.enn.energy.price.common.error.PriceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类.
 *
 * @author : wuchaon
 * @version : 1.0 2021/11/29 16:34
 * @since : 1.0
 **/
public class PriceDateUtils {


    private static SimpleDateFormat dayDataFormat = new SimpleDateFormat("yyyy-MM-dd");

    static {
        dayDataFormat.setLenient(false);
    }

    public static Date addDateByday (Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addDateByHour (Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    /**
     * 获取相差天数的当天开始时间
     *
     * @param days
     * @return
     */
    public static Date getNextDayBeginTime(int days) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();

    }

    /**
     * 格式化到天的日期
     *
     * @param date
     * @return
     */
    public static String dayDateToStr(Date date) {
        if (date == null) {
            return null;
        }
        return dayDataFormat.format(date);
    }

    /**
     * 字符串转换日期
     *
     * @param date
     * @return
     */
    public static Date strToDayDate(String date) {
        if (date == null) {
            return null;
        }
        try {
           return dayDataFormat.parse(date);
        } catch (ParseException e) {
            throw new PriceException(ErrorCodeEnum.VALIDATION_CODE_EXCEPTION.getErrorCode(),"获取指定格式日期的数据格式异常");
        }
    }

    /**
     * 获取指定的日期
     * @param date
     * @return
     */
    public static Date getDesignatedDayDate(String date){

        try {
            return dayDataFormat.parse(date);
        } catch (ParseException e) {
            throw new PriceException(ErrorCodeEnum.VALIDATION_CODE_EXCEPTION.getErrorCode(),"获取指定格式日期的数据格式异常");
        }
    }

    static ThreadLocal<SimpleDateFormat> sf_dd = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(FORMAT_Y_M_D);
        }
    };
    public static String FORMAT_Y_M_D = "yyyy-MM-dd";

    public static boolean beforeOrEqual(Date sourceDate, Date anotherDate) {
        return sourceDate.compareTo(anotherDate) <= 0 ? true : false;
    }


    public static boolean beforeNoEqual(Date sourceDate, Date anotherDate) {
        return sourceDate.compareTo(anotherDate) < 0 ? true : false;
    }

    public static boolean afterOrEqual(Date sourceDate, Date anotherDate) {
        return sourceDate.compareTo(anotherDate) >= 0 ? true : false;
    }

    public static boolean equal(Date sourceDate, Date anotherDate) {
        return sourceDate.compareTo(anotherDate) == 0 ? true : false;
    }

    public static boolean afterNoEqual(Date sourceDate, Date anotherDate) {
        return sourceDate.compareTo(anotherDate) > 0 ? true : false;
    }

    public static Date getToday() throws ParseException {
        Calendar current = Calendar.getInstance();
        String today = sf_dd.get().format(current.getTime());
        Date todayDate = sf_dd.get().parse(today);
        return todayDate;
    }

    public static long getExpireTime(Long expireBase){
//        Math.random() * 3600 +
        return expireBase;
    }

    public static boolean beforeOrEqual(String source, String target) {
        int compare = source.compareTo(target);
        return compare <= 0 ? true : false;
    }

    public static boolean afterOrEqual(String source, String target) {
        int compare = source.compareTo(target);
        return compare >= 0 ? true : false;
    }

    public static String formatTimeStr(String time){
        try {
//            if (time.endsWith("2-29")){//如果为29号，则取上一天的时间
//                time = time.replace("2-29","2-28");
//            }
            sf_dd.get().setLenient(false);
            String format = sf_dd.get().format(sf_dd.get().parse(time));
            sf_dd.remove();
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static void main(String[] args) throws ParseException {
        System.out.println(getNextDayBeginTime(-1));
        String s1 = "2022-01-01";
        String s2 = "2022-12-01";
        String s4 = "2022-04-01";
        System.out.println(beforeOrEqual(s1, s2));
        System.out.println(afterOrEqual(s2, s4));
        System.out.println(s1.substring(5));
        String s3 = "2021-02-29";
        sf_dd.get().setLenient(false);
        System.out.println(sf_dd.get().parse(s3));
        System.out.println(sf_dd.get().format(sf_dd.get().parse(s3)));
    }
}
