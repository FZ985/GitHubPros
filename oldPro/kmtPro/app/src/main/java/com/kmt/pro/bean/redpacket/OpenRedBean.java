package com.kmt.pro.bean.redpacket;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-28 11:01
 **/
public class OpenRedBean implements Serializable {
    public HeadBean head;
    public String code;//0可以抢 1 已经抢啦  2 迟啦 3 超时
    public String amount;

    public static class HeadBean implements Serializable{
        public String amount;
        public String childrens;
        public String type;
        public String userId;//发红包UersID
        public String userName;//用户昵称
        public String userAvatar;//发红包UersID
        public String title;//留言
        public String childrensUsed;//义强多少个
        public String amountUsed;//已抢多少秒
        public String investorsCode;//发行人code
        public String totalPrice;//价值
        public String investorsName;//发行人名字
    }
}
