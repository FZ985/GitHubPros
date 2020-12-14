package com.wzcuspro.app.callback;

import android.view.View;

public interface BaseInitCallback extends View.OnClickListener {

    void initView();

    void initData();

    int layoutResId();

    View createView();

}
