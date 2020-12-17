package com.kmt.pro.bean;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-02-25 12:09
 **/
public class KeFuBean implements Serializable {

    public int type;
    public String name;

    public KeFuBean(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
