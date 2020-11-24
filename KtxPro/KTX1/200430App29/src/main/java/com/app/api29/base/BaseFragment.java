package com.app.api29.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import api29.libs.app.base.SuperBaseFragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by JFZ
 * on 2018/5/20.
 */

public abstract class BaseFragment<P extends BasePresenter> extends SuperBaseFragment implements BaseView {
    protected P presenter;
    protected boolean isCreateView;
    protected Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getLayoutId();
        if (resId != 0) {
            mRootView = LayoutInflater.from(getActivity()).inflate(resId, container, false);
        } else {
            mRootView = createView();
        }
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this, mAct);
        }
        initView();
        isCreateView = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected P getPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dettachView();
        }
        if (unbinder != null) unbinder.unbind();
        isCreateView = false;
    }
}
