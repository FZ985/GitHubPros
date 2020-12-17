package com.kmt.pro.bean;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-20 17:32
 **/
public class AppVersionBean implements Serializable {
    public String version; // 最新版本
    public String redirectlocation; // 更新地址ַ
    public String stratery; // 更新策略 0：不需要更新 1：不强制更新 2：强制更新
    public String ishidden; //1隐藏，2显示,与android 无关
    public String upgradeDetails;//升级详情
    public String payChannelState;// 隐藏支付字段 , 0 all 1 ali 2 union

    public String getVersion() {
        return version;
    }

    public String getRedirectlocation() {
        return redirectlocation;
    }

    public String getStratery() {
        return stratery;
    }

    public String getIshidden() {
        return ishidden;
    }

    public String getUpgradeDetails() {
        return upgradeDetails;
    }

    public String getPayChannelState() {
        return payChannelState;
    }
}
