package com.kmt.pro.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.kmt.pro.base.BaseApp;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.app.base.BaseSharedPreferences;

/**
 * Create by JFZ
 * date: 2020-06-29 17:21
 **/
public class KmtConfigSp extends BaseSharedPreferences {
    private static KmtConfigSp sp = new KmtConfigSp();

    public static synchronized KmtConfigSp get() {
        return sp;
    }

    @Override
    public SharedPreferences getSharedPreference() {
        return BaseApp.getInstance().getSharedPreferences(KConstant.SP.spdata_kmtAllConfig, Context.MODE_PRIVATE);
    }
}
