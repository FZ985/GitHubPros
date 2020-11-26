package com.h5invokeandroidokhttp;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.Keep;

import java.lang.ref.WeakReference;

import jsinvoke.JsInterface;
import jsinvoke.JsInvoke;

@Keep
public class AndroidJsHelper extends JsInterface {
    private WebView webView;
    private WeakReference<Activity> weak;
    private JsInvoke invoke;

    public AndroidJsHelper(Activity activity, WebView webView) {
        this.weak = new WeakReference<>(activity);
        this.webView = webView;

    }

    @Override
    public WebView getWebView() {
        return webView;
    }

    @Keep
    @JavascriptInterface
    public String invoke(String param1, String params2) {
        try {
            invoke = getInvoke(param1);
            if (invoke != null) {
                return invoke.invoke(weak.get(), this, params2);
            }
        } catch (Exception e) {
            Log.i("invoke", "不支持的调用:" + e.getMessage());
        }
        return "";
    }
}