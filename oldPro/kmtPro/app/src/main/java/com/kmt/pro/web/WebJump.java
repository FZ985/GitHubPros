package com.kmt.pro.web;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.kmt.pro.R;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.ui.activity.WebViewActivity;

/**
 * Create by JFZ
 * date: 2020-07-07 11:57
 **/
public class WebJump {

    /**
     * 用户注册协议
     *
     * @param context
     */
    public static void toRegisterWeb(Context context) {
        toWeb(context,
                KConstant.http_html + "farmfang_rule/userprotocol.html?time=" + System.currentTimeMillis(),
                "用户协议", "用户协议", "用户协议", R.mipmap.share_userprotocol, false, true);
    }

    public static void toRule(Context context) {
        String share = context.getString(R.string.set_kmt_rule);
        toWeb(context,
                KConstant.http_html + "farmfang_rule/tradingRules.html?time=" + System.currentTimeMillis(),
                share, share, share, R.mipmap.share_trade, false, true);
    }

//    ==========================================================================================================================

    public static void toWebNoShare(Context context, String url) {
        toWebNoShare(context, url, false);
    }

    public static void toWebNoShare(Context context, String url, boolean isAppendParams) {
        toWebNoShare(context, url, null, isAppendParams);
    }

    public static void toWebNoShare(Context context, String url, String title, boolean isAppendParams) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KConstant.Key.web_url, url);
        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(KConstant.Key.web_title, title);
        }
        intent.putExtra(KConstant.Key.web_isAppendParams, isAppendParams);
        intent.putExtra(KConstant.Key.web_isShowShareButton, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void toWeb(Context context, String url, String title, String shareTitle, String shareText, boolean isAppendParams, boolean showShareButton) {
        toWeb(context, url, title, shareTitle, shareText, R.mipmap.applogo, isAppendParams, showShareButton);
    }

    public static void toWeb(Context context, String url, String title, String shareTitle, String shareText, int shareLogo, boolean isAppendParams, boolean showShareButton) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KConstant.Key.web_url, url);
        intent.putExtra(KConstant.Key.web_title, title);
        intent.putExtra(KConstant.Key.web_shareTitle, shareTitle);
        intent.putExtra(KConstant.Key.web_shareText, shareText);
        intent.putExtra(KConstant.Key.web_shareLogo, shareLogo);
        intent.putExtra(KConstant.Key.web_isAppendParams, isAppendParams);
        intent.putExtra(KConstant.Key.web_isShowShareButton, showShareButton);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
