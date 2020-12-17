package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.bean.login.RegisterGetSmsBean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.RegisterContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-07 14:28
 **/
public class RegisterModel implements RegisterContract.LoginModel {

    @Override
    public Observable<CommonResp<RegisterGetSmsBean>> getMsg(String type, String tel, String uuid, String conturyCode) {
        return ApiManager.get().loginApi().getMsg(type, tel, uuid, conturyCode);
    }

    @Override
    public Observable<CommonResp<LoginBean>> userRegister(String tel, String pwd, String code, String conturyCode, String channel, String uuid, String platform, String verCode) {
        return ApiManager.get().loginApi().userRegister(tel, pwd, code, conturyCode, channel, uuid, platform, verCode);
    }

    @Override
    public Observable<CommonResp<LoginBean>> forgetPwd(String tel, String pwd, String code, String uuid) {
        return ApiManager.get().loginApi().forgetPwd(tel, pwd, code, uuid);
    }

    @Override
    public Observable<CommonResp<LoginBean>> fastLoginNew(String tel, String uuid, String code, String appNum) {
        return ApiManager.get().loginApi().fastLoginNew(tel, uuid, code, appNum);
    }
}
