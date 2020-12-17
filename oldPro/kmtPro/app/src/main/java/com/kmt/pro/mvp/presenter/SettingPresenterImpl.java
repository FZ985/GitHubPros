package com.kmt.pro.mvp.presenter;

import com.kmt.pro.helper.Login;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SettingContract;
import com.kmt.pro.mvp.model.SetModel;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-16 13:38
 **/
public class SettingPresenterImpl extends SettingContract.SetPresenter {
    private SettingContract.Model model;

    public SettingPresenterImpl() {
        model = new SetModel();
    }

    @Override
    public void exitApp() {
        model.userLogout().compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            UserSp.get().clear();
                            Login.get().setLogin(false).setData(null);
                            getView().exitSuccess();
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }
}
