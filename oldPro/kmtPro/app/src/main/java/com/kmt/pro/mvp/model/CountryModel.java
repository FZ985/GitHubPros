package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.CountryBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.mvp.contract.CountryCodeContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-06 16:52
 **/
public class CountryModel implements CountryCodeContract.Model {
    @Override
    public Observable<CommonListResp<CountryBean>> getConturyList() {
        return ApiManager.get().kmtApi().getConturyList();
    }
}
