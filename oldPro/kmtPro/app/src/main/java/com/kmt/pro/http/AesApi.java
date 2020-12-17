package com.kmt.pro.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.security2.AesEncryptionUtil;

/**
 * Create by JFZ
 * date: 2020-04-17 15:28
 **/
public class AesApi {

    public static String encrypt(String content) {
        return AesEncryptionUtil.encrypt(content, KConstant.Service_Key, KConstant.Service_KeyIv);
    }

    public static String encryptObj(Object obj) {
        return encrypt(new Gson().toJson(obj));
    }

    public static String decrypt(String content) {
        try {
            if (!TextUtils.isEmpty(content)) {
                String result = AesEncryptionUtil.decrypt(content, KConstant.Service_Key, KConstant.Service_KeyIv);
                if (!TextUtils.isEmpty(result)) {
                    return result;
                }
                return content;
            }
            return content;
        } catch (Exception e) {

        }
        return content;
    }

}
