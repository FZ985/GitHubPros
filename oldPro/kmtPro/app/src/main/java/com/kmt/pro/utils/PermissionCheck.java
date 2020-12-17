package com.kmt.pro.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.kmt.pro.base.BaseApp;
import com.kmtlibs.app.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionCheck {

    public static final String[] PERMISSIONS_READ_AND_WRITE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static final String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final String[] PERMISSION_PHONE_READ_STATE = {
            Manifest.permission.READ_PHONE_STATE
    };

    public static void reqCamera(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, requestCode);
    }

    public static void reqPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean checkReadWritePermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static boolean checkCameraPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static boolean checkSplashPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static boolean checkPhoneReadPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }

    public static boolean checkClander(Context context) {
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }

    public static List<String> getPermissionResult(String[] permissions) {
        List<String> requestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            Logger.e("permission", "权限：" + permission);
            if (ContextCompat.checkSelfPermission(BaseApp.getInstance(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission);
            }
        }
        return requestPermissionList;
    }
}
