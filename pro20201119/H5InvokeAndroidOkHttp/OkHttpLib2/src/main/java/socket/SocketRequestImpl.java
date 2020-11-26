package socket;

import android.text.TextUtils;

import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.Http;
import com.okhttplib2.utils.OkhttpUtil;

import okhttp3.Request;
import okhttp3.WebSocketListener;

import static com.okhttplib2.utils.OkhttpUtil.log;

public class SocketRequestImpl implements SocketListener {
    private Http.GETBuilder builder;

    public SocketRequestImpl(Http.GETBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void build(WebSocketListener listener) {
        if (TextUtils.isEmpty(builder.url())) {
//            sendFailedCall(callback, -1, new NullPointerException("url can not be null!"));
            return;
        }
        if (!(builder.url().startsWith("http") || builder.url().startsWith("https"))) {
//            sendFailedCall(callback, -1, new IllegalArgumentException("The url prefix is not http or https!"));
            return;
        }
        if (listener == null) return;
        Request request = OkhttpUtil.getRequest(builder, null, null, null);
        if (request == null) return;
        showLoadding();
        log("socket", builder.url() + "<<开始请求时间:" + builder.requestTime());
//        asyncCall(request, callback);
        OkHttpFactory.getInstance().client().newWebSocket(request, listener);
        OkHttpFactory.getInstance().client().dispatcher().executorService().shutdown();
    }

    public void showLoadding() {
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (builder.load() != null && !builder.load().isShowing())
                    builder.load().show();
            }
        });
    }

    public void dismissLoadding() {
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (builder.load() != null && builder.load().isShowing()) {
                    builder.load().dismiss();
                }
            }
        });
    }

}