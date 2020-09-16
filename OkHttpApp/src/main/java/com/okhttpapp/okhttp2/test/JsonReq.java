package com.okhttpapp.okhttp2.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-09-16 11:34
 **/
public class JsonReq {
    public String os = "android";
    public String version = "3.0.1";
    public String userCode;
    public String channel = "guanfang";
    public JsonPars pars;

    public JsonReq(String userCode) {
        this.userCode = userCode;
        List<String> list = new ArrayList<>();
        list.add("%fa09-16-11-1234567890");
        pars = new JsonPars();
        pars.list = list;
    }

    public class JsonPars {
        public List<String> list;
    }
}
