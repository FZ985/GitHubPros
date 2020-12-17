package com.kmt.pro.helper;

import renrenkan.RenRenKanKey;

/**
 * Create by JFZ
 * date: 2020-06-29 17:24
 **/
public interface KConstant {

    //内部配置请求,一键切换线上线下
    boolean TEST_DEBUG = false;
    //线上数字货币请求地址
    String symbol_domain = "https://newsymbol.buybuybuybuybuy.com";
    String data_domain = "https://newkmtapi.buybuybuybuybuy.com";

    //线上请求地址设置
//    String wallet_domain = TEST_DEBUG ? HttpConfig.get().symbolDomain() : symbol_domain;
    String wallet_domain = symbol_domain;
    //    String release_domain = TEST_DEBUG ? HttpConfig.get().dataDomain() : data_domain;
    String release_domain = data_domain;

//     String wallet_domain = "http://192.168.0.113:8080";
//     String release_domain = "http://kmt.lnamphp.com";//测试
//     String release_domain = "http://10.2.9.134";//测试

    //接口请求
    String base_url = release_domain + "/api/";

    String https_url = base_url;
    String uoload_img = base_url;

    String http_html = release_domain + "/static/"; // 静态的页面
    String img_url = release_domain + "/image/"; // 图片的访问地址
    String RED_SHARE = "http://kmt1.vwfrkst.cn/static/farmfang_packet/html/packet.html?packetId="; // 红包的地址

    //微信APPID
    String APP_ID = "wx890fbd39f2fdeef4";
    String APP_SECRET = "1bb6ea334e3fd3373355f037c5669867";

    String Service_Key = RenRenKanKey.get().getInterfaceKeyToxiaofei();
    String Service_KeyIv = RenRenKanKey.get().getInterfaceKeyIvToxiaofei();


    interface Html {
        String invite_rule = http_html + "regulations1.html";//邀请规则地址
        String guadan_rule = http_html + "farmfang_rule/launchOrderRule.html";//挂单规则地址
        String suocang = http_html + "lockup/suocang.html";//锁仓地址
    }


    //生成sp的文件名称
    interface SP {
        //详情数据传递 随退出删除
        String spdata_kmtDetail = "spdata_kmtDetail";
        //保存用户相关  随退出删除
        String spdata_kmtUser = "spdata_kmtUser";
        //kmt全局配置
        String spdata_kmtAllConfig = "spdata_kmtAllConfig";
    }

    //传值的key
    interface Key {
        String fromPagerKey = "fromPager";//路径
        String isForgetKey = "isForget";//是否忘记密码
        String web_url = "web_url";
        String web_title = "web_title";
        String web_shareTitle = "web_shareTtile";
        String web_shareText = "web_shareText";
        String web_shareLogo = "web_shareLogo";
        String web_isAppendParams = "web_isAppendParams";
        String web_isShowShareButton = "web_isShowShareButton";
        String key_smsCode = "sms_code";
        String key_coinlist_type = "coinlistType";//数字货币列表类型  1： 充币 2：提币 3： 兑换
        String key_tokenSymbol = "tokenSymbol";
        String key_symbol_id = "symbol_id";
        String key_symbol_recordType = "symbol_recordType";//历史记录类型  1：充值记录 2： 提现记录
        String key_gbt_type = "symbol_gbt_type";//1 充值记录；2 提币记录
        String key_blockHash = "blockHash";
        String key_red_id = "red_id";
        String index = "index";
        String order_id = "order_id";
        String order_from_switch = "order_from_switch";
        String investorsCode = "investorsCode";
        String houseName = "houseName";
        String fromOrderList = "fromOrderList";
        String picturePath = "picture_path";
    }

    //activity的路径
    interface BackPage {
        //默认没有
        String normal = "normal";
        //首页
        String MainActivity = com.lxbuytimes.kmtapp.MainActivity.class.getName();
        //等待认购页面
        String WaitActivity = com.kmt.pro.ui.activity.detail.WaitActivity.class.getName();
    }

    interface UrlContants {
        String scheme = "kmt://";
        String bottomSwitch = scheme + "bottomswitch_";//底部导航切换
        String bottomSwitchNoFinish = scheme + "bottomswitchNofinish_";//底部导航切换
        String accountDetails = scheme + "accountDetails";//账户明细
        String changeOrder = scheme + "changeOrder";//兑换订单
        String inviteFriends = scheme + "inviteFriends";//邀请好友
        String buyingPrivileges = scheme + "buyingPrivileges";//购买特权
        String customerService = scheme + "customerService";//联系客服
        String bindParent = scheme + "bindParent";//绑定师傅
        String switchTab = scheme + "switchTab_";//底部导航切换
        String taobaoMall = scheme + "taobaoMall_";//淘宝商城
        String taobaoMallNoDetail = scheme + "taobaoMallNoDetail_";//淘宝商城,底部导航进去的

    }
}
