package com.app.api29.utils;

import android.widget.Toast;

import com.app.api29.SortBean;
import com.app.api29.chinese2pinyin.PinyinComparator;
import com.app.api29.chinese2pinyin.SortObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Sort {

    public static int AZ_09_OTHER = 1;
    public static int NUM_AZ_OTHER = 2;

    public static List<SortBean> getSort(List<SortBean> datas, int type) {
        Collections.sort(datas, new PinyinComparator());
        List<SortBean> data = new ArrayList<>();
        List<SortBean> nums = new ArrayList<>();
        List<SortBean> az = new ArrayList<>();
        List<SortBean> other = new ArrayList<>();
        System.out.println("sort:" + datas.size());
        Pattern p = Pattern.compile("[a-zA-Z]");

        for (SortBean so : datas) {
            System.out.println("letter:" + so.getLetter() + ",isAZ:" + p.matcher(so.getLetter()).matches());
            if (so.getLetter().equals("#") || so.getLetter().equals("@")) {
                other.add(so);
            }
//            if (so.getLetter().matches("[A-Za-z]]")) {
            if (p.matcher(so.getLetter()).matches()) {
                az.add(so);
            }
//            }
            if (so.getLetter().matches("[0-9]")) {
                nums.add(so);
            }
        }
//        data.clear();
        if (type == AZ_09_OTHER) {
            data.addAll(az);
            data.addAll(nums);
            data.addAll(other);
        } else {
            data.addAll(nums);
            data.addAll(az);
            data.addAll(other);
        }
        System.out.println("numsSize:" + nums.size());
        System.out.println("azSize:" + az.size());
        System.out.println("otherSize:" + other.size());
        System.out.println("data:" + data.size());
        return data;
    }
}