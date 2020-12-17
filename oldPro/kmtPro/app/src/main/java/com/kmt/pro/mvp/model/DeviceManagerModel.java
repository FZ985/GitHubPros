package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.DeviceManagerBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.DeviceManagerContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-20 15:54
 **/
public class DeviceManagerModel implements DeviceManagerContract.Model {
    @Override
    public Observable<CommonListResp<DeviceManagerBean>> queryDeviceList() {
        return ApiManager.get().kmtApi().queryDeviceList();
    }

    @Override
    public Observable<CommonResp> deleteDevice(String id) {
        return ApiManager.get().kmtApi().deleteDevice(id);
    }

    @Override
    public Observable<CommonResp> remarkDevice(String id, String note) {
        return ApiManager.get().kmtApi().remarkDevice(id, note);
    }
}
