package com.kmt.pro.bean.mine;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-06-05 15:44
 **/
public class LockUpBean implements Serializable {

    private String prediction;
    private String lockupMoney;

    public String getPrediction() {
        return prediction+"";
    }

    public String getLockupMoney() {
        return lockupMoney+"";
    }
}
