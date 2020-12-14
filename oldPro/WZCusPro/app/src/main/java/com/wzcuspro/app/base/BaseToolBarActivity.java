package com.wzcuspro.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.wzcuspro.R;
import com.wzcuspro.app.widget.CustomToolbar;


public abstract class BaseToolBarActivity extends BaseSwipActivity {

    private View mContentView;
    protected CustomToolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.base_toolbar_activity);
            mToolbar = findViewById(R.id.base_toolbar);
            int resId = layoutResId();
            if (resId != 0) {
                mContentView = View.inflate(this, resId, null);
            } else mContentView = createView();
            if (mContentView != null) {
                ((ViewGroup) findViewById(R.id.base_content))
                        .addView(mContentView, new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // 干掉默认标题
            initView();
            initData();
            styleBar(this);
        } catch (Exception e) {
            System.out.print("onCreate初始化异常捕获>>>>:" + e.getMessage());
        }
    }
}
