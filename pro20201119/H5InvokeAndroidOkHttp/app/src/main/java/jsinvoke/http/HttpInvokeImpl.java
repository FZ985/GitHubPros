package jsinvoke.http;

import android.app.Activity;
import android.text.TextUtils;

import androidx.annotation.Keep;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.okhttplib2.HttpImpl;
import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.Http;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.config.JRequest;
import com.okhttplib2.config.OkHttpConfig;
import com.okhttplib2.utils.OkhttpUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import expand.DefLoad;
import jsinvoke.JsCall;
import jsinvoke.JsInterface;
import jsinvoke.JsInvoke;

@Keep
public class HttpInvokeImpl implements JsInvoke {
    private String get = "get";
    private String post = "post";
    private String postParams = "postParams";
    private String postFrom = "postFrom";
    private String tag = "invokeHttp";

    @Override
    public String invoke(Activity activity, JsInterface helper, String data) {
        Map<String, HttpData> map = getMap(data);
        log("mapSize:" + map.size());
        if (activity == null || helper == null || TextUtils.isEmpty(data))
            return "";
        try {
            if (map.size() > 0) {
                Http.Builder builder = null;
                HttpData http = null;
                JRequest request = null;
                if (map.containsKey(get)) {
                    //get请求
                    http = map.get(get);
                    builder = HttpImpl.get(getUrl(http));
                } else if (map.containsKey(post)) {
                    //postJson请求
                    http = map.get(post);
                    builder = HttpImpl.postJson(getUrl(http));
                } else if (map.containsKey(postParams)) {
                    //postParams
                    http = map.get(postParams);
                    builder = HttpImpl.postParams(getUrl(http));
                } else if (map.containsKey(postFrom)) {
                    //post表单请求
                    http = map.get(postFrom);
                    builder = HttpImpl.postForm(getUrl(http));
                }
                if (http != null && builder != null) {
                    builder.request(request = new JRequest(http.getParams()));
                    if (http.getTimeout() != 0) {
                        OkHttpConfig.getInstance().setTimeout(http.getTimeout()).init();
                    }
                    List<JRequest.JRequestHeaders> headers = http.getHeaders();
                    if (request != null && headers != null && headers.size() > 0)
                        request.addHeaders(headers);
                    log("#####invoke请求数据:" + http.toString());
                    Http.Builder finalBuilder = builder;
                    if (http.loading) {
                        OkHttpFactory.getInstance().obtainHandler().post(() -> {
                            finalBuilder.load(DefLoad.use(activity));
                        });
                    }
                    if (http.async) {
                        //异步
                        HttpData finalHttp = http;
                        finalBuilder.enqueue(new RequestCallback<String>() {
                            @Override
                            public void onResponse(String data) {
                                if (helper != null && !TextUtils.isEmpty(finalHttp.success)) {
                                    helper.load("javascript:" + finalHttp.success + "(" + new Gson().toJson(new InvokeResponse(data)) + ")");
                                }
                            }

                            @Override
                            public void onError(int code, Exception e) {
                                if (helper != null && !TextUtils.isEmpty(finalHttp.error)) {
                                    helper.load("javascript:" + finalHttp.error + "(" + new Gson().toJson(new InvokeResponse(code, e.getMessage())) + ")");
                                }
                            }
                        });
                    } else {
                        //同步
                        String ss = (String) finalBuilder.executeObject();
                        return new Gson().toJson(new InvokeResponse(ss));
                    }
                }
            }
        } catch (Exception e) {
            log("fail:" + e.getMessage());
        }
        return "";
    }

    private String getUrl(HttpData data) {
        if (data != null) {
            if (!TextUtils.isEmpty(data.getUrl()) && data.getUrl().startsWith("http"))
                return data.getUrl();
            return data.getBaseUrl() + data.getUrl();
        }
        return "";
    }

    private void log(String m) {
        OkhttpUtil.log(tag, m);
    }

    @Override
    public void recycle() {
    }

    private Map<String, HttpData> getMap(String data) {
        Map<String, HttpData> newMap = new HashMap<>();
        try {
            Map<String, HttpData> map = new Gson().fromJson(data, new TypeToken<Map<String, HttpData>>() {
            }.getType());
            if (map != null) {
                Iterator<String> iterator = map.keySet().iterator();
                if (iterator.hasNext()) {
                    String next = iterator.next();
                    newMap.put(next, map.get(next));
                }
                return newMap;
            }
        } catch (JsonSyntaxException e) {
            log("invokeJsonHttp解析失败：" + e.getMessage());
        }
        return new HashMap<>();
    }

    @Keep
    private static class InvokeResponse implements Serializable {
        private int code;
        private String message;
        private String data;

        public InvokeResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public InvokeResponse(String data) {
            this.code = 200;
            this.data = data;
        }
    }

    @Keep
    public static class HttpData extends JsCall {
        public boolean async = true;
        public boolean loading = false;
        private int timeout;
        private String baseUrl;
        private String url;
        private HashMap<String, Object> params;
        private HashMap<String, Object> headers;

        public String getBaseUrl() {
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = "";
            }
            return baseUrl;
        }

        public String getUrl() {
            if (TextUtils.isEmpty(url)) {
                url = "";
            }
            return url;
        }

        public HashMap<String, Object> getParams() {
            if (params == null) {
                params = new HashMap<>();
            }
            return params;
        }

        public List<JRequest.JRequestHeaders> getHeaders() {
            if (headers != null && headers.size() > 0) {
                List<JRequest.JRequestHeaders> list = new ArrayList<>();
                Iterator<Map.Entry<String, Object>> iterator = headers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> next = iterator.next();
                    list.add(new JRequest.JRequestHeaders(next.getKey(), next.getValue().toString()));
                }
                return list;
            }
            return new ArrayList<>();
        }

        public int getTimeout() {
            return timeout;
        }

        @Override
        public String toString() {
            return "HttpData{" +
                    "async=" + async +
                    ", loading=" + loading +
                    ", timeout=" + timeout +
                    ", baseUrl='" + baseUrl + '\'' +
                    ", url='" + url + '\'' +
                    ", params=" + params +
                    ", headers=" + headers +
                    ", success='" + success + '\'' +
                    ", error='" + error + '\'' +
                    '}';
        }
    }
}