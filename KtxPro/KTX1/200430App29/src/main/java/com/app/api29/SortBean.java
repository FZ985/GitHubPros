package com.app.api29;

import com.app.api29.chinese2pinyin.SortObject;

import java.io.Serializable;

public class SortBean extends SortObject implements Serializable {

    public SortBean(String sortName) {
        super(sortName);
    }
}