package com.kmt.pro.mvp.model;

import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SetPayPwdContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-20 14:47
 **/
public class SetPayPwdModel implements SetPayPwdContract.Model {
    @Override
    public Observable<CommonResp> setPayPwd(String old, String type) {
        return ApiManager.get().kmtApi().setPayPwd(old, type);
    }

    @Override
    public Observable<CommonResp> foundPayPwd(String old, String code, String type) {
        return ApiManager.get().kmtApi().foundPayPwd(old, code, type);
    }
}
