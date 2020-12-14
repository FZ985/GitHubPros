package com.wzcuspro.app.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.wzcuspro.app.utils.Logger;


public abstract class BaseSwipActivity extends SuperInitActivity {
    /**
     * 权限状态
     */
    protected boolean HAS_PERMISSION = false;
    protected static final int REQUECT_CODE_READ_SDCARD = 200;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
    };

    public void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ) {
            // 申请WRITE_EXTERNAL_STORAG|REQUECT_CODE_READ_SDCARD权限
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUECT_CODE_READ_SDCARD);
        } else {
            HAS_PERMISSION = true;
            okPermission();
            System.out.println("已有权限");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUECT_CODE_READ_SDCARD:
                Logger.e("##########onRequestPermissionsResult#############");
                Logger.e("grantResults:" + grantResults.toString());
                if (grantResults.length != 0) {
                    for (int i = 0; i < grantResults.length; i++) {
//                        Logger.e("" + grantResults[i] + "" + Logger.logDetail());
                        HAS_PERMISSION = true;
                        //一个权限没同意
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            HAS_PERMISSION = false;
                            break;
                        }
                    }
//                    Logger.e("Permission has now been granted. Showing preview.="
//                            + HAS_PERMISSION + Logger.logDetail());
                    if (HAS_PERMISSION) {
                        okPermission();
                    } else {
                        noHasPermission();
                    }
                } else {
                    HAS_PERMISSION = false;
                    Logger.e("Permission was NOT granted."
                            + Logger.logDetail());
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void okPermission() {
    }

    public void noHasPermission() {
    }

}
