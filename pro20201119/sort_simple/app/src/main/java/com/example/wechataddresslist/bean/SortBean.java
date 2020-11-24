package com.example.wechataddresslist.bean;

import com.example.wechataddresslist.utils.Utils;
import com.lib.chinese2pinyin.SortObject;

import java.io.Serializable;

public class SortBean extends SortObject implements Serializable {
    public int type;

    public SortBean(String sortName,String fullName, int type) {
        super(sortName,fullName);
        this.type = type;
    }

    public SortBean(String sortName,String fullName) {
        super(sortName,fullName);
        this.type = Utils.DATA;
    }
}