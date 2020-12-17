package com.kmt.pro.bean.mine;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-09 12:16
 **/
public class MineBean implements Serializable {

    public String userName;
    public String userAvatar;
    public String positionPrice;//总市值
    public String investmentNumber;//投资人数
    public String totalWorth;//总盈亏
    public String totalWithdrawMoney;//总提现
    public String totalRechargeMoney;//总充值
    public String userPurse;//钱包余额
    public String netRecharge;//净充值
    public String userPayStats;//已经有支付密码1，无2
    public String withDrawMoney;//可用余额
    public String withdrawCommissionCharge;//提现手续费
    public String generalAssets;//资产总额
    public String todayEarnings;//今日盈亏
    public boolean showTodayEarnings;

    public String getUserName() {
        return userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getPositionPrice() {
        return positionPrice;
    }

    public String getInvestmentNumber() {
        return investmentNumber;
    }

    public String getTotalWorth() {
        return totalWorth;
    }

    public String getTotalWithdrawMoney() {
        return totalWithdrawMoney;
    }

    public String getTotalRechargeMoney() {
        return totalRechargeMoney;
    }

    public String getUserPurse() {
        return userPurse;
    }

    public String getNetRecharge() {
        return netRecharge;
    }

    public String getUserPayStats() {
        return userPayStats;
    }

    public String getWithDrawMoney() {
        return withDrawMoney;
    }

    public String getWithdrawCommissionCharge() {
        return withdrawCommissionCharge;
    }

    public String getGeneralAssets() {
        return generalAssets;
    }

    public String getTodayEarnings() {
        return todayEarnings;
    }

    public boolean isShowTodayEarnings() {
        return showTodayEarnings;
    }
}
