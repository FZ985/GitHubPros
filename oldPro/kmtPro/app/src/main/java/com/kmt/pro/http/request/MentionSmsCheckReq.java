package com.kmt.pro.http.request;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-24 18:22
 **/
public class MentionSmsCheckReq extends UserIdReq implements Serializable {

    private String phone;

    public MentionSmsCheckReq(String phone) {
        super();
        this.phone = phone;
    }
}
