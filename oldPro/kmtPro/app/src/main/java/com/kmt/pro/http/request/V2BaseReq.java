package com.kmt.pro.http.request;


import com.kmt.pro.base.BaseApp;
import com.kmt.pro.utils.DeviceInfoUtils;

import java.io.Serializable;

public class V2BaseReq<T> implements Serializable {

    public String os = "android";
    public String ver = DeviceInfoUtils.getVersionName(BaseApp.getInstance());
    public T pars;

    public void setPars(T pars) {
        this.pars = pars;
    }
}
