package com.lib.chinese2pinyin;

import java.util.Comparator;

/**
 * Create by JFZ
 * date: 2020-07-06 18:12
 **/
public class PinyinComparator implements Comparator<ContactItemInterface> {

    @Override
    public int compare(ContactItemInterface o1, ContactItemInterface o2) {
//        if (o1.getLetter().matches("[0-9]") || o2.getLetter().matches("[0-9]")) {
//            return o1.getLetter().compareTo(o2.getLetter());
//        } else
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
