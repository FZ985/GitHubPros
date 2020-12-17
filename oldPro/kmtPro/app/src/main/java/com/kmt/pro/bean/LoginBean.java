package com.kmt.pro.bean;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-06-30 16:11
 **/
public class LoginBean implements Serializable {
    public String userAvatar = "";
    public String userName;
    public String userTel;
    public String userId;
    public String userPayStats;//1是已经设置支付密码，2是没设置
    public String mobileCountryCode;//返回的国家代码
    public String brokerNo;// hcode
    public String higherLevel;// 师傅邀请码
    public String brokerRealName;//经纪人姓名号,默认为0
    public String issuerFlag; //0不是发行人，1是发行人
    public String userEasemobId;//环信ID
    public String userEasemobPassword;//环信密码
    public String userFeeRate;//交易手续费
    public String newUserFlag;//是否为新用户, 0否1是
    public String wxFeeLabelFlag;//是否需要提示手续费  0是1否
    public String miaochatAlertFlag = "2";//秒说支付秒数提醒，1是，2否
    public String userPasswordFlag; // 是否设置登陆密码
    public String sessionId;

    public void setUserPasswordFlag(String userPasswordFlag) {
        this.userPasswordFlag = userPasswordFlag;
    }

    public void setUserPayStats(String userPayStats) {
        this.userPayStats = userPayStats;
    }
}
