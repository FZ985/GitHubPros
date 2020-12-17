package com.kmt.pro.helper;

import com.kmt.pro.callback.impl.LetterInterface;

import java.util.Comparator;

/**
 * Create by JFZ
 * date: 2020-07-22 17:03
 **/
public class PinyinComparatorHelp<T extends LetterInterface> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        if (o1.getLetter().equals("@")
                || o2.getLetter().equals("#")) {
            return -1;
        } else if (o1.getLetter().equals("#")
                || o2.getLetter().equals("@")) {
            return 1;
        } else {
            return o1.getLetter().compareTo(o2.getLetter());
        }
    }
}
