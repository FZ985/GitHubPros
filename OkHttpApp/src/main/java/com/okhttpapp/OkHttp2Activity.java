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
import okhttp3.Response;

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
}
