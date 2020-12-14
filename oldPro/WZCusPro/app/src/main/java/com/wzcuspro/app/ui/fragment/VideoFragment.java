package com.wzcuspro.app.ui.fragment;

import android.annotation.SuppressLint;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.wzcuspro.R;
import com.wzcuspro.app.adapter.MainTabsAdapter;
import com.wzcuspro.app.base.BaseFragment;
import com.wzcuspro.app.glide.GlideApp;
import com.wzcuspro.app.utils.Logger;
import com.wzcuspro.app.utils.Tools;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import weiying.customlib.app.receive.SendRecvHelper;

@SuppressLint("NewApi")
public class VideoFragment extends BaseFragment {
    private AppBarLayout appBarlayout;
    private ViewPager vp;
    private ImageView img;
    private TextView tabLayout;
    private int height;

    @Override
    public void initView() {
        appBarlayout = findViewById(R.id.appBarlayout);
        vp = findViewById(R.id.vp);
        img = findViewById(R.id.img);
        tabLayout = findViewById(R.id.tabLayout);
        GlideApp.with(this).load("http://pic1.win4000.com/wallpaper/c/545088304042f.jpg").centerCrop().transition(DrawableTransitionOptions.withCrossFade()).into(img);
    }

    @Override
    public void initData() {
        height = Tools.dip2px(mContext, 220);

        List<BaseFragment> list = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new HomeFragment());
        list.add(new HomeFragment());
        list.add(new HomeFragment());
//        scrollview.setFillViewport(true);
        vp.setAdapter(new MainTabsAdapter(getChildFragmentManager(), list));

    }

    @Override
    public int layoutResId() {
        return R.layout.fragment_video;
    }
}
