package com.kmt.pro.http;

import com.kmt.pro.helper.KConstant;

/**
 * Create by JFZ
 * date: 2020-07-09 14:17
 **/
public interface HttpUrl {
    String kmt_config = "https://weizhuan-app-h5010.oss-cn-shenzhen.aliyuncs.com/kmt/android/newkmt.json";
    //获取数字货币
    String walletget = KConstant.wallet_domain + "/symbol/wallet/find/get";

    //数字货币列表（充提兑）
    String theCoinList = KConstant.wallet_domain + "/symbol/list/get";

    //充值页面
    String recharge = KConstant.wallet_domain + "/symbol/charge/page/get";

    //充值记录
    String recharge_history = KConstant.wallet_domain + "/symbol/charge/history/get";

    //提币历史记录
    String mention_record = KConstant.wallet_domain + "/symbol/withdrawal/history/get";

    //提币详情
    String mention_detail = KConstant.wallet_domain + "/symbol/withdrawal/info/get";

    //充值详情
    String recharge_record_detail = KConstant.wallet_domain + "/symbol/charge/info/get";

    //提币界面
    String mention = KConstant.wallet_domain + "/symbol/withdrawal/page/get";

    //提币验证码
    String smsCode = KConstant.wallet_domain + "/symbol/wallet/send/set";

    //提币提交
    String mention_commit = KConstant.wallet_domain + "/symbol/withdrawal/page/set";

}
