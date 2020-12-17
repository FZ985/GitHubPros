package com.kmt.pro.mvp.model;

import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.AccountIncomeDetailContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-27 18:05
 **/
public class AccountIncomeDetailModel implements AccountIncomeDetailContract.Model {
    @Override
    public Observable<CommonResp> cancelWithdraw(String id) {
        return ApiManager.get().kmtApi().cancelWithdraw(id);
    }
}
