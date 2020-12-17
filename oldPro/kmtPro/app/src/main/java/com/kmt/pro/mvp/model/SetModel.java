package com.kmt.pro.mvp.model;

import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SettingContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-16 13:38
 **/
public class SetModel implements SettingContract.Model {
    @Override
    public Observable<CommonResp> userLogout() {
        return ApiManager.get().kmtApi().userLogout();
    }
}
