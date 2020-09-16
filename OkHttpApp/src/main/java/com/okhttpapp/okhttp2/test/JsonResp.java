package com.okhttpapp.okhttp2.test;

import java.io.Serializable;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-09-16 11:40
 **/
public class JsonResp implements Serializable {
//    {"retCode":"ok","retMsg":"success","body":{"list":[],"is_send":1,"send_sum":0,"send_success":19}}

    public String retCode;
    public String retMsg;
    public JsonRespBody body;

    public class JsonRespBody implements Serializable {
        public List<String> list;
        public String is_send;
        public String send_sum;
        public String send_success;

        @Override
        public String toString() {
            return "JsonRespBody{" +
                    "list=" + list +
                    ", is_send='" + is_send + '\'' +
                    ", send_sum='" + send_sum + '\'' +
                    ", send_success='" + send_success + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonResp{" +
                "retCode='" + retCode + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", body=" + (body == null ? "null" : body.toString()) +
                '}';
    }
}
