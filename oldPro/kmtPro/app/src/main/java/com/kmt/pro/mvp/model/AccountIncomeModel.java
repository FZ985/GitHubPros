package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.AccountIncomeBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.mvp.contract.AccountIncomeContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-24 11:27
 **/
public class AccountIncomeModel implements AccountIncomeContract.Model {
    @Override
    public Observable<CommonListResp<AccountIncomeBean>> newOrderRecord(String currentPager, String pageSize, String type) {
        return ApiManager.get().kmtApi().newOrderRecord(currentPager, pageSize, type);
    }
}
