package com.kmt.pro.mvp.model;

import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.UpdatePayPasswordContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-17 18:29
 **/
public class UpdatePasswordModel implements UpdatePayPasswordContract.Model {

    @Override
    public Observable<CommonResp> verifyPayPwd(String old, String type) {
        return ApiManager.get().kmtApi().verifyPayPwd(old, type);
    }

    @Override
    public Observable<CommonResp> updatePayPwd(String old, String newpwd, String type) {
        return ApiManager.get().kmtApi().updatePayPwd(old, newpwd, type);
    }
}
