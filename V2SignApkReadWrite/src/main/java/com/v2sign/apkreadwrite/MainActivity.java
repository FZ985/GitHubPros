package com.v2sign.apkreadwrite;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView main_tv;
    private String root = Environment.getExternalStorageDirectory().getAbsolutePath();
    private File file = new File(root + File.separator + "11.apk");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_tv = findViewById(R.id.main_tv);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    public void write(View view) {
        ApkInfo.write(file);
    }

    public void read(View view) {
        String string = ApkInfo.readSelf(this);
        StringBuilder s = new StringBuilder();
        s.append("读取内容:");
        s.append(string + "");
        main_tv.setText(s);
        Toast.makeText(this, s.toString(), Toast.LENGTH_LONG).show();
    }
}