package com.kmt.pro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.callback.LoginListener;
import com.kmt.pro.helper.Login;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SplashContract;
import com.kmt.pro.mvp.presenter.SplashPresenterImpl;
import com.kmt.pro.permission.PermissionTool;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.utils.PermissionCheck;
import com.kmt.pro.utils.SystemUtils;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.ActivityManager;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.utils.Utils;
import com.kmtlibs.okhttp.base.OkHttpConfig;
import com.lxbuytimes.kmtapp.MainActivity;
import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * Create by JFZ
 * date: 2020-06-29 17:17
 **/
public class SplashActivity extends FragmentActivity implements SplashContract.SplashView, LoginListener {
    private ImageView splash_iv;
    private TextView splash_time_tv;
    private SplashPresenterImpl presenter;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PermissionTool tool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getAppInstance().addActivity(this);
        setContentView(R.layout.activity_splash);
        splash_iv = findViewById(R.id.splash_iv);
        splash_time_tv = findViewById(R.id.splash_time_tv);
        presenter = new SplashPresenterImpl();
        presenter.attachView(this, this);
        initData();
    }

    private void initData() {
        if (Utils.isConnection(this)) {
            init();
        } else {
            //注册网络观察者广播
            regNetWork();
            Tools.showToastCenter("网络连接失败,请检查网络!", Gravity.CENTER);
        }
    }

    private void init() {
        presenter.queryBackground();
        requestPermission();
    }

    @Override
    public void background(String image) {
        if (!TextUtils.isEmpty(image)) {
            Glide.with(this)
                    .load(image)
                    .into(splash_iv);
        }
    }

    private void start() {
        UserSp.get().setUuid(SystemUtils.getUUID(BaseApp.getInstance()));
        if (!TextUtils.isEmpty(UserSp.get().getMobile())) {
            //判断密码
            if (!TextUtils.isEmpty(UserSp.get().getPassword())) {
                //用户登录
                presenter.userLogin(SplashActivity.this);
            } else if (!TextUtils.isEmpty(UserSp.get().getCodePassword())) {
                //应用登录
                presenter.applicationLogin(SplashActivity.this);
            } else timeDown();
        } else {
            //未登录
            timeDown();
        }
    }

    private void requestPermission() {
        if (PermissionCheck.checkSplashPermission(BaseApp.getInstance())) {
            start();
        } else {
            tool = PermissionTool.get();
            tool.requestPermission(this, PERMISSIONS_STORAGE, 200, new PermissionTool.PermissionCall() {
                @Override
                public void onPermissionOk() {
                    start();
                }

                @Override
                public void onPermissionNo(List<String> faildPermissionList, List<String> donotAsk) {
                    OkHttpConfig.getInstance().obtainHandler().postDelayed(() -> {
                        if (!isFinishing()) {
                            if (donotAsk.size() > 0) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                                dialog.setTitle("提示");
                                dialog.setMessage("您拒绝了某些权限的使用,部分功能将无法体验,是否需要再次授权该权限？");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("去设置", (dialog1, which) -> {
                                    if (dialog1 != null) dialog1.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 201);
                                });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            } else requestPermission();
                        }
                    }, 300);
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                    if (requestCode == 201) {
                        Logger.e("permission", "设置界面返回结果");
                        requestPermission();
                    }
                }
            });
        }
    }

    private class MRunnable implements Runnable {
        private int count;

        MRunnable(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            count--;
            runOnUiThread(() -> {
                splash_time_tv.setText(String.valueOf(count));
                if (count == 0) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    RetrofitManager.getInstance().obtainHandler().postDelayed(MRunnable.this, 1000);
                }
            });
        }
    }

    //倒计时
    private void timeDown() {
        splash_time_tv.setVisibility(View.VISIBLE);
        int count = 4;
        splash_time_tv.setText(String.valueOf(count));
        RetrofitManager.getInstance().obtainHandler().postDelayed(new MRunnable(count), 1000);
        splash_time_tv.setOnClickListener(v -> {
            RetrofitManager.getInstance().obtainHandler().removeCallbacksAndMessages(null);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void loginSuccess(LoginBean data) {
        if (data != null) {
            Login.get().setLogin(true);
            Login.get().setData(data);
            UserSp.get()
                    .setUserId(data.userId)
                    .setHeadImg(data.userAvatar)
                    .setNickName(data.userName)
                    .setHxId(data.userEasemobId)
                    .setBrokerNo(data.brokerNo);
            timeDown();
        } else {
            loginErr("");
        }
    }

    @Override
    public void loginOtherStatus(CommonResp<LoginBean> data) {

    }

    @Override
    public void loginErr(String err) {
        Login.get().setLogin(false);
        Login.get().setData(null);
        timeDown();
    }

    private BroadcastReceiver receiver;

    private void regNetWork() {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        String action = intent.getAction();
                        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                            if (Utils.isConnection(BaseApp.getInstance())) {
                                Logger.e("app_广播_网络连接正常");
                                init();
                            }
                        }
                    }
                }
            };
        }
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, mFilter); // 网络监听必须调用系统的注册
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tool != null) {
            tool.destry();
        }
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (Exception e) {
                Logger.log("SplashActivity", "######退出程序异常#####" + e.getMessage());
            }
        }
        presenter.dettachView();
        ActivityManager.getAppInstance().removeActivity(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionTool.get().onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionTool.get().onActivityResult(requestCode, resultCode, data);
    }
}
