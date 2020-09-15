package com.okhttpapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.okhttplib2.OkHttpFactory;

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
        OkHttpFactory.getInstance().obtainHandler().postDelayed(() -> {
            Toast.makeText(this, "dddd", Toast.LENGTH_SHORT).show();
        }, 1000);
    }
}
