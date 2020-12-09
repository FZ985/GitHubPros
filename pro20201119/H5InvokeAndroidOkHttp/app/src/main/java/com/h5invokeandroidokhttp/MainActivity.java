package com.h5invokeandroidokhttp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.okhttplib2.HttpImpl;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.upload.UIProgressRequestListener;

import java.io.File;

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
    }

    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void ddd(View view) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 100);
        } else {
            File file = new File(Environment.getExternalStorageDirectory(), "hh.png");
            System.out.println("sdcard:" + file.getPath());
            HttpImpl.postForm("https://api.lanjinqd.com/WebService3.asmx/GetImageJson")
                    .enqueueUploadFile(new File[]{file}, new String[]{"path"}, new RequestCallback<String>() {
                        @Override
                        public void onResponse(String data) {
                            System.out.println("upload_cuccess:" + data);
                        }

                        @Override
                        public void onError(int code, Exception e) {
                            System.out.println("upload_error:" + e.getMessage());

                        }
                    }, new UIProgressRequestListener() {
                        @Override
                        public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                            System.out.println("upload==" + bytesWrite);
                        }
                    });
        }
    }
}