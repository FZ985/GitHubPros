package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.bean.redpacket.RedPacketRecordBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SelfRedRecordContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-28 16:53
 **/
public class SelfRedRecordModel implements SelfRedRecordContract.Model {
    @Override
    public Observable<CommonResp<RedPacketRecordBean>> redRecordRequest(String id) {
        return ApiManager.get().kmtApi().redRecordRequest(id);
    }

    @Override
    public Observable<CommonListResp<RedDetailListBean>> myRedGrayRequest(String no, String size) {
        return ApiManager.get().kmtApi().myRedGrayRequest(no, size);
    }

    @Override
    public Observable<CommonListResp<RedDetailListBean>> myRedSendRequest(String no, String size) {
        return ApiManager.get().kmtApi().myRedSendRequest(no, size);
    }
}
