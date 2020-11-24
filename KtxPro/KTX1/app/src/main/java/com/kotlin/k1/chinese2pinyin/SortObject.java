package com.kotlin.k1.chinese2pinyin;

import java.io.Serializable;

public class SortObject implements ContactItemInterface, Serializable {
    private String sortName;
    private String letter;
    private String fullName;

    public SortObject(String sortName) {
        this.sortName = sortName;
        this.fullName = PinYin.getPinYin(sortName);
        System.out.println("fullname:" + getFullName().charAt(0));
        setLetter(this.fullName);
    }

    private void setLetter(String sortName) {
        String sortString = sortName.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
            this.letter = sortString.toUpperCase();
        } else {
            this.letter = "#";
        }
    }

    @Override
    public String getLetter() {
        return letter;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getDisplayInfo() {
        return sortName;
    }
}