package com.kmt.pro.http.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-02-24 18:22
 **/
public class CommonAllResp<O, L> implements Serializable {
    public String MSG;
    public O OBJECT;
    private List<L> LIST;
    private String STATUS;

    private String code;
    private String retMsg;
    private O data;
    private List<L> datas;

    public String getCode() {
        return code + "";
    }

    public String getRetMsg() {
        return retMsg + "";
    }

    public O getData() {
        return data;
    }

    public List<L> getDatas() {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        return datas;
    }

    public List<L> getLIST() {
        if (LIST == null) {
            LIST = new ArrayList<>();
        }
        return LIST;
    }

    public String getSTATUS() {
        return STATUS + "";
    }

    @Override
    public String toString() {
        return "CommonAllResp{" +
                "MSG='" + MSG + '\'' +
                ", OBJECT=" + OBJECT +
                ", LIST=" + LIST +
                ", STATUS='" + STATUS + '\'' +
                ", code='" + code + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", data=" + data +
                ", datas=" + datas +
                '}';
    }
}
