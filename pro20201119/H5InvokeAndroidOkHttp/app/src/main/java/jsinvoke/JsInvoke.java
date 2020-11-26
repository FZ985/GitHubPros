package jsinvoke;

import android.app.Activity;
import android.webkit.WebView;

import androidx.annotation.Keep;

@Keep
public interface JsInvoke {

    String invoke(Activity activity, JsInterface jsInterface, String data);

    void recycle();
}