package com.kmt.pro.callback;

import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.http.response.CommonResp;

/**
 * Create by JFZ
 * date: 2020-06-30 16:08
 **/
public interface LoginListener {

    void loginSuccess(LoginBean data);

    void loginOtherStatus(CommonResp<LoginBean> data);

    void loginErr(String err);

}
