package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.SplashBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SplashContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-06-30 14:35
 **/
public class SplashModel implements SplashContract.Model {
    @Override
    public Observable<CommonResp<SplashBean>> getSplash() {
        return ApiManager.get().kmtApi().getSplash();
    }
}
