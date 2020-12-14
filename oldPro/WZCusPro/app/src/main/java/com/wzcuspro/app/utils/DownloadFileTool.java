package com.wzcuspro.app.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import okhttp.OkHttpFactory;
import okhttp.file.download.DownLoadBean;
import okhttp.file.download.DownLoadFileUtil;
import okhttp.file.download.DownLoadStatusCallback;
import okhttp.utils.InstallUtil;


public class DownloadFileTool {

    private Context mContext;
    private DownloadManager mDownloadManager;
    private long downloadId;
    private String mFileName;
    private boolean isApk = false;
    private DownLoadBean bean;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Tools.showToast("开始下载");
            }
        }
    };

    public DownloadFileTool(Context context) {
        this.mContext = new WeakReference<Context>(context).get();
    }

    public void downloadApk(String fileName, String path, String downUrl, DownLoadStatusCallback callback) {
        DownLoadBean bean = new DownLoadBean(fileName, path, downUrl, 0, 0);
        OkHttpFactory.download(bean, callback);
    }

    public void downloadApkFile(String fileName, String downUrl) {
        String path = DownLoadFileUtil.defualtDownloadDir(mContext);
        bean = new DownLoadBean(fileName, DownLoadFileUtil.defualtDownloadDir(mContext), downUrl, 0, 0);
        downloadFile(path, downUrl, bean.fileName);
    }

    public void downloadFile(String path, String downUrl, String fileName) {
        if (fileName.contains(".apk")) {
            isApk = true;
        } else isApk = false;

        //判断权限
        int permission = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_DENIED) {
            //浏览器下载
            Log.i("DownloadManager", "下载没有权限");
            try {
                Uri uri = Uri.parse(downUrl + "");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                mContext.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(mContext, "打开浏览器异常...", Toast.LENGTH_SHORT).show();
            }
        } else {
            //正常下载
            final String packageName = "com.android.providers.downloads";
            int state = mContext.getPackageManager().getApplicationEnabledSetting(packageName);
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("温馨提示").setMessage
                        ("系统下载管理器被禁止，需手动打开").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + packageName));
                            mContext.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            mContext.startActivity(intent);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                if (!((Activity) mContext).isFinishing()) {
                    builder.create().show();
                }
            } else {
                //正常下载流程
                Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
                this.mFileName = fileName;
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downUrl));
                request.setAllowedOverRoaming(false);

                //通知栏显示
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(mFileName);
                request.setDescription("正在下载中...");
                request.setVisibleInDownloadsUi(true);
                //设置下载的路径 sd卡的 Environment.DIRECTORY_DOWNLOADS
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                Uri uri = Uri.fromFile(new File(path + fileName + ""));
                request.setDestinationUri(uri);

                if (mContext != null) {
                    //获取DownloadManager
                    mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (mDownloadManager != null)
                        downloadId = mDownloadManager.enqueue(request);

                    mContext.registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(query);

        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    Tools.showToast(mContext, "下载完成");
                    if (isApk) {
                        if (bean != null) {
                            InstallUtil.installApk(new File(bean.getPath()), mContext);
                        }
                    }

                    if (mReceiver != null && mContext != null) {
                        mContext.unregisterReceiver(mReceiver);
                    }
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        cursor.close();
    }
}
