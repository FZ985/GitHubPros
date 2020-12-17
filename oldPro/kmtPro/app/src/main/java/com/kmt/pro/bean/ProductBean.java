package com.kmt.pro.bean;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-01 15:43
 **/
public class ProductBean implements Serializable {
    public String addPrice;
    public String houseDescription;
    public String buyerOrderPrice;
    public String investorsAvatar;
    public String investorsClosePrice;
    public String investorsCode;
    public int investorsFixFlag;
    public String investorsFixPrice;
    public String investorsName;
    public String investorsPresellDate;
    public String investorsPublishDate;
    public int investorsStatus;
    public String newOrderPrice;
    public String uplowPrice;
    public int uplowStatus;
    public int presellFlag; // 1是预购 2是申购
    public String createUserName;
    public String suffix;
    public long preSellCountDownMillSecond;
}
