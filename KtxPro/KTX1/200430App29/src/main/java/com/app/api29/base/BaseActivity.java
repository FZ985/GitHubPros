package com.app.api29.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import api29.libs.app.base.SuperInitActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends SuperInitActivity implements BaseView {

    protected P mPresenter;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
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
            unbinder = ButterKnife.bind(this);
            initView();
            styleBar(this);
            initData();
        } catch (Exception e) {
            System.out.print("捕获初始化异常===>>>:" + e.getMessage());
        }
    }

    public boolean isPortrait() {
        return false;
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
    }


}
