package com.kmt.pro.mvp.presenter;

import com.kmt.pro.helper.Login;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SetPayPwdContract;
import com.kmt.pro.mvp.model.SetPayPwdModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-20 14:48
 **/
public class SetPayPwdPresenterImpl extends SetPayPwdContract.SetPayPwdPresenter {
    private SetPayPwdContract.Model model;

    public SetPayPwdPresenterImpl() {
        model = new SetPayPwdModel();
    }

    @Override
    public void reqSetPayPwd(String pwd, String type) {
        model.setPayPwd(pwd, type)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            Login.get().getData().setUserPayStats("1");
                            getView().setPayPwdSuccess();
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }

    @Override
    public void reqFoundPayPwd(String pwd, String smsCode, String type) {
        model.foundPayPwd(pwd, smsCode, type)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            Login.get().getData().setUserPayStats("1");
                            getView().foundPayPwdSuccess();
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }
}
