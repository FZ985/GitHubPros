package com.kmt.pro.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.kmt.pro.base.BaseApp;
import com.kmt.pro.helper.KConstant.UrlContants;
import com.kmt.pro.ui.activity.BindFriendActivity;
import com.kmt.pro.web.WebJump;
import com.kmtlibs.app.ActivityManager;
import com.kmtlibs.app.receive.SendRecvHelper;
import com.kmtlibs.app.utils.Logger;
import com.lxbuytimes.kmtapp.MainActivity;


/**
 * Create by JFZ
 * date: 2020-03-30 15:11
 **/
public class UrlJumpHelper {
    public static void jump(Activity activity, String url) {
        jump(activity, "", url);
    }

    public static void jump(Activity activity, String loginBackPath, String url) {
        Logger.e("UrlJumpHelper_url:" + url);
        if (activity == null) return;
        if (TextUtils.isEmpty(url)) return;
        if (Login.get().isLogin()) {
            if (URLUtil.isNetworkUrl(url)) {
                //网页链接
                WebJump.toWebNoShare(activity, url);
            } else if (url.startsWith(UrlContants.scheme)) {
                if (url.contains(UrlContants.bottomSwitch)) {
                    String indexStr = url.replace(UrlContants.bottomSwitch, "").trim();
                    try {
                        int index = Integer.valueOf(indexStr);
                        SendRecvHelper.send(BaseApp.getInstance(), new Intent(Actions.ACT_BOTTOM_SWITCH)
                                .putExtra(KConstant.Key.index, index));
                        ActivityManager.getAppInstance().keepActivity(MainActivity.class);
                    } catch (NumberFormatException e) {
                    }
                }if (url.contains(UrlContants.bottomSwitchNoFinish)) {
                    String indexStr = url.replace(UrlContants.bottomSwitchNoFinish, "").trim();
                    try {
                        int index = Integer.valueOf(indexStr);
                        SendRecvHelper.send(BaseApp.getInstance(), new Intent(Actions.ACT_BOTTOM_SWITCH)
                                .putExtra(KConstant.Key.index, index));
                    } catch (NumberFormatException e) {
                    }
                }
                if (url.equals(UrlContants.accountDetails)) {
                    //账户明细
                    ActivityHelper.toAccountIncome(activity);
                } else if (url.equals(UrlContants.changeOrder)) {
                    //兑换订单
                    ActivityHelper.toExchangeOrderActivity(activity);
                } else if (url.equals(UrlContants.inviteFriends)) {
                    //邀请好友
//                    activity.startActivity(new Intent(activity, NewInviteFriendActivity.class));
                } else if (url.equals(UrlContants.buyingPrivileges)) {
                    //购买特权
//                    activity.startActivity(new Intent(activity, BuyPrivilegesActivity.class));
                } else if (url.equals(UrlContants.customerService)) {
                    //联系客服
                    ActivityHelper.toKeFu(activity);
                } else if (url.equals(UrlContants.bindParent)) {
                    //绑定师傅
                    activity.startActivity(new Intent(activity, BindFriendActivity.class));
                } else if (url.contains(UrlContants.switchTab)) {
                    //导航切换
//                    try {
//                        int index = Integer.parseInt(url.replace(UrlContants.switchTab, "").trim());
//                        SendRecvHelper.send(activity, new Intent(Actions.SWITCH_TAB).putExtra("index", index));
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                    }
                } else if (url.contains(UrlContants.taobaoMall)) {
                    noLoginJump(activity, url);
                } else if (url.contains(UrlContants.taobaoMallNoDetail)) {
                    //淘宝商城, 导航进去
//                    String mStocKCode = url.replace(UrlContants.taobaoMallNoDetail, "").trim();
//                    activity.startActivity(new Intent(activity, TaoBaoMallActivity.class)
//                            .putExtra("investorsCode", mStocKCode)
//                            .putExtra("isDetail", false));
                }
            }
        } else {
            if (!TextUtils.isEmpty(loginBackPath)) {
                ActivityHelper.toLogin(activity, loginBackPath);
                return;
            }
            noLoginJump(activity, url);
        }
    }

    private static void noLoginJump(Activity activity, String url) {
//        if (URLUtil.isNetworkUrl(url)) {
//            //网页链接
//            BasicHelper.jumpWeb(activity, url, "");
//        } else if (url.contains(UrlContants.taobaoMall)) {
//            //淘宝商城
//            String mStocKCode = url.replace(UrlContants.taobaoMall, "").trim();
//            activity.startActivity(new Intent(activity, TaoBaoMallActivity.class)
//                    .putExtra("investorsCode", mStocKCode)
//                    .putExtra("isDetail", true));
//        }
    }


    //个人中心条目跳转
    public static void mineJump(String url) {
        jump(ActivityManager.getAppInstance().currentActivity(), MainActivity.class.getName(), url);
    }

    public static void fromPageJump(Activity activity, String fromPager) {
        final Intent intent = new Intent();
        if (!TextUtils.isEmpty(fromPager)) {
            Class c = null;
            try {
                c = Class.forName(fromPager);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            intent.setClass(activity, c);
        } else {
            intent.setClass(activity, MainActivity.class);
        }
        activity.startActivity(intent);
    }
}
