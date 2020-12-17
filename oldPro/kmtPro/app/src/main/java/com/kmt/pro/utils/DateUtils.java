package com.kmt.pro.utils;


import android.text.TextUtils;

import com.kmtlibs.app.utils.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String PULLREFRESH_DATE_FORMAT = "MM-dd HH:mm";

    /**
     * 根据指定的格式格式化日期对象
     *
     * @param date
     *            日期对象
     * @param foramt
     *            要格式的格式
     * @return
     */
    public static String dateFormat(Date date, String foramt) {
        String result = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(foramt);
            result = df.format(date);
        }
        return result;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String getTime(long time) {
        return format(time, DEFAULT_DATE_FORMAT);
    }

    public static String getTime2(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH");
            return sdf.format(new Date(time));
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateFormat(String time) {
        String format = "";
        try {
            if (!TextUtils.isEmpty(time)) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf1.parse(time);
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");
                format = sdf.format(d);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String getPullTime(long time) {
        return format(time, PULLREFRESH_DATE_FORMAT);
    }

    /**
     * 格式化时间,自定义标签
     *
     * @param time    时间
     * @param pattern 格式化时间用的标签 yyyy-MM-dd hh:MM:ss  HH为24小时
     * @return
     */
    public static String format(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param strDateBegin
     *            开始时间 00:00:00
     * @param strDateEnd
     *            结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(String strDate, String strDateBegin,
                                   String strDateEnd) {
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(0, 2));
        int strDateM = Integer.parseInt(strDate.substring(3, 5));
        //int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // 截取开始时间时分秒
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        //int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
        // 截取结束时间时分秒
        int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        //int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
            // 当前时间小时数在开始时间和结束时间小时数之间
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
                // 当前时间小时数等于开始时间小时数，小于结束的小时数
            } else if(strDateH == strDateBeginH && strDateH < strDateEndH){
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM
                    && strDateM <= strDateEndM) {
                return true;
            }
            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
            else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM <= strDateEndM) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static int getMonth(String time){
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date=null;
        try {
            date=sim.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (c.get(Calendar.MONTH) + 1);
    }
    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String dayForWeek(String pTime) {
        String week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
            int dayForWeek = 0;
            if(c.get(Calendar.DAY_OF_WEEK) == 1){
                dayForWeek = 7;
            }else{
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            if(dayForWeek == 1){
                week = "周一";
            }else if (dayForWeek == 2){
                week = "周二";
            }else if(dayForWeek == 3){
                week = "周三";
            }else if(dayForWeek == 4){
                week = "周四";
            }else if(dayForWeek == 5){
                week = "周五";
            }else if(dayForWeek == 6) {
                week = "周六";
            }else if(dayForWeek == 7){
                week = "周日";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Logger.i("日期转换错误" + pTime);
        }

        return week;
    }

    /**
     *字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getCurDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前是几号
     *
     * @return
     */
    public static int getCurDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
}
