package com.kmt.pro.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.kmtlibs.app.base.SuperBaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import androidx.annotation.Nullable;
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
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException e) {
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
        try {
            EventBus.getDefault().unregister(this);
        } catch (EventBusException e) {
        }
    }
    private InputMethodManager inputMethodManager;
    public void hideSoftKeyBoard() {
        View localView = getActivity().getCurrentFocus();
        if (this.inputMethodManager == null) {
            this.inputMethodManager = ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null)) {
            this.inputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }
}
