package com.kmt.pro.bean.detail;

import android.text.TextUtils;

import com.kmt.pro.utils.Check;
import com.kmtlibs.app.utils.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-02 15:57
 **/
public class DetailBean implements Serializable {
    /**
     * addPrice : -50
     * averagePrice : 45
     * changPoint : -50
     * higestPrice : 50
     * lowestPrice : 50
     * openPrice : 100
     * price : 50
     */

    public BaseInformationBean baseInformation;
    /**
     * investorsAvatar : 2fcSMajjnH6.png
     * investorsBirthday : 1991-10-10
     * investorsCityOfBirth : 北京
     * investorsCode : 16543415
     * investorsCreateDate : 2016-09-26 16:50:47.0
     * investorsDescription : 没啥描述
     * investorsEthnicity : 汉族
     * investorsFixPrice : 0
     * investorsId : 4dbd35c8955d4bed94072d4ca954cfaf
     * investorsMainAchievements : 没啥成就
     * investorsName : 高萌
     * investorsNationality : 中国
     * investorsSchool :
     * investorsTotalNum : 100
     * investorsWork : 教师
     * openPrice : 100
     */

    public InvestorsBean investors;
    /**
     * num : 50
     * price : 50
     * time : 06.26
     */

    public List<BuyAndSellerListBean> buyAndSellerList;
    /**
     * num : 40
     * price : 42
     */

    public List<BuyFiverListBean> buyFiverList;
    /**
     * price : 45
     * time : 12.00
     */

    public List<MinitePriceListBean> minitePriceList;

    public List<SellFilverListBean> sellFilverList;

    public List<TInvestHisListBean> tInvestHisList;


    public static class BaseInformationBean implements Serializable {
        public String addPrice;
        public String averagePrice;// 均价
        public String changPoint;
        public String higestPrice;
        public String lowestPrice;
        public String openPrice;
        public String price;//最新价
        public String changeMoney;
        public String closePrice;//昨收价格
        public float uplowFlag;//是否第一天上市,第一天为100,之后为10
        public String stats;//是否申购中的状态
        public String circulatingTime;//流通时间
        public String investorsPrice;//总价
        public String turnoverNum;//0.00秒
        public String increasePoint; // 累计总涨幅
        public String investorsFixPrice;// 发行价
        public String votePercent = "0%"; //投票占用比
        public String voteDays = "0"; //投票数
        public String uplowPrice = "";
    }

    public static class InvestorsBean implements Serializable {
        public String investorsAvatar;
        public String investorsBirthday;
        public String investorsCityOfBirth;
        public String investorsCode;
        public String investorsCreateDate;
        public String investorsDescription;
        public String investorsEthnicity;
        public String investorsFixPrice;//发行价
        public String investorsId;
        public String investorsMainAchievements;
        public String investorsName;
        public String investorsNationality;
        public String investorsSchool;
        public String investorsTotalNum;
        public String investorsWork;
        public String openPrice;
        public String investorsPcImg;
        public String investorsTimeScope;
        public String investorsStatus;//0是没集合竞价之前(申购中)，1发行失败，2发行成功
        public String investorsPrice;//总身价
        public String investorsTop3;//排行头像
        public String investorsVideoUrl = "";//视频地址
        public String chatRoomId = "";//自定义数据,接收消息的聊天室ID
        public long preSellTime;// 倒计时
        public String purchaseFlag; //"1"表示是非预告期 “0”表示是预告期
        public String voteFlag; // "2"可以投票 其他不能
        public int investorsType; // 0是微信号否则是微信群
        // 房子的有关数据
        public String houseAddress; // 详细地址
        public double houseLat; // 纬度
        public double houseLon; // 经度
        public String pricePerMeter;// 房子单价多少钱/平米
        public String investorsPublishDate;//发行时间
        public String houseGoodness;// 优势
        public String investorsReleaseNum;//发行的天数
        public String houseVector;// 户型朝向
        public String houseFloor;// 所在楼层
        public String houseBuildingType;// 楼型
        public int hasElevator;// 是否有电梯：0，无，1，有
        public String houseDecoration;// 装修状况
        public String houseYear;// 房屋年代
        public String houseUse;// 房屋用途
        public String houseRightType;// 房屋权属
        public String houseDescription;// 房屋描述
    }

    public static class BuyAndSellerListBean implements Serializable {
        private String num;
        public String price;
        public String time;
        public String type;//S卖 B买

        public String getNum() {
            if (TextUtils.isEmpty(num)) return "";
            return Check.changeNum(num);
        }
    }

    public static class BuyFiverListBean implements Serializable {
        private String num;
        public String price;
        public String numStr;

        public String getNumStr() {
            return numStr;
        }

        public void setNumStr(String numStr) {
            this.numStr = numStr;
        }

        public String getNum() {
            String oldNum = (num + "");
            if (oldNum.contains("- -")) {
                return num;
            }
            Logger.e("detail_buy_num:" + num);
            String newNum = Check.changeNum(num);
            Logger.e("detail_buy_newNum:" + newNum);
            return newNum;
        }
    }

    public static class MinitePriceListBean implements Serializable {
        public String price;
        public String time;
        public String buyAndSell;//+为红色,-为绿色
        public float radio;
    }

    public static class SellFilverListBean implements Serializable {
        public String num;
        public String price;
        public String numStr;
    }

    /**
     * investorsAllMoney : 63999.02 成交额
     * investorsAllNum : 1180686.2 成交量
     * investorsClosePrice : 0.05 //昨天的收盘价
     * investorsCode : 0 //股票代码
     * investorsDate : 2016-11-18  //日期
     * investorsHighestPrice : 0.10 //最高
     * investorsLowestPrice : 0.02 //最低
     * investorsOpenPrice : 0.05 //今开
     * investorsTodayClosePrice : 0.10 //今收
     * uplowPrice : 100.00% //涨跌率
     */
    public static class TInvestHisListBean implements Serializable {
        public String investorsAllMoney;
        public String investorsAllNum;
        public String investorsClosePrice;
        public int investorsCode;
        public String investorsDate;
        public String investorsHighestPrice;
        public String investorsLowestPrice;
        public String investorsOpenPrice;
        public String investorsTodayClosePrice;
        public String uplowPrice;
        public String uplow;
    }

}
