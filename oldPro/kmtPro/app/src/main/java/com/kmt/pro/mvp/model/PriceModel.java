package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.HomeBannerBean;
import com.kmt.pro.bean.ProductBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.PriceContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-01 15:41
 **/
public class PriceModel implements PriceContract.Model {
    @Override
    public Observable<CommonResp<HomeBannerBean>> queryBannerList() {
        return ApiManager.get().kmtApi().queryBannerList();
    }

    @Override
    public Observable<CommonListResp<ProductBean>> queryStock() {
        return ApiManager.get().kmtApi().queryStock();
    }
}
