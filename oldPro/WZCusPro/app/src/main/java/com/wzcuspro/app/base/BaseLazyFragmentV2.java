package com.wzcuspro.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by JFZ
 * on 2018/5/20.
 * 懒加载第二版，界面加载一次不在销毁
 */

public abstract class BaseLazyFragmentV2 extends SuperBaseFragment {
    public boolean isVisible;//用户可见
    public boolean isCreateView;//view 渲染完成
    public boolean isNetData;//能否请求网络数据
    private boolean isLoadedData;//是否加载完数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            int resId = layoutResId();
            if (resId != 0) {
                mRootView = LayoutInflater.from(getActivity()).inflate(resId, container, false);
            } else {
                mRootView = createView();
            }
            initView();
            isCreateView = true;
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Logger.e("jfz生命", "setUserVisibleHint_getUserVisibleHint():" + getUserVisibleHint());
        if (getUserVisibleHint()) {
            isVisible = true;
            if (isCreateView && !isNetData && !isLoadedData) {
                isLoadedData = true;
                netData();
            }
            onVisible();
        } else {
            isVisible = false;
            if (isCreateView) isNetData = false;
            else isNetData = true;
            onInvisible();
        }
//        Logger.e("jfz生命", ">>>>>>>setUserVisibleHint+isNetData:" + isNetData);
    }

    public void onVisible() {
    }

    public void onInvisible() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isVisible && isCreateView && isNetData && !isLoadedData) {
            isLoadedData = true;
            netData();
        } else {
            isNetData = false;
        }
    }

    private void netData() {
        isNetData = false;
        initData();
        isLoadedData = true;
    }
}
