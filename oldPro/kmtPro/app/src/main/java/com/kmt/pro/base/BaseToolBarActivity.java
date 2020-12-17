package com.kmt.pro.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kmt.pro.R;
import com.kmt.pro.normal.widget.CustomToolbar;
import com.kmt.pro.utils.Check;
import com.kmtlibs.immersionbar.ImmersionBar;
import com.kmtlibs.immersionbar.StatusView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseToolBarActivity<P extends BasePresenter> extends BaseSwipActivity implements BaseView {

    private View mContentView;
    protected CustomToolbar mToolbar;
    protected LinearLayout statusRoot;
    protected StatusView statusbar;
    protected ImmersionBar immersionBar;
    private Unbinder unbinder;
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((Build.VERSION.SDK_INT != Build.VERSION_CODES.O) && isPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.base_toolbar_activity);
        mToolbar = (CustomToolbar) findViewById(R.id.base_toolbar);
        statusRoot = (LinearLayout) findViewById(R.id.toolbar_root);
        statusbar = (StatusView) findViewById(R.id.toolbar_statusview);
        int resId = getLayoutId();
        if (resId != 0) {
            mContentView = View.inflate(this, resId, null);
        } else mContentView = createView();
        if (mContentView != null) {
            ((ViewGroup) findViewById(R.id.base_content))
                    .addView(mContentView, new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }
        unbinder = ButterKnife.bind(this, mContentView);
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException e) {
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 干掉默认标题
        immersionBar = ImmersionBar.with(this);
        toolbarColor(ContextCompat.getColor(this, R.color.white));
        presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this, this);
        }
        mToolbar.showBlackBack(v -> finish());
        initView();
        styleBar(this);
        initData();
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
    }

    public void toolbarColor(int color) {
        if (ImmersionBar.isSupportStatusBarDarkFont())
            immersionBar.statusBarDarkFont(Check.isWhiteColor(color));
        mToolbar.bottomLine(Check.isWhiteColor(color));
        mToolbar.allTextColor(Check.isWhiteColor(color) ? ContextCompat.getColor(mContext, R.color.black20) : Color.WHITE);
        immersionBar.statusBarView(statusbar).fitsSystemWindows(false).fullScreen(false).init();
        try {
            statusRoot.setBackground(ContextCompat.getDrawable(mContext, color));
        } catch (Exception e) {
            statusRoot.setBackgroundColor(color);
        }
    }

    @Override
    public void finish() {
        hideSoftKeyBoard();
        super.finish();
    }

    protected P getPresenter() {
        return null;
    }

    public boolean isPortrait() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dettachView();
        }
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
        if (unbinder != null) unbinder.unbind();
        immersionBar = null;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void XXX(MessageEvent messageEvent) {
//    ...
//    }
}
