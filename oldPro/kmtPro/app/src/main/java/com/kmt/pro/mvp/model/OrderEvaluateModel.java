package com.kmt.pro.mvp.model;

import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.OrderEvaluateContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-08-05 11:28
 **/
public class OrderEvaluateModel extends MineModel implements OrderEvaluateContract.Model {
    @Override
    public Observable<CommonResp> commitBooking(String id, String code, String content, String image) {
        return ApiManager.get().kmtApi().commitBooking(id, code, content, image);
    }
}
