package com.kmt.pro.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import com.kmtlibs.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends BaseSwipActivity implements BaseView {

    protected P mPresenter;
    protected Unbinder unbinder;
    protected ImmersionBar immersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((Build.VERSION.SDK_INT != Build.VERSION_CODES.O) && isPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        int resId = getLayoutId();
        if (resId != 0) {
            setContentView(resId);
        } else {
            setContentView(createView());
        }
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this, this);
        }
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException e) {
        }
        unbinder = ButterKnife.bind(this, this);
        initView();
        styleBar(this);
        initData();
    }

    @Override
    public void finish() {
        hideSoftKeyBoard();
        super.finish();
    }

    public boolean isPortrait() {
        return true;
    }

    protected P getPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.dettachView();
        }
        if (unbinder != null) unbinder.unbind();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
        if (immersionBar != null) {
            immersionBar = null;
        }
    }


}
