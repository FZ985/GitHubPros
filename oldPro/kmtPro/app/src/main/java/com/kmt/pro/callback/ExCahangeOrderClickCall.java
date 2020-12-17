package com.kmt.pro.callback;

/**
 * Create by JFZ
 * date: 2020-08-04 10:25
 **/
public interface ExCahangeOrderClickCall {
    //支付
    void status1(String orderId, int switchFrom);

    //确认完成
    void status2(String orderId);

    //评论
    void status3(String orderId, String investorsCode, String houseName);

    void showDialog(String message, String delType,String orderId);
}
