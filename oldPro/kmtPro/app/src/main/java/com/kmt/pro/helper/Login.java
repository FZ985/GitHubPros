package com.kmt.pro.helper;

import android.text.TextUtils;

import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.sp.UserSp;

/**
 * Create by JFZ
 * date: 2020-06-30 16:19
 **/
public class Login {
    private static Login login;
    private LoginBean data;
    private boolean isLogin;

    private Login() {
    }

    public static Login get() {
        if (login == null) {
            synchronized (Login.class) {
                if (login == null) {
                    login = new Login();
                }
            }
        }
        return login;
    }

    public LoginBean getData() {
        return data;
    }

    public void setData(LoginBean data) {
        this.data = data;
        //保存邀请码
        if (data != null && !TextUtils.isEmpty(data.higherLevel)) {
            UserSp.get().setInviteCode(data.higherLevel);
        }
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Login setLogin(boolean login) {
        isLogin = login;
        return this;
    }

    public void exit() {
        this.isLogin = false;
        this.data = null;
    }
}
