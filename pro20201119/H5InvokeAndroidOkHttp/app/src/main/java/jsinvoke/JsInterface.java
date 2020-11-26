package jsinvoke;

import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.Keep;

@Keep
public abstract class JsInterface {


    @Keep
    public abstract WebView getWebView();

    @Keep
    public JsInvoke getInvoke(String name) throws Exception {
        String parentPath = JsInvoke.class.getName();
        String payImplPath = parentPath.substring(0, parentPath.lastIndexOf(JsInvoke.class.getSimpleName()));
        String className = name.substring(0, 1).toUpperCase() + name.substring(1) + "InvokeImpl";
        return (JsInvoke) Class.forName(payImplPath + name + "." + className).newInstance();
    }

    @Keep
    @JavascriptInterface
    public void load(String methods) {
        Log.e("invokeHttp", "webview == null:" + (getWebView() == null));
        if (getWebView() != null) {
            Log.e("invokeHttp", "调用h5:" + methods);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWebView().evaluateJavascript(methods, null);
            } else {
                getWebView().loadUrl(methods);
            }
        }
    }
}