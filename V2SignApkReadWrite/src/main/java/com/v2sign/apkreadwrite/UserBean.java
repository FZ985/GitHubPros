package com.v2sign.apkreadwrite;

import java.io.Serializable;

/**
 * Description:
 * Author: jfz
 * Date: 2021-01-19 9:50
 */
public class UserBean implements Serializable {

    private String name;
    private String uid;
    private String invite;
    private String md5;

    public UserBean(String name, String uid, String invite, String md5) {
        this.name = name;
        this.uid = uid;
        this.invite = invite;
        this.md5 = md5;
    }
}