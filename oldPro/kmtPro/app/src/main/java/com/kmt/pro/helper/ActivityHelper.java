package com.kmt.pro.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.kmt.pro.bean.AccountIncomeBean;
import com.kmt.pro.ui.activity.AccountIncomeActivity;
import com.kmt.pro.ui.activity.AccountIncomeDetailActivity;
import com.kmt.pro.ui.activity.BigPicturePreviewActivity;
import com.kmt.pro.ui.activity.DeviceManagerActivity;
import com.kmt.pro.ui.activity.KeFuActivity;
import com.kmt.pro.ui.activity.PayPwdForgetActivity;
import com.kmt.pro.ui.activity.PaySetActivity;
import com.kmt.pro.ui.activity.SetLoginPwdActivity;
import com.kmt.pro.ui.activity.SetPayPasswordActivity;
import com.kmt.pro.ui.activity.SettingActivity;
import com.kmt.pro.ui.activity.UpdatePayPwdActivity;
import com.kmt.pro.ui.activity.coin.CoinListActivity;
import com.kmt.pro.ui.activity.coin.CoinRecordActivity;
import com.kmt.pro.ui.activity.coin.CoinRecordDetailActivity;
import com.kmt.pro.ui.activity.coin.MentionMoneyActivity;
import com.kmt.pro.ui.activity.coin.RechargeCoinActivity;
import com.kmt.pro.ui.activity.detail.DetailsActivity;
import com.kmt.pro.ui.activity.login.CountryCodeActivity;
import com.kmt.pro.ui.activity.login.LoginActivity;
import com.kmt.pro.ui.activity.login.RegisterActivity;
import com.kmt.pro.ui.activity.order.ConfimOrderActivity;
import com.kmt.pro.ui.activity.order.ExchangeOrderActivity;
import com.kmt.pro.ui.activity.order.OrderDetailActivity;
import com.kmt.pro.ui.activity.order.OrderEvaluateActivity;
import com.kmt.pro.ui.activity.order.OrderEvaluateFinishActivity;
import com.kmt.pro.ui.activity.redpacket.RedPacketDetailActivity;
import com.kmt.pro.ui.activity.redpacket.SelfRedRecordActivity;
import com.lxbuytimes.kmtapp.MainActivity;

/**
 * Create by JFZ
 * date: 2020-06-30 18:22
 **/
public class ActivityHelper {
    //登录成功返回到主界面
    public static void toLogin(Context context) {
        toLogin(context, KConstant.BackPage.MainActivity);
    }

    //登录界面
    public static void toLogin(Context context, String page) {
        toActivity(context, new Intent(context, LoginActivity.class)
                .putExtra(KConstant.Key.fromPagerKey, page));
    }

    //去注册
    public static void register(Context context, String page, boolean isForget) {
        toActivity(context, new Intent(context, RegisterActivity.class)
                .putExtra(KConstant.Key.fromPagerKey, page)
                .putExtra(KConstant.Key.isForgetKey, isForget));
    }

    //去设置
    public static void toSet(Context context) {
        toActivity(context, SettingActivity.class);
    }

    //设置登录密码
    public static void toSetLoginPwd(Context context) {
        toActivity(context, SetLoginPwdActivity.class);
    }

    //支付设置
    public static void toPaySet(Context context) {
        toActivity(context, PaySetActivity.class);
    }

    //修改密码
    public static void toUpdatePayPwd(Context context) {
        toActivity(context, UpdatePayPwdActivity.class);
    }

    //找回支付密码
    public static void toPayPwdForget(Context context) {
        toActivity(context, PayPwdForgetActivity.class);
    }

    //国家区号选择
    public static void toCountryCodeActivity(Context context) {
        toActivity(context, CountryCodeActivity.class);
    }

    //重置支付密码
    public static void toSetPayPwdActivity(Context context, String smsCode, String fromPager) {
        toActivity(context, new Intent(context, SetPayPasswordActivity.class)
                .putExtra(KConstant.Key.fromPagerKey, fromPager)
                .putExtra(KConstant.Key.key_smsCode, smsCode));
    }

    //设备管理
    public static void toDeviceManager(Context context) {
        toActivity(context, DeviceManagerActivity.class);
    }

    //充币列表
    public static void toRechargeCoin(Context context) {
        toActivity(context, new Intent(context, CoinListActivity.class).putExtra(KConstant.Key.key_coinlist_type, 1));
    }

    //提币列表
    public static void toTiCoin(Context context) {
        toActivity(context, new Intent(context, CoinListActivity.class).putExtra(KConstant.Key.key_coinlist_type, 2));
    }

    //详情
    public static void toDetailsActivity(Context context) {
        toActivity(context, DetailsActivity.class);
    }

    //充币记录
    public static void toRechargeRecord(Context context, String symbol_id) {
        toActivity(context, new Intent(context, CoinRecordActivity.class)
                .putExtra(KConstant.Key.key_symbol_id, symbol_id)
                .putExtra(KConstant.Key.key_symbol_recordType, 1));
    }

    //提币记录
    public static void toMentionRecord(Context context, String symbol_id) {
        toActivity(context, new Intent(context, CoinRecordActivity.class)
                .putExtra(KConstant.Key.key_symbol_id, symbol_id)
                .putExtra(KConstant.Key.key_symbol_recordType, 2));
    }

