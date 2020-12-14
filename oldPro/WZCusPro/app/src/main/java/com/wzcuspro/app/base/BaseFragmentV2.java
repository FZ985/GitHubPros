package com.wzcuspro.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by JFZ
 * on 2018/5/20.
 * 懒加载---界面显示去加载数据，界面不显示销毁
 */

public abstract class BaseFragmentV2 extends SuperBaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            int resId = layoutResId();
            if (resId != 0) {
                mRootView = LayoutInflater.from(getActivity()).inflate(resId, container, false);
            } else {
                mRootView = createView();
            }
            initView();
            initData();
        }
        return mRootView;
    }

}
