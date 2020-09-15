package com.okhttpapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.okhttplib2.HttpImpl;
import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.RequestCallback;

import java.util.logging.Logger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Create by JFZ
 * date: 2020-09-15 13:52
 **/
public class OkHttp2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp2);
    }

    public void syncGet(View view) {
        HttpImpl.get("http://www.baidu.com")
                .bind(this)
                .enqueue(new RequestCallback<String>() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(OkHttp2Activity.this, data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        Toast.makeText(OkHttp2Activity.this, "请求失败:" + code + "," + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
