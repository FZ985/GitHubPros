package com.kmt.pro.http.interceptor;


import com.kmt.pro.base.BaseApp;
import com.kmt.pro.utils.DeviceInfoUtils;
import com.kmt.pro.utils.SystemUtils;
import com.kmtlibs.app.utils.Logger;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import renrenkan.Md5Utils;

public class CommonParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String version = DeviceInfoUtils.getVersionName(BaseApp.getInstance());
        Request oldRequest = chain.request();
        String url = oldRequest.url().toString();
        String useUrl = "";
//        Logger.v("REQ_请求原url:" + url);
        if (url.contains("?")) {
            String tmp = url.substring(url.lastIndexOf("api"), url.lastIndexOf("/"));
            if (tmp.contains(".")) {
                useUrl = (tmp.substring(tmp.lastIndexOf("api/"), tmp.lastIndexOf("?"))).replace("api/", "");
            } else {
                useUrl = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
            }
        } else {
            useUrl = url.substring(url.lastIndexOf("/") + 1);
        }
        //接口名后三位
        StringBuilder builder = new StringBuilder(useUrl.substring(useUrl.length() - 6, useUrl.length() - 3));
        //参数名排序
        Set<String> set = oldRequest.url().queryParameterNames();
        List<String> list = new ArrayList(set);
        list.add("v");
        list.add("p");
        Collections.sort(list, Collator.getInstance(java.util.Locale.ENGLISH));
        //参数名后三位
        for (int i = 0; i < list.size(); i++) {
            String value;
            if ("v".equals(list.get(i))) {
                value = version;
            } else if ("p".equals(list.get(i))) {
                value = "anzhuo";
            } else {
                value = oldRequest.url().queryParameter(list.get(i));
            }
            if (value != null && !"".equals(value)) {
                Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
                Matcher emojiMatcher = emoji.matcher(value);
                if (emojiMatcher.find()) {
                    builder.append("123");
                } else if (value.length() > 3) {
                    String val = value.substring(value.length() - 3);
                    builder.append(val);
                } else {
                    builder.append(value);
                }
            }
            if (i == 5) {
                break;
            }
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String token = Md5Utils.getToken(
                builder.toString(),
                timeStamp.substring(timeStamp.length() - 3));
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("timeStamp", timeStamp)
                .addQueryParameter("v", version)
                .addQueryParameter("p", "anzhuo")
                //.addQueryParameter("token", Md5Utils.MD5(builderFinaly.append(timeStamp.substring(timeStamp.length() - 3)).toString()));
                .addQueryParameter("token", token);

        Request newRequest = oldRequest.newBuilder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", SystemUtils.getPlatform() + DeviceInfoUtils.getSeviceVendor() + "(" + DeviceInfoUtils.getDeviceModel() + ")")
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();
        Logger.e("REQ_请求地址final:" + newRequest.url().toString());
        return chain.proceed(newRequest);
    }
}
