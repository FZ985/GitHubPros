package com.wzcuspro.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseActivity extends BaseSwipActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int resId = layoutResId();
            if (resId != 0) {
                setContentView(resId);
            } else {
                setContentView(createView());
            }
            initView();
            initData();
            styleBar(this);
        } catch (Exception e) {
            System.out.print("捕获初始化异常===>>>:" + e.getMessage());
        }
    }
}
