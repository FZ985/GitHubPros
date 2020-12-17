package com.kmt.pro.ui.fragment;

import android.os.Bundle;

import com.kmt.pro.callback.MainFragmentPageCall;

/**
 * Create by JFZ
 * date: 2020-07-01 10:06
 **/
public class WebFragment extends ContentFragment {

    private String url;

    @Override
    public void refresh() {

    }

    @Override
    public int getLayoutId() {
        if (getArguments() != null) {
            url = getArguments().getString("loadUrl");
        }
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    public static WebFragment instance(String url, MainFragmentPageCall call) {
        WebFragment fragment = new WebFragment();
        fragment.call = call;
        Bundle bundle = new Bundle();
        bundle.putString("loadUrl", url);
        fragment.setArguments(bundle);
        return fragment;
    }
}
