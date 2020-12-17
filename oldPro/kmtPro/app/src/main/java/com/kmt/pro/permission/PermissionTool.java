package com.kmt.pro.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.kmt.pro.base.BaseApp;
import com.kmtlibs.app.ActivityManager;
import com.kmtlibs.app.utils.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by JFZ
 * on 2020/2/18.
 */

public class PermissionTool {

    private static PermissionTool tool;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private int reqCode;
    private PermissionCall call;

    private PermissionTool() {
    }

    public interface PermissionCall {
        void onPermissionOk();

        //第一个为拒绝的权限, 第二个为勾选不再询问按钮的权限列表
        void onPermissionNo(List<String> faildPermissionList, List<String> donotAsk);

        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    }

    public static PermissionTool get() {
        if (tool == null) {
            tool = new PermissionTool();
        }
        return tool;
    }

    public void setCall(PermissionCall call) {
        if (call != null) {
            this.call = new WeakReference<>(call).get();
        }
    }

    public void requestPermission(Activity act, String[] permissions, int requestCode) {
        requestPermission(act, permissions, requestCode, null);
    }

    public void requestPermission(Activity act, String[] permissions, int requestCode, PermissionCall call) {
        this.reqCode = requestCode;
        setCall(call);
        ActivityCompat.requestPermissions(new WeakReference<>(act).get(), permissions,
                requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Logger.e("权限工具", "onRequestPermissionsResult:" + requestCode + "," + (permissions == null) + "," + (grantResults == null));
        Activity activity = ActivityManager.getAppInstance().currentActivity();
        if (activity == null) return;
        if (requestCode == reqCode && grantResults.length != 0) {
            List<String> requestPermissionList = getPermissionResult(permissions);
            if (requestPermissionList.size() == 0) {
                //权限申请成功
                Logger.e("权限工具", "权限申请成功--");
                if (call != null) {
                    call.onPermissionOk();
                }
                return;
            } else {
                Logger.e("权限工具", "拒绝申请");
                //勾选了对话框中”Don’t ask again”的选项, 返回false
                List<String> reqFialList = new ArrayList<>();
                List<String> fialList = new ArrayList<>();
                for (String deniedPermission : permissions) {
                    fialList.add(deniedPermission);
                    boolean flag = activity.shouldShowRequestPermissionRationale(deniedPermission);
                    if (!flag) {
                        //拒绝授权
                        reqFialList.add(deniedPermission);
                    }
                }
                //拒绝授权
                Logger.e("权限工具", "没有申请222222");
                if (call != null) {
                    call.onPermissionNo(fialList, reqFialList);
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Logger.e("权限工具", "onActivityResult:" + requestCode + "," + resultCode + "," + (data == null));
        if (call != null) call.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private List<String> getPermissionResult(String[] permissions) {
        List<String> requestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            Logger.e("permission", "权限：" + permission);
            if (ContextCompat.checkSelfPermission(BaseApp.getInstance(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission);
            }
        }
        return requestPermissionList;
    }

    public void destry() {
        call = null;
    }
}
