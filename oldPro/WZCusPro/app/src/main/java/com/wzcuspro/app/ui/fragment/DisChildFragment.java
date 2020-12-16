package com.wzcuspro.app.ui.fragment;

import android.view.View;
import android.widget.ScrollView;

import com.wzcuspro.R;
import com.wzcuspro.app.base.BaseFragment;

import weiying.customlib.refresh.pullui.scroll.ScrollableHelper;

public class DisChildFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {
    private ScrollView scrollview;

    @Override
    public void initView() {
        scrollview = findViewById(R.id.scrollview);
    }

    @Override
    public void initData() {

    }

    @Override
    public int layoutResId() {
        return R.layout.fragment_dischild;
    }

    @Override
    public View getScrollableView() {
        return scrollview;
    }
}
