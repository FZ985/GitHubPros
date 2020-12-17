package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.mine.AvatorBean;
import com.kmt.pro.bean.mine.LockUpBean;
import com.kmt.pro.bean.mine.MineBean;
import com.kmt.pro.bean.mine.MineItemBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.MineContract;

import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * Create by JFZ
 * date: 2020-07-09 12:06
 **/
public class MineModel implements MineContract.Model {
    @Override
    public Observable<CommonResp<MineBean>> requestPerson() {
        return ApiManager.get().kmtApi().requestPerson();
    }

    @Override
    public Observable<CommonResp<LockUpBean>> lockUp() {
        return ApiManager.get().kmtApi().lockUp();
    }

    @Override
    public Observable<CommonListResp<MineItemBean>> getMineItem() {
        return ApiManager.get().kmtApi().getMineItem();
    }

    @Override
    public Observable<CommonListResp<String>> rollNotices() {
        return ApiManager.get().kmtApi().rollNotices();
    }

    @Override
    public Observable<CommonResp> modifyPersonMessage(String type, String content) {
        return ApiManager.get().kmtApi().modifyPersonMessage(type, content);
    }

    @Override
    public Observable<CommonResp<AvatorBean>> headImgRequest(String type, MultipartBody.Part file) {
        return ApiManager.get().kmtApi().headImgRequest(type, file);
    }
}
