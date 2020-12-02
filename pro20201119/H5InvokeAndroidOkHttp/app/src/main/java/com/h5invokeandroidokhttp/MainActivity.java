package com.h5invokeandroidokhttp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.okhttplib2.HttpImpl;

public class MainActivity extends AppCompatActivity {
    private WebView main_web;
    private AndroidJsHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_web = findViewById(R.id.main_web);
        helper = new AndroidJsHelper(this, main_web);
        initWeb();
        main_web.loadUrl("file:///android_asset/test.html");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        main_web.getSettings().setJavaScriptEnabled(true);
        main_web.getSettings().setAllowFileAccessFromFileURLs(true);
        main_web.addJavascriptInterface(helper, "app");
        main_web.setWebChromeClient(new WebChromeClient());

        HttpImpl.postJson("")
                .bind(this)
                .enqueue(null);
    }
}