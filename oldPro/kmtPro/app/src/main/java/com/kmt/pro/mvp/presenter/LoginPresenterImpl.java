package com.kmt.pro.mvp.presenter;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.bean.login.RegisterGetSmsBean;
import com.kmt.pro.callback.LoginListener;
import com.kmt.pro.helper.Login;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.RegisterContract;
import com.kmt.pro.mvp.model.RegisterModel;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

import java.util.Random;

import renrenkan.Md5Utils;

/**
 * Create by JFZ
 * date: 2020-07-08 11:25
 **/
public class LoginPresenterImpl extends RegisterContract.LoginPresenter {

    private RegisterContract.LoginModel model;

    public LoginPresenterImpl() {
        model = new RegisterModel();
    }

    @Override
    public void sendMSG(String type, String conturyCode, String user_phone) {
        model.getMsg(type, user_phone, UserSp.get().getUuid(), conturyCode)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<RegisterGetSmsBean>>() {
                    @Override
                    public void onResponse(CommonResp<RegisterGetSmsBean> data) {
                        if (!isViewAttached()) return;
                        if ("0".equals(data.getSTATUS())) {
                            Tools.showToast("发送短信成功");
                            if ("9".equals(type)) {
                                startTimeDown(getView().getFastSmsCodeTv());
                            } else {
                                startTimeDown(getView().getNumberSmsCodeTv());
                            }
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
    public void numLogin(String phone, String pwd, String smsCode, Loadding loadding) {
        final String password = Md5Utils.MD5(pwd);
        userLogin(phone, password, smsCode, "", loadding, new LoginListener() {
            @Override
            public void loginSuccess(LoginBean data) {
                if (!isViewAttached()) return;
                if (loadding != null && loadding.isShowing()) {
                    loadding.dismiss();
                }
                Login.get().setLogin(true);
                Login.get().setData(data);
                UserSp.get()
                        .setCodePassword("")
                        .setPassword(password)
                        .setMobile(phone);
                getView().loginSuccess();
            }

            @Override
            public void loginOtherStatus(CommonResp<LoginBean> data) {
                if (!isViewAttached()) return;
                if (data.getSTATUS().equals("11")) {
                    getView().getNumberSmsRL().setVisibility(View.VISIBLE);
                    getView().getNumberSmsET().setFocusable(true);
                    getView().getNumberSmsET().requestFocus();
                    startTimeDown(getView().getNumberSmsCodeTv());
                }
                if (loadding != null && loadding.isShowing()) {
                    loadding.dismiss();
                }
            }

            @Override
            public void loginErr(String err) {
                if (loadding != null && loadding.isShowing()) {
                    loadding.dismiss();
                }
                Tools.showToast(err);
            }
        });
    }

    @Override
    public void fastLogin(String phone, String smsCode) {
        final String random = getRandomString(26);
        model.fastLoginNew(phone, UserSp.get().getUuid(), smsCode, random)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<LoginBean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp<LoginBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            UserSp.get()
                                    .setCodePassword(random + smsCode)
                                    .setPassword("")
                                    .setMobile(phone);
                            Login.get().setLogin(true);
                            Login.get().setData(data.OBJECT);
                            saveUserData(data.OBJECT);
                            getView().loginSuccess();
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

    private CountDownTimer timer;

    private void startTimeDown(TextView textView) {
        if (timer != null) {
            timer.cancel();
            timer.onFinish();
        }
        timer = null;
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isViewAttached() && textView != null) {
                    textView.setClickable(false);
                    textView.setEnabled(false);
                    textView.setText(millisUntilFinished / 1000 + "秒");
                    textView.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.black33));
                }
            }

            @Override
            public void onFinish() {
                if (isViewAttached() && textView != null) {
                    textView.setText("获取验证码");
                    textView.setClickable(true);
                    textView.setEnabled(true);
                    textView.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.black66));
                }
            }
        };
        timer.start();
    }

    // 26位的随机数
    private static String getRandomString(int length) {
        //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    @Override
    public void dettachView() {
        if (timer != null) {
            timer.cancel();
        }
        super.dettachView();
        timer = null;
    }
}
