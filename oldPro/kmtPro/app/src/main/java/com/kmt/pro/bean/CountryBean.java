package com.kmt.pro.bean;

import android.text.TextUtils;

import com.kmt.pro.utils.chinese2pinyin.PinYin;
import com.kmtlibs.app.utils.Logger;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-06 16:50
 **/
public class CountryBean implements Serializable {
    private String name;//国家名称
    public String phoneAreaCode;//电话代码

    public String getName() {
        return name + "";
    }

    public int getId() {
        String idStr = PinYin.getPinYin(name);
        Logger.e("拼音:" + idStr + ",idStr.charAt(0):" + idStr.charAt(0));
        return idStr.charAt(0);
    }

    public String substring() {
        if (TextUtils.isEmpty(name)) return "#";
        if (name.length() > 0) {
            if (name.length() > 1)
                return name.substring(0, 1);
            else return name;
        } else return "#";
    }
}
