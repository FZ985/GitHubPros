package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.login.RegisterGetSmsBean;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.ForgetPayPwdContract;
import com.kmt.pro.mvp.model.ForgetPayPwdModel;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-20 12:04
 **/
public class ForgetPayPwdPresenterImpl extends ForgetPayPwdContract.ForgetPayPwdPresenter {
    private ForgetPayPwdModel model;

    public ForgetPayPwdPresenterImpl() {
        model = new ForgetPayPwdModel();
    }

    @Override
    public void sendSms(String type, String mobile, String countryCode) {
        model.getMsg(type, mobile, UserSp.get().getUuid(), countryCode)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<RegisterGetSmsBean>>() {
                    @Override
                    public void onResponse(CommonResp<RegisterGetSmsBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().sendSmsCodeSuccess();
                        } else if (data.getSTATUS().equals("3")) {
                            Login((bool) -> {
                                if (bool) sendSms(type, mobile, countryCode);
                                else Tools.showToast(data.MSG);
                            });
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }

    @Override
    public void checkSmsCode(String code) {
        model.checkSmsCode(code)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().sendCheck();
                        } else if (data.getSTATUS().equals("3")) {
                            Login((b) -> {
                                if (b)checkSmsCode(code);
                                else Tools.showToast(data.MSG);
                            });
                        } else {
                            Tools.showToast(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }
}
