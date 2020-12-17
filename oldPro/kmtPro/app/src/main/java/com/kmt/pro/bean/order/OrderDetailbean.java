package com.kmt.pro.bean.order;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-08-05 15:50
 **/
public class OrderDetailbean implements Serializable {

    public String bookingAuditingTime;//预约下单时间
    public String bookingId;//预约主键id
    public String bookingInvestorsId;//房子编码
    public String bookingOrderNo;//预约订单号
    public String bookingStatus;//预约状态 1.下单待支付 2.支付成功待入住 3.交易完成待评价4.评价完成(交易关闭) 31.用户取消预约 33.系统定时任务取消预约
    public String bookingTimeValue;//入住时段的客房总市值
    public String bookingTimesSecondsLong;//预约天数
    public String bookingUserId;//预约用户id
    public String userName;//联系人名称
    public String userTel;//联系人手机号
    public String hotelGuestsNames; //入住人姓名 多个名称用,号拼接
    public String checkInStartDate; //入住开始时间
    public String checkInEndDate; //入住结束时间
    public String houseAddress; //详细地址
    public String houseName; //房源名称
    public String houseAvatar; //房源头像
    public boolean flag = false; //24小时内true >=24小时 false
    public boolean completionFlag = false; //true 显示确认完成按钮 false 不显示
    public String extraChange;//卫生费用
    public String orderType;
    public String createUserName;
    public String wxImg; // 微信二维码
    public String wxNum; // 微信群名称
}
