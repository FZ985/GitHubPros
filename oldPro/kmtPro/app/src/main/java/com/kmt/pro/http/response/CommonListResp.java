package com.kmt.pro.http.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-02-24 18:22
 **/
public class CommonListResp<T> implements Serializable {
    public String MSG;
    private List<T> LIST;
    private String STATUS;
    private String code;
    private String retMsg;
    private List<T> data;

    public String getCode() {
        return code + "";
    }

    public String getRetMsg() {
        return retMsg + "";
    }

    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public List<T> getLIST() {
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
        return "CommonListResp{" +
                "MSG='" + MSG + '\'' +
                ", LIST=" + LIST +
                ", STATUS='" + STATUS + '\'' +
                ", code='" + code + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
