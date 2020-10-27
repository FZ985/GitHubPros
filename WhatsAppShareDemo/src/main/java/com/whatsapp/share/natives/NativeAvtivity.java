package com.whatsapp.share.natives;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.whatsapp.share.R;

import java.io.File;
import java.util.List;

public class NativeAvtivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natives);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    public void shareText(View view) {
        WhatsNative.shareText(this, "https://www.baidu.com");
    }

    public void shareFileImage(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        WhatsNative.shareFileImage(this, file);
    }

    public void shareWebImage(View view) {
        WhatsNative.shareUrlImage(this,
//                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1600941071891&di=e00a1b9e3afe65c689aff723d80dcbdf&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201504%2F02%2F20195964e0xt3081es8syt.jpg");
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1600942587643&di=29c7f19f98b97de88dd1d27bd6608d2f&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201304%2F18%2F001339jv88x0qs06vo3qq6.jpg");
    }

    public void test(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        List<ResolveInfo> resolveInfos = this.getPackageManager().queryIntentActivities(intent, 0);

        for (int i = 0; i < resolveInfos.size(); i++) {
            System.out.println("应用列表:" + resolveInfos.get(i).activityInfo.toString());
        }
    }
}
