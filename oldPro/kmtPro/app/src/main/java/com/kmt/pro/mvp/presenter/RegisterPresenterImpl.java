package com.kmt.pro.mvp.presenter;

import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.bean.login.RegisterGetSmsBean;
import com.kmt.pro.helper.Login;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.RegisterContract;
import com.kmt.pro.mvp.model.RegisterModel;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.utils.DeviceInfoUtils;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

import renrenkan.Md5Utils;

/**
 * Create by JFZ
 * date: 2020-07-07 14:28
 **/
public class RegisterPresenterImpl extends RegisterContract.RegisterPresenter {
    private RegisterContract.Model model;

    public RegisterPresenterImpl() {
        model = new RegisterModel();
    }

    @Override
    public void sendSms(boolean isForget, String tel, String countryCode) {
        model.getMsg(isForget ? "1" : "0", tel, UserSp.get().getUuid(), countryCode)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<RegisterGetSmsBean>>() {
                    @Override
                    public void onResponse(CommonResp<RegisterGetSmsBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            //倒计时
                            Tools.showToast("发送短信成功");
                            getView().startTimeDown();
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

    @Override
    public void register(String phone, String sms, String pwd, String countryCode) {
        String password = Md5Utils.MD5(pwd);
        model.userRegister(phone, password, sms, countryCode, DeviceInfoUtils.getChannel(BaseApp.getInstance()),
                UserSp.get().getUuid(), "Android", DeviceInfoUtils.getVersionName(BaseApp.getInstance()))
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<LoginBean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp<LoginBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            UserSp.get().setMobile(phone).setPassword(password);
                            Login.get().setLogin(true);
                            Login.get().setData(data.OBJECT);
                            Tools.showToast("注册成功");
                            saveUserData(data.OBJECT);
                            getView().registerSuccess();
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

    @Override
    public void forget(String phone, String sms, String pwd, String countryCode) {
        String password = Md5Utils.MD5(pwd);
        model.forgetPwd(phone, password, sms, UserSp.get().getUuid())
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<LoginBean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp<LoginBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            UserSp.get().setMobile(phone).setPassword(password);
                            Login.get().setLogin(true);
                            Login.get().setData(data.OBJECT);
                            getView().registerSuccess();
                        } else if ("3".equals(data.getSTATUS())) {
                            Login((bool) -> {
                                if (bool) {
                                    forget(phone, sms, pwd, countryCode);
                                } else Tools.showToast(data.MSG);
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
