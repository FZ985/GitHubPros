package com.kmt.pro.bean.redpacket;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-28 11:05
 **/
public class RedDetailListBean implements Serializable {
    /**
     * amount : 20
     * stateTime : null
     * userId : var0hswts0mjs
     * userName : jack
     * userAvatar : /images/VCNRurUCEn.jpg
     */

    public String amount;
    public String stateTime;
    public String userId;
    public String userName;
    public String userAvatar;
    public String state;//红包状态
    public String isBest;// 0 否 1是  是否最佳
    public String envelopId;//红包ID
}
