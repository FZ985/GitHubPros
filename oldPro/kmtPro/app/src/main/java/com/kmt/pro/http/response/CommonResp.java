package com.kmt.pro.http.response;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-02-24 18:22
 **/
public class CommonResp<T> implements Serializable {
    public String MSG;
    public T OBJECT;
    private String STATUS;
    private String code;
    private String retMsg;
    public T data;



    public String getCode() {
        return code + "";
    }

    public String getRetMsg() {
        return retMsg + "";
    }

    public String getSTATUS() {
        return STATUS + "";
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    @Override
    public String toString() {
        return "CommonResp{" +
                "MSG='" + MSG + '\'' +
                ", OBJECT=" + OBJECT +
                ", STATUS='" + STATUS + '\'' +
                ", code='" + code + '\'' +
                ", retMsg='" + retMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
