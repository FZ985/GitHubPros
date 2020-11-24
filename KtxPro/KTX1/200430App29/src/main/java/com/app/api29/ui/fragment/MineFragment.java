package com.app.api29.ui.fragment;

import com.app.api29.R;
import com.app.api29.base.BaseFragment;

import api29.libs.app.utils.Logger;

/**
 * Create by JFZ
 * date: 2020-05-06 11:54
 **/
public class MineFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        Logger.e("=====mine====initView");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("=====mine====");
    }

    @Override
    public void initData() {

    }
}
