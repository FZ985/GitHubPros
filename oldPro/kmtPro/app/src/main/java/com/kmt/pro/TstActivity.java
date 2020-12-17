package com.kmt.pro;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.web.WebJump;

/**
 * Create by JFZ
 * date: 2020-07-07 17:48
 **/
public class TstActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.zz_activity_tst;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
    public void ddd(View view) {
        WebJump.toWebNoShare(this, "https://www.bjjnts.cn/");
    }


    public void click(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                //有悬浮窗权限开启服务绑定 绑定权限
                Tools.showToast("=====1======");
            } else {
                //没有悬浮窗权限m,去开启悬浮窗权限
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 默认有悬浮窗权限  但是 华为, 小米,oppo 等手机会有自己的一套 Android6.0 以下
            // 会有自己的一套悬浮窗权限管理 也需要做适配
            Tools.showToast("=====2======");
        }
    }

    public void detail(View view) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 201);
    }
}
