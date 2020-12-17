package com.kmt.pro.web;

import com.kmt.pro.helper.Login;
import com.kmtlibs.app.utils.Logger;

/**
 * Create by JFZ
 * date: 2020-07-07 14:04
 **/
public class WebHelp {

    public static String appendParams(String url) {
        Logger.e("web拼接_原url:" + url);
        if (!Login.get().isLogin()) {
            return url;
        }
        return url;
    }
}
