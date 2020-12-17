package com.kmt.pro.mvp.model;

import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.ForgetPayPwdContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-20 12:00
 **/
public class ForgetPayPwdModel extends RegisterModel implements ForgetPayPwdContract.Model {
    @Override
    public Observable<CommonResp> checkSmsCode(String code) {
        return ApiManager.get().kmtApi().checkSmsCode(code);
    }
}
