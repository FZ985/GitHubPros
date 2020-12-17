package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.OneKeyBuyBean;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.BindFriendContract;
import com.kmt.pro.mvp.model.BindFriendModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-16 14:33
 **/
public class BindFriendPresenterImpl extends BindFriendContract.BindFriendPresenter {

    private BindFriendContract.Model model;

    public BindFriendPresenterImpl() {
        model = new BindFriendModel();
    }

    @Override
    public void bindFriend(String code) {
        model.bindFriend(code)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<OneKeyBuyBean>>() {
                    @Override
                    public void onResponse(CommonResp<OneKeyBuyBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onBindSuccess(data.OBJECT,code);
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        Tools.showToast(e.getMessage());
                    }
                });
    }
}
