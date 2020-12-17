package com.kmt.pro.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.kmt.pro.base.BaseApp;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.app.base.BaseSharedPreferences;


/**
 * Create by JFZ
 * date: 2020-05-21 19:57
 **/
public class DetailSp extends BaseSharedPreferences {

    private static DetailSp detailSp;

    private String name = "name";
    private String code = "code";
    private String mPresell = "mPresell";
    private String investorsFixPrice = "investorsFixPrice";
    private String jpush = "jpush";

    private DetailSp() {
    }

    public static synchronized DetailSp get() {
        if (detailSp == null) {
            detailSp = new DetailSp();
        }
        return detailSp;
    }

    public String getName() {
        return getString(name, "");
    }

    public DetailSp setName(String val) {
        putString(name, val);
        return this;
    }

    public String getCode() {
        return getString(code, "");
    }

    public DetailSp setCode(String val) {
        putString(code, val);
        return this;
    }

    public int getmPresell() {
        return getInt(mPresell, 0);
    }

    public DetailSp setmPresell(int val) {
        putInt(mPresell, val);
        return this;
    }

    public String getInvestorsFixPrice() {
        return getString(investorsFixPrice, "");
    }

    public DetailSp setInvestorsFixPrice(String val) {
        putString(investorsFixPrice, val);
        return this;
    }

    public String getJpush() {
        return getString(jpush, "0");
    }

    public DetailSp setJpush(String val) {
        putString(jpush, val);
        return this;
    }

    @Override
    public SharedPreferences getSharedPreference() {
        return BaseApp.getInstance().getSharedPreferences(KConstant.SP.spdata_kmtDetail, Context.MODE_PRIVATE);
    }
}
