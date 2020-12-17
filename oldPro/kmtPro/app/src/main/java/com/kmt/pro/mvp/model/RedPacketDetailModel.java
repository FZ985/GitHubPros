package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.redpacket.OpenRedBean;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.RedPacketDetailContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-28 11:10
 **/
public class RedPacketDetailModel implements RedPacketDetailContract.Model {

    @Override
    public Observable<CommonResp<OpenRedBean>> openRedRequest(String id) {
        return ApiManager.get().kmtApi().openRedRequest(id);
    }

    @Override
    public Observable<CommonListResp<RedDetailListBean>> grapRedListRequest(String id, String no, String size) {
        return ApiManager.get().kmtApi().grapRedListRequest(id, no, size);
    }
}
