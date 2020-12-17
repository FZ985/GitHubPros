package com.kmt.pro.callback;

import com.kmt.pro.bean.AppVersionBean;
import com.kmt.pro.bean.KeFu;

/**
 * Create by JFZ
 * date: 2020-07-09 11:57
 **/
public interface HttpCallback {

    interface AppVersionCallback {
        void onVersion(AppVersionBean data);
    }

    interface KeFuCall {
        void onData(KeFu data);
    }
}
