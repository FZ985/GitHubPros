package com.example.wechataddresslist.utils;

import com.example.wechataddresslist.bean.SortBean;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.java.cncity.CnCityDict;


import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static final int HEAD = 0x11;
    public static final int DATA = 0x12;

    public static String[] items = {"aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh",
            "2222",
            "jjjj",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "@#$%^",
            "#@$%^",
            "$#@%^",
            "99999",
            "99999",
            "oooo", "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh",
            "99999",
            "99999",
            "jjjj",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "oooo", "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh",
            "jjjj",
            "11111",
            "99999",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "oooo",
            "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "dgad",
            "gggg",
            "hhhh", "421",
            "123",
            "jjjj",
            "77",
            "adggg",
            "hhhjk",
            "2323",
            "yyy",
            "uuuu",
            "iii",
            "oooo",
            "aaaa",
            "bbbbb",
            "ccc",
            "zzzz",
            "sssda",
            "asf",
            "1111",
            "dgad",
            "gggg",
            "hhhh",
            "jjjj",
            "adggg",
            "hhhjk",
            "yyy",
            "uuuu",
            "iii",
            "洛阳",
            "安徽",
            "太原",
            "临沂",
            "威海",
            "浙江",
            "杭州",
            "青岛",
            "北京",
            "成都",
            "温州",
            "---dd",
            "重庆", "长沙",
            "<<<<dd",
            "石家庄",
            "oooo"};


    public static int PY_For_lib = 1;
    public static int PY_For_tinypinyin = 2;
    public static int PY_For_pinyin4j = 3;


    public static List<SortBean> datas(int py) {
        List<SortBean> datas = new ArrayList<>();
        if (py == Utils.PY_For_tinypinyin){
            Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance()));
        }
        for (String s : items) {
            if (py == Utils.PY_For_lib) {
                datas.add(new SortBean(s, com.lib.chinese2pinyin.PinYin.getPinYin(s), DATA));
            }
            if (py == Utils.PY_For_tinypinyin) {
                datas.add(new SortBean(s, Pinyin.toPinyin(s,","), DATA));
            }
            if (py == Utils.PY_For_pinyin4j) {
                datas.add(new SortBean(s, Pinyin4jUtil.converterToSpell(s), DATA));
            }
        }
        return datas;
    }
}