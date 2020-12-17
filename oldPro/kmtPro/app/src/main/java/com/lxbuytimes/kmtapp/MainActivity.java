package com.lxbuytimes.kmtapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.widget.RelativeLayout;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.callback.MainFragmentPageCall;
import com.kmt.pro.helper.Actions;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Login;
import com.kmt.pro.helper.MainContentHelper;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.widget.bottomtab.LottieBottomInstance;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.immersionbar.ImmersionBar;
import com.kmtlibs.okhttp.base.OkHttpConfig;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements LottieBottomInstance.LottieBottomCall, MainFragmentPageCall {
    @BindView(R.id.main_bottombar_rl)
    RelativeLayout mainBottombarRl;
    private long exitTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        regBroadcastRecv(Actions.ACT_BOTTOM_SWITCH);
        LottieBottomInstance.getInstance().init(mainBottombarRl).setCall(this);
        mHandler.sendEmptyMessageDelayed(0, 200);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == 0) {
            LottieBottomInstance.getInstance().switchIndex(0);
        }
        return true;
    }

    @Override
    public void initData() {
        onTabClick(0, LottieBottomInstance.getInstance().getOldIndex());
    }

    @Override
    public void onFragmentPage(int page, int type) {
        MainContentHelper.get().onFragmentPage(this, page, type);
    }

    @Override
    public void onTabClick(int index, int oldIndex) {
        OkHttpConfig.getInstance().obtainHandler().postDelayed(() -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MainContentHelper.get().switchTab(index, transaction, MainActivity.this);
        }, MainContentHelper.get().checkFragmentIsInstance(index) ? 350 : 0);
    }

    @Override
    public void onSafeReceive(Intent intent, String action) {
        super.onSafeReceive(intent, action);
        if (action.equals(Actions.ACT_BOTTOM_SWITCH)) {
            int index = intent.getIntExtra(KConstant.Key.index, 0);
            Logger.e("切换导航:" + index);
            LottieBottomInstance.getInstance().switchIndex(index);
        }
    }

    @Override
    public void styleBar(Activity activity) {
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true).init();
    }

    @Override
    public void onTabReleaseClick(int index) {

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            Login.get().exit();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        MainContentHelper.get().destry(getSupportFragmentManager().beginTransaction());
        super.onDestroy();

    }
}
