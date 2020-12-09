package com.okhttpapp;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.okhttpapp.okhttp2.test.IPBean;
import com.okhttpapp.okhttp2.test.JsonReq;
import com.okhttpapp.okhttp2.test.JsonResp;
import com.okhttplib2.HttpImpl;
import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.config.JRequest;

import java.io.File;
import java.util.HashMap;

import expand.DefLoad;
import expand.download.DownLoadBuilder3;
import expand.download.DownLoadListenerAdapter;
import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2020-09-15 13:52
 **/
public class OkHttp2Activity extends AppCompatActivity {
    private String ipUrl = "http://apis.juhe.cn/ip/ipNew";
    private String testUrl = "http://appapi1.lnamphp.com/job/app/job/qrCodeInfo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp2);
    }

    public void syncGet(View view) {
        try {
            Response response = HttpImpl.get("http://www.baidu.com").bind(this).execute();
            if (response != null) {
                String string = response.body().string();
                Toast.makeText(this, "返回:" + string, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "同步catch:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void asyncGet(View view) {
        HttpImpl.get("http://www.baidu.com")
                .bind(this)
                .load(DefLoad.use(this))
                .enqueue(new RequestCallback<String>() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(OkHttp2Activity.this, "请求成功:" + data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        Toast.makeText(OkHttp2Activity.this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void postParams(View view) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ip", "192.168.0.99");
        map.put("key", "9f703454a63103c1588a6abfec6ef91d");
        try {
            IPBean data = (IPBean) HttpImpl.postParams(ipUrl)
                    .request(new JRequest(IPBean.class, map))
                    .executeObject();
            if (data != null) {
                Toast.makeText(this, "请求成功:" + data.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void postAsyncParams(View view) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ip", "192.168.0.99");
        map.put("key", "9f703454a63103c1588a6abfec6ef91d");
        HttpImpl.postParams(ipUrl)
                .request(new JRequest(map))
                .enqueue(new RequestCallback<IPBean>() {
                    @Override
                    public void onResponse(IPBean data) {
                        Toast.makeText(OkHttp2Activity.this, "请求成功:" + data.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        Toast.makeText(OkHttp2Activity.this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void postAsyncJson(View view) {
        JRequest request = new JRequest(new JsonReq("1082565"));
        HttpImpl.postJson(testUrl)
                .request(request)
                .enqueue(new RequestCallback<JsonResp>() {
                    @Override
                    public void onResponse(JsonResp data) {
                        Toast.makeText(OkHttp2Activity.this, "请求成功:" + data.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        Toast.makeText(OkHttp2Activity.this, "请求失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void download(View view) {
        DownLoadBuilder3.getInstance().cancelAll();
        ((Button) view).setText("download");
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator;
        System.out.println(path);
        HttpImpl.download3("https://g37.gdl.netease.com/onmyoji_setup_11.9.0.zip",
                path, "weixin7021android1800_arm64.zip", new DownLoadListenerAdapter() {
                    @Override
                    public void update(long progress, float percent, long contentLength, boolean done) {
                        runOnUiThread(() -> {
                            ((Button) view).setText(progress + "" + percent);
                        });
                    }

                    @Override
                    public void complete(File file) {
                        runOnUiThread(() -> {
                            ((Button) view).setText("下载完成");
                            Toast.makeText(OkHttp2Activity.this, "下载完成:" + file.getPath(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void error(Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(OkHttp2Activity.this, "下载失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    public void cancel(View view) {
        OkHttpFactory.getInstance().cancel("https://g37.gdl.netease.com/onmyoji_setup_11.9.0.zip");
    }
}
