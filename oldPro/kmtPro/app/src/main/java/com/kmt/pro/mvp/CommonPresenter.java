package com.kmt.pro.mvp;


import android.text.TextUtils;
import android.widget.EditText;

import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BasePresenter;
import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.AppVersionBean;
import com.kmt.pro.bean.KeFu;
import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.callback.AutoCallBack;
import com.kmt.pro.callback.HttpCallback;
import com.kmt.pro.callback.LoginListener;
import com.kmt.pro.helper.Login;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.utils.DeviceInfoUtils;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-05-12 11:48
 * 全局请求接口放在这里，每个页面模块可以继承此
 **/
public class CommonPresenter<V extends BaseView> extends BasePresenter<V> {

    //用户登录
    public void userLogin(LoginListener listener) {
        userLogin(UserSp.get().getMobile(), UserSp.get().getPassword(), "", "1", null, listener);
    }

    public void userLogin(String phone, String password, String smsCode, String isAuto, Loadding loadding, LoginListener listener) {
        ApiManager.get().loginApi().multiDevLogin(phone, password, UserSp.get().getUuid(), smsCode, isAuto)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<LoginBean>>(loadding) {
                    @Override
                    public void onResponse(CommonResp<LoginBean> data) {
                        if (data.getSTATUS().equals("0")) {
                            saveUserData(data.OBJECT);
                            listener.loginSuccess(data.OBJECT);
                        } else if (data.getSTATUS().equals("11")) {
                            listener.loginOtherStatus(data);
                        } else {
                            listener.loginErr(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        listener.loginErr(e.getMessage());
                    }
                });
    }

    //应用登录
    public void applicationLogin(LoginListener listener) {
        ApiManager.get().loginApi().applicationLogin(UserSp.get().getMobile(), UserSp.get().getUuid(), UserSp.get().getCodePassword(), "1")
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<LoginBean>>() {
                    @Override
                    public void onResponse(CommonResp<LoginBean> data) {
                        if (data.getSTATUS().equals("0")) {
                            saveUserData(data.OBJECT);
                            listener.loginSuccess(data.OBJECT);
                        } else listener.loginErr(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        listener.loginErr(e.getMessage());
                    }
                });
    }

    public void saveUserData(LoginBean data) {
        if (data != null) {
            UserSp.get()
                    .setUserId(data.userId)
                    .setHeadImg(data.userAvatar)
                    .setNickName(data.userName)
                    .setHxId(data.userEasemobId)
                    .setBrokerNo(data.brokerNo);
        }
    }

    public boolean checkEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        return TextUtils.isEmpty(text);
    }

    public boolean checkPhoneLength(String countryCode, EditText editText) {
        if (countryCode.equals("86")) {
            if (editText.getText().toString().trim().length() != 11) {
                Tools.showToast("手机号不足11位");
                return false;
            }
        }
        return true;
    }

    public boolean checkPassword(EditText editText) {
        String pwd = editText.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Tools.showToast("请输入密码");
            return false;
        }
        if (pwd.length() < 6) {
            Tools.showToast("密码最少6位");
            return false;
        }
        if (pwd.length() > 16) {
            Tools.showToast("密码不能超过16位");
            return false;
        }
        return true;
    }

    // 判断是否可以自动登录
    public boolean autoLoginAlbe() {
        String userName = UserSp.get().getMobile();
        String userPwd = UserSp.get().getPassword();
        String userCodePwd = UserSp.get().getCodePassword();
        if (!"".equals(userName) && !"".equals(userPwd)) {
            return true;
        } else if (!"".equals(userName) && !"".equals(userCodePwd)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLogin() {
        return Login.get().isLogin();
    }

    public void Login(AutoCallBack callBack) {
        if (isLogin() && autoLoginAlbe()) {
            userLogin(new LoginListener() {
                @Override
                public void loginSuccess(LoginBean data) {
                    Login.get().setLogin(true);
                    Login.get().setData(data);
                    callBack.callBack(true);
                    Logger.i("自动登录成功");
                }

                @Override
                public void loginOtherStatus(CommonResp<LoginBean> data) {

                }

                @Override
                public void loginErr(String err) {
                    Login.get().setLogin(false);
                    Login.get().setData(null);
                    callBack.callBack(false);
                }
            });
        } else callBack.callBack(false);
    }

    public void checkAppVersion(Loadding loadding, HttpCallback.AppVersionCallback callback) {
        ApiManager.get().kmtApi().checkVerison(DeviceInfoUtils.getVersionName(BaseApp.getInstance()),
                "android",
                UserSp.get().getUuid(),
                DeviceInfoUtils.getChannel(BaseApp.getInstance()))
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<AppVersionBean>>(loadding) {
                    @Override
                    public void onResponse(CommonResp<AppVersionBean> data) {
                        if (data.getSTATUS().equals("0")) {
                            if (callback != null) callback.onVersion(data.OBJECT);
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }

    public void kefu(HttpCallback.KeFuCall call) {
        ApiManager.get().kmtApi().customerService()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<KeFu>>() {
                    @Override
                    public void onResponse(CommonResp<KeFu> data) {
                        if (call != null) {
                            call.onData(data.OBJECT);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }
}
