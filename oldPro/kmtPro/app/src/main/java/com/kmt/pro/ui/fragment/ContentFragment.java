package com.kmt.pro.ui.fragment;

import com.kmt.pro.base.BaseFragment;
import com.kmt.pro.base.BasePresenter;
import com.kmt.pro.callback.MainFragmentPageCall;

/**
 * Create by JFZ
 * date: 2020-07-01 9:59
 **/
public abstract class ContentFragment<P extends BasePresenter> extends BaseFragment<P> {
    public MainFragmentPageCall call;

    public abstract void refresh();
}
