package com.android11compat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android11compat.sdcardcompat.SdCard;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    }

    public void onSdcardCompat(View view) {

//        SdCard.createConfigFile();
//        SdCard.createGroupFile();
//        SdCard.createQrcodeLog();
        SdCard.createQunNameFile();
    }

    public void write(View view) {
        SdCard.writeQunName("ddddddd" + System.currentTimeMillis());
    }

    public void isExits(View view) {
        String path = Environment.getExternalStorageDirectory() + SdCard.groupPath;
        File file = new File(path);
        System.out.println("是否存在:" + file.exists());
        File[] files = Environment.getExternalStorageDirectory().listFiles();
        for (File f : files) {
            System.out.println("目录:" + f.getAbsolutePath());
        }

        if (Environment.isExternalStorageManager()) {
//            writeFile();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 200);
        }
    }
}