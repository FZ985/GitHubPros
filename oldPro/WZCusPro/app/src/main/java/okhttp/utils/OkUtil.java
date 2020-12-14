package okhttp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp.Params;


/**
 * Created by JFZ .
 * on 2018/1/15.
 */

public class OkUtil {
    private static final String TAG = "*****Okhttp看门狗*****";
    private static boolean DEBUG = true;
    public static final Gson GSON = new Gson();

    public static void log(String msg) {
        log(TAG, msg);
    }

    public static void log(String tag, String msg) {
        String result = "\n";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result += thisMethodStack.getClassName() + "."; // 当前的类名（全名）
        result += thisMethodStack.getMethodName();
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")  \n";
        if (DEBUG) {
            Log.i(tag, result + "\n" + msg);
        }
        if (TextUtils.isEmpty(tag)) {
            Log.e(TAG, result + "\n" + msg);
        } else {
            Log.e(tag, result + "\n" + msg);
        }
    }

    public static void d(String value) {
        if (DEBUG) {
            Log.d(TAG, value);
        }
    }

    public static void e(String value) {
        if (DEBUG) {
            Log.d(TAG, value);
        }
    }

    /**
     * 检查url
     **/
    public static boolean checkUrl(String url) {
        return TextUtils.isEmpty(url);
    }

    public static Params[] validateParam(Params[] params) {
        if (params == null)
            return new Params[0];
        else
            return params;
    }

    /**
     * url拼接
     **/
    public static String splicingParams(Params... params) {
        if (params.length == 0) {
            return "";
        }
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Params p : params) {
            if (pos > 0) {
                sb.append("&");
            }
            sb.append(p.key).append("=").append(p.value);
            pos++;
        }

//        OkUtil.log(">>>>拼接:" + sb.toString());
        return sb.toString();
    }

    public static Params[] appendParams(HashMap<String, Object> map) {
        Params[] params = null;
        if (map != null) {
            Iterator iter = map.entrySet().iterator();
            List<Params> paramsList = new ArrayList<Params>();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
//                OkUtil.log("key", "get_key:" + key.toString());
//                OkUtil.log("value", "get_value:" + val.toString());
                if (val == null) {
                    val = "";
                }
                paramsList.add(new Params(key.toString(), val.toString()));
            }
            params = new Params[paramsList.size()];
//            Log.e("params", "params:" + params.length);
            for (int i = 0; i < paramsList.size(); i++) {
                params[i] = new Params(paramsList.get(i).key, paramsList.get(i).value);
            }
        } else {
            params = new Params[0];
        }
        return params;
    }

    public static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static String reqParams(Object src) {
        String json = "";
        if (src != null && src instanceof String) {
            json = (String) src;
        } else {
            if (src == null) {
            } else {
                json = GSON.toJson(src);
            }
        }
        return json;
    }

    /**
     * 获取手机是否链接网络
     *
     * @param context
     * @return
     */
    public static boolean isConnection(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable;
        if (networkInfo != null) {
            isAvailable = networkInfo.isAvailable();
        } else {
            isAvailable = false;
        }
        Log.i("ConnectionVerdict", isAvailable + "");
        return isAvailable;
    }

    /**
     * 获取联网的Manager
     *
     * @param context
     * @return
     */
    private static ConnectivityManager getConnectivityManager(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return null;
        }
        return mConnectivityManager;
    }

    public static void closeAll(Closeable... closeables) {

        if (closeables == null) {
            return;
        }

        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
