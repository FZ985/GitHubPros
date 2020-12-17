package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.detail.CommentBean;
import com.kmt.pro.bean.detail.DetailBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.DetailContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-02 16:15
 **/
public class DetailModel implements DetailContract.Model {
    @Override
    public Observable<CommonResp<DetailBean>> queryStorkInformation(String code) {
        return ApiManager.get().kmtApi().queryStorkInformation(code);
    }

    @Override
    public Observable<CommonListResp<CommentBean>> commentList(String code, String current, String size) {
        return ApiManager.get().kmtApi().commentList(code, current, size);
    }
}
