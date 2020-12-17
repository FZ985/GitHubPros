package com.kmt.pro.utils;

import android.graphics.Color;
import android.text.TextUtils;

import com.kmt.pro.R;
import com.kmtlibs.app.utils.Logger;

/**
 * Create by JFZ
 * date: 2020-07-02 10:06
 **/
public class Check {

    /**
     * 隐藏手机号中间位数
     * 由于不知道国外手机号的位数，所以去手机号中间、中间前一位、中间后一位三位数来隐藏
     * 如果手机号太短显示全部
     * @param mobile
     * @return
     */
    public static String getGoneCenterMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            if (mobile.length() == 11) {
                return mobile.substring(0, 3) + "****" + mobile.substring(7);
            } else {
                int center = mobile.length() / 2;
                int beforeIndex = center - 2;
                int afterIndex = center + 1;
                if (beforeIndex > 0 && afterIndex < mobile.length()) {
                    try {
                        return mobile.substring(0, beforeIndex) + "***" + mobile.substring(afterIndex);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return mobile;
    }

    public static String getStringNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0";
        }
        return str;
    }

    public static boolean isWhiteColor(int color) {
        return (color == R.color.white) || (color == R.color.transparent) || (color == Color.parseColor("#FFFFFF") || (color == Color.WHITE) || (color == Color.parseColor("#ffffff")));
    }

    //科学计数法转换
    public static String changeNum(String num) {
        try {
//            BigDecimal bigDecimal = new BigDecimal(num);
//            Logger.e("getNum_num:" + bigDecimal.intValue() + ",max:" + Integer.MAX_VALUE);
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            String n = nf.format(Double.valueOf(num));
            return n;
        } catch (Exception e) {
            Logger.e("getNum_err:" + e.getMessage());
            return num;
        }
    }

}
