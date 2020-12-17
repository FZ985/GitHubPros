package com.kmt.pro.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.AppVersionBean;
import com.kmt.pro.utils.FileUtil;
import com.kmt.pro.utils.PermissionCheck;
import com.kmt.pro.widget.NumberProgressBar;
import com.kmtlibs.app.ActivityManager;
import com.kmtlibs.app.base.BaseDialog;
import com.kmtlibs.app.base.DialogDismissCallback;
import com.kmtlibs.app.utils.DateTime;
import com.kmtlibs.app.utils.InstallUtil;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.okhttp.HttpImpl;
import com.kmtlibs.okhttp.file.download.DownLoadListenerAdapter;
import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * Created by JFZ
 * on 2020/3/3.
 */

public class AppUpdateDialog extends BaseDialog {
    private AppVersionBean version;
    private TextView update_versionname;
    private TextView update_content;
    private NumberProgressBar update_progress;
    private Button update_next;
    private Button update_downloadapp;
    private DialogDismissCallback.BaseDialogCall callback;
    private File file;

    public AppUpdateDialog(@NonNull Context context, AppVersionBean version, DialogDismissCallback.BaseDialogCall listener) {
        super(context);
        gravity(Gravity.CENTER);
        this.version = version;
        this.callback = listener;
        width(WindowManager.LayoutParams.WRAP_CONTENT);
        initData();
    }

    @Override
    public int resLayoutId() {
        return R.layout.dialog_app_update;
    }

    @Override
    public void initView() {
        update_versionname = findViewById(R.id.update_versionname);
        update_content = findViewById(R.id.update_content);
        update_progress = findViewById(R.id.update_progress);
        update_next = findViewById(R.id.update_next);
        update_downloadapp = findViewById(R.id.update_downloadapp);
    }

    @Override
    public void initData() {
        update_versionname.setText("新版本升级V" + version.getVersion());
        update_content.setText(version.getUpgradeDetails() + "");
        if ("1".equals(version.getStratery())) {
            //软更
            update_next.setText("稍后再说");
            update_next.setOnClickListener(view -> {
                if (callback != null) {
                    callback.dismissCall(AppUpdateDialog.this, 0);
                }
            });
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        } else if ("2".equals(version.getStratery())) {
            //强更
            setCanceledOnTouchOutside(false);
            setCancelable(false);
            update_next.setText("退出应用");
            update_next.setOnClickListener(view -> {
                dismiss();
                ActivityManager.getAppInstance().AppExit(BaseApp.getInstance());
            });
        }

        update_downloadapp.setOnClickListener(view -> {
            if (file != null && file.exists() && update_downloadapp.getText().toString().equals("点击安装")) {
                //已完成下载，去安装
                InstallUtil.installApk(file, BaseApp.getInstance());
            } else {
                if (PermissionCheck.checkReadWritePermission(getContext())) {
                    //没下载，去下载
                    download();
                } else {
                    PermissionCheck.reqPermission((Activity) getContext(), PermissionCheck.PERMISSIONS_READ_AND_WRITE, 200);
                }
            }
        });
    }

    private void download() {
        update_progress.setVisibility(View.VISIBLE);
        String path = FileUtil.createDownloadAppFile();
        String name = BaseApp.getInstance().getResources().getString(R.string.app_name) + DateTime.format("yyyyMMddHHmmss") + ".apk";

        HttpImpl.download3(version.getRedirectlocation(), path, name, new DownLoadListenerAdapter() {
            @Override
            public void update(long progress, float percents, long contentLength, boolean done) {
                final int percent = (int) (progress * 100f / contentLength);
                Logger.e("下载apk_progress:" + progress + "," + percent + "," + contentLength);
                RetrofitManager.getInstance().obtainHandler().post(() -> {
                    if (isShowing()) {
                        update_downloadapp.setEnabled(false);
                        update_downloadapp.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        update_downloadapp.setText("正在下载");
                        update_progress.setProgress(percent);
                        update_progress.setMax(100);
                    }
                });
            }

            @Override
            public void complete(File file) {
                Logger.e("下载apk成功:" + file.getPath());
                AppUpdateDialog.this.file = file;
                RetrofitManager.getInstance().obtainHandler().post(() -> {
                    if (isShowing()) {
                        update_downloadapp.setEnabled(true);
                        update_downloadapp.setBackgroundColor(Color.parseColor("#FFCC08"));
                        update_downloadapp.setText("点击安装");
                        update_progress.setMax(100);
                        update_progress.setProgress(100);
                    }
                    InstallUtil.installApk(file, BaseApp.getInstance());
                });
            }

            @Override
            public void error(Exception e) {
                Logger.e("下载apk失败:" + e.getMessage());
                RetrofitManager.getInstance().obtainHandler().post(() -> {
                    if (isShowing()) {
                        update_downloadapp.setEnabled(true);
                        update_downloadapp.setBackgroundColor(Color.parseColor("#FFCC08"));
                        update_downloadapp.setText("重新下载");
                    }
                    try {
                        if (file != null && file.exists()) file.delete();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
            }
        });
    }
}