    //充币
    public static void toRecharge(Context context, String symbol_id, String tokenSymbol) {
        toActivity(context, new Intent(context, RechargeCoinActivity.class)
                .putExtra(KConstant.Key.key_symbol_id, symbol_id)
                .putExtra(KConstant.Key.key_tokenSymbol, tokenSymbol));
    }

    //提币
    public static void toMention(Context context, String symbol_id, String tokenSymbol) {
        toActivity(context, new Intent(context, MentionMoneyActivity.class)
                .putExtra(KConstant.Key.key_symbol_id, symbol_id)
                .putExtra(KConstant.Key.key_tokenSymbol, tokenSymbol));
    }

    //充币、提币记录详情
    public static void toRocordDetail(Context context, int type, String blockHash) {
        toActivity(context, new Intent(context, CoinRecordDetailActivity.class)
                .putExtra(KConstant.Key.key_gbt_type, type)
                .putExtra(KConstant.Key.key_blockHash, blockHash));
    }

    //账户明细
    public static void toAccountIncome(Context context) {
        toActivity(context, AccountIncomeActivity.class);
    }

    //账户明细详情
    public static void toAccountIncomeDetail(Context context, AccountIncomeBean data) {
        if (data == null) return;
        Intent intent = new Intent(context, AccountIncomeDetailActivity.class);
        intent.putExtra("Type", data.getType());
        intent.putExtra("bankType", data.getBankCardType());
        intent.putExtra("bankId", data.getBankCard());
        intent.putExtra("owner", data.getBankOwner());
        intent.putExtra("date", data.getRecordTradeDate());
        intent.putExtra("money", data.getMarketValue());
        intent.putExtra("recordStatus", data.getRecordStatus());
        intent.putExtra("freeRate", data.getFeeRate());
        intent.putExtra("name", data.getName());
        intent.putExtra("recordId", data.getRecordId());
        intent.putExtra("picture", data.getPicture());
        intent.putExtra("recordType", data.getRecordType());
        intent.putExtra("title", null != data.getTitle() ? data.getTitle() : "人家送钱,我送时间");
        toActivity(context, intent);
    }

    //提交订单页面
    public static void toConfimOrderActivity(Activity activity, String orderId, int from) {
        activity.startActivity(new Intent(activity, ConfimOrderActivity.class)
                .putExtra(KConstant.Key.order_id, orderId)
                .putExtra(KConstant.Key.order_from_switch, from));
    }

    //大图预览界面
    public static void toBigPricturePreviewActivity(Context context, String path) {
        toActivity(context,new Intent(context, BigPicturePreviewActivity.class)
        .putExtra(KConstant.Key.picturePath,path));
    }

    //评价
    public static void toOrderEvaluateActivity(Context context, String mBookId, String investorsCode, String houseName) {
        toActivity(context, new Intent(context, OrderEvaluateActivity.class)
                .putExtra(KConstant.Key.order_id, mBookId)
                .putExtra(KConstant.Key.investorsCode, investorsCode)
                .putExtra(KConstant.Key.houseName, houseName));
    }

    //评价完成
    public static void toOrderEvaluateFinishActivity(Context context, String mBookId, String investorsCode, String houseName) {
        toActivity(context, new Intent(context, OrderEvaluateFinishActivity.class)
                .putExtra(KConstant.Key.order_id, mBookId)
                .putExtra(KConstant.Key.investorsCode, investorsCode)
                .putExtra(KConstant.Key.houseName, houseName));
    }

    //红包详情
    public static void toRedPacketDetailActivity(Context context, String red_id) {
        toActivity(context, new Intent(context, RedPacketDetailActivity.class).putExtra(KConstant.Key.key_red_id, red_id));
    }

    //查看我的红包记录
    public static void toSelfRedRecordActivity(Context context) {
        toActivity(context, new Intent(context, SelfRedRecordActivity.class));
    }

    //兑换订单
    public static void toExchangeOrderActivity(Context context) {
        toActivity(context, new Intent(context, ExchangeOrderActivity.class));
    }

    //订单详情
    public static void toOrderDetailActivity(Context context, String orderId, boolean from) {
        toActivity(context, new Intent(context, OrderDetailActivity.class)
                .putExtra(KConstant.Key.order_id, orderId)
                .putExtra(KConstant.Key.fromOrderList, from));
    }

    //客服
    public static void toKeFu(Context context) {
        toActivity(context, KeFuActivity.class);
    }

    /*** ==========================================================================================================***/
    public static void toActivity(Context context, Class<?> cls) {
        toActivity(context, new Intent(context, cls));
    }

    public static void toActivity(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean toPageActivity(Context context, String fromPager) {
        return toPageActivity(context, fromPager, new Intent());
    }

    public static boolean toPageActivity(Context context, String fromPager, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!TextUtils.isEmpty(fromPager)) {
            if (!fromPager.equals(KConstant.BackPage.normal)) {
                Class c = null;
                try {
                    c = Class.forName(fromPager);
                    intent.setClass(context, c);
                    context.startActivity(intent);
                    return true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            intent.setClass(context, MainActivity.class);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

}
