package com.wzcuspro.app;


import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import com.wzcuspro.R;
import com.wzcuspro.app.adapter.MainTabsAdapter;
import com.wzcuspro.app.base.BaseActivity;
import com.wzcuspro.app.base.BaseFragment;
import com.wzcuspro.app.callback.BottomBarCallback;
import com.wzcuspro.app.ui.fragment.DisFragment;
import com.wzcuspro.app.ui.fragment.HomeFragment;
import com.wzcuspro.app.ui.fragment.MineFragment;
import com.wzcuspro.app.ui.fragment.VideoFragment;
import com.wzcuspro.app.ui.preview.PreviewAdFragment;
import com.wzcuspro.app.utils.Tools;
import com.wzcuspro.app.widget.BottomBar;

import java.util.ArrayList;
import java.util.List;

import weiying.customlib.barlib.ImmersionBar;
import weiying.customlib.widget.WZNoScrollViewPager;
import weiying.customlib.widget.WZViewPager;

public class MainActivity extends BaseActivity implements BottomBarCallback {
    private BottomBar mBottomBar;
    private WZNoScrollViewPager viewPager;

    @Override
    public void initView() {
        mBottomBar = findViewById(R.id.main_bottombar);
        mBottomBar.setCallback(this);
        viewPager = findViewById(R.id.main_viewpager);
        List<BaseFragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new VideoFragment());
        list.add(new DisFragment());
        list.add(new MineFragment());
        viewPager.setAdapter(new MainTabsAdapter(getSupportFragmentManager(), list));
    }

    @Override
    public void initData() {
        mBottomBar.showPoint(0);
        mBottomBar.showMsg(1, "5");
        mBottomBar.showMsg(2, "50");
        mBottomBar.showMsg(3, "500");
    }

    @Override
    public void bottomClick(int index, int oldIndex) {
        viewPager.setCurrentItem(index);
    }

    @Override
    public void bottomReleaseClick(int index, int oldIndex) {
        Tools.showToast("ReleaseClick新：" + index + ",老：" + oldIndex);
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.fitsSystemWindows(false).init();
    }

    @Override
    public int layoutResId() {
        return R.layout.activity_main;
    }
}
