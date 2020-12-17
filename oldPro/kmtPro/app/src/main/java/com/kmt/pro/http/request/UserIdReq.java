package com.kmt.pro.http.request;


import com.kmt.pro.sp.UserSp;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-15 11:29
 **/
public class UserIdReq implements Serializable {

    private String user_id;

    public UserIdReq() {
        this.user_id = UserSp.get().getUserId();
    }
}
