package com.kmt.pro.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;

import com.kmt.pro.R;
import com.kmt.pro.adapter.KeFuAdapter;
import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.mvp.CommonPresenter;
import com.kmt.pro.normal.widget.CustomToolbar;
import com.kmtlibs.immersionbar.ImmersionBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-08-05 14:57
 **/
public class KeFuActivity extends BaseActivity<CommonPresenter> {
    @BindView(R.id.toolbar)
    CustomToolbar toolbar;
    @BindView(R.id.kefu_recycle)
    RecyclerView kefuRecycle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_kefu;
    }

    @Override
    public void initView() {
        toolbar.title.setText("联系客服");
        toolbar.title.setTextColor(Color.WHITE);
        toolbar.showWhiteBack(v -> finish());
        kefuRecycle.setLayoutManager(new LinearLayoutManager(this));
        kefuRecycle.setHasFixedSize(false);
    }

    @Override
    public void initData() {
        mPresenter.kefu(data -> {
            if (data != null) {
                if (!TextUtils.isEmpty(data.qq) || !TextUtils.isEmpty(data.wx)) {
                    kefuRecycle.setAdapter(new KeFuAdapter(data.qq, data.wx));
                }
            }
        });
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.init();
    }

    @Override
    protected CommonPresenter getPresenter() {
        return new CommonPresenter();
    }
}
