package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.OneKeyBuyBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.BindFriendContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-16 14:33
 **/
public class BindFriendModel implements BindFriendContract.Model {
    @Override
    public Observable<CommonResp<OneKeyBuyBean>> bindFriend(String code) {
        return ApiManager.get().kmtApi().bindFriend(code);
    }
}
