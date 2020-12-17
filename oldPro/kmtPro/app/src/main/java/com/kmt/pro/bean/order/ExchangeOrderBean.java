package com.kmt.pro.bean.order;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-29 10:00
 **/
public class ExchangeOrderBean implements Serializable {
    public String bookingId;//预约主键id
    public String bookingInvestorsId;//房子编码
    public String bookingOrderNo;//预约订单号
    public String bookingStatus;//预约状态 1.下单待支付 2.支付成功待入住 3.交易完成待评价4.评价完成(交易关闭) 31.用户取消预约 33.系统定时任务取消预约
    public String bookingTimesSecondsLong;//预约天数
    public String checkInStartDate; //入住开始时间
    public String checkInEndDate; //入住开始时间
    public String houseName; //入住开始时间
    public String houseAvatar; //入住开始时间
    public boolean flag;//24小时内true >=24小时 false
    public boolean completionFlag; //显示是否出现确认完成
    public String orderType;
    public String createUserName;
}
