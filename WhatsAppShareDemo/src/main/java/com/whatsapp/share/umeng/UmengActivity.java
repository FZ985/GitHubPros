package com.whatsapp.share.umeng;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.whatsapp.share.R;
import com.whatsapp.share.umeng.share.UMShareListenerImpl;
import com.whatsapp.share.umeng.share.UShare;

import java.io.File;

public class UmengActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umeng);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    public void shareText(View view) {
        UShare.shareText(this, "hello", new UMShareListenerImpl());
    }

    public void shareFileImage(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        UShare.shareFileImage(this, file, new UMShareListenerImpl());
    }

    public void shareWebImage(View view) {
        UShare.shareWebImage(this,
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1600928326169&di=c9c656e55d4956c6d95c57322f219cdc&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201503%2F19%2F211608ztcq7higicydxhsy.jpg",
                new UMShareListenerImpl());
    }

}
