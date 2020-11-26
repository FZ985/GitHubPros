package jsinvoke;

import android.app.Activity;

import androidx.annotation.Keep;

@Keep
public class SocketInvokeImpl implements JsInvoke  {

    @Override
    public String invoke(Activity activity, JsInterface jsInterface, String data) {
        return "";
    }

    @Override
    public void recycle() {

    }
}