package com.kmt.pro.callback.impl;

import android.text.TextUtils;

/**
 * Create by JFZ
 * date: 2020-07-22 17:01
 **/
public abstract class LetterInterface {

    public abstract String getFullName();

    public String getLetter() {
        String pinyin = getFullName();
        if (TextUtils.isEmpty(pinyin)) return "#";
        String sortString = pinyin.length() >= 1 ? pinyin.substring(0, 1).toUpperCase() : pinyin;
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }

}
