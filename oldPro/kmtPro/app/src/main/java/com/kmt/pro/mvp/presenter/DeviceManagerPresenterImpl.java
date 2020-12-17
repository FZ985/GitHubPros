package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.DeviceManagerBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.DeviceManagerContract;
import com.kmt.pro.mvp.model.DeviceManagerModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-20 15:55
 **/
public class DeviceManagerPresenterImpl extends DeviceManagerContract.DeviceManagerPresenter {
    private DeviceManagerContract.Model model;

    public DeviceManagerPresenterImpl() {
        model = new DeviceManagerModel();
    }

    @Override
    public void requestList() {
        model.queryDeviceList().compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<DeviceManagerBean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonListResp<DeviceManagerBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onList(data.getLIST());
                        } else getView().onListErr(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().onListErr("网络拥堵,稍后重试!");
                    }
                });
    }

    @Override
    public void deleterDevice(String id, int position) {
        model.deleteDevice(id)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onDeleteSuccess(position);
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        Tools.showToast(e.getMessage());
                    }
                });
    }

    @Override
    public void remarkDevice(String id, String name, int position) {
        model.remarkDevice(id, name)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onRemarkDeviceSuccess(position, name);
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        Tools.showToast(e.getMessage());
                    }
                });
    }
}
