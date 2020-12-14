package com.wzcuspro.app.ui.fragment;

import android.animation.ValueAnimator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nineoldandroids.view.ViewHelper;
import com.wzcuspro.R;
import com.wzcuspro.app.adapter.MainTabsAdapter;
import com.wzcuspro.app.base.BaseFragment;
import com.wzcuspro.app.glide.GlideApp;
import com.wzcuspro.app.utils.Logger;
import com.wzcuspro.app.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import weiying.customlib.refresh.pullui.scroll.ScrollableLayout;

public class DisFragment extends BaseFragment {

    private ScrollableLayout scrolllayout;
    private ImageView img;
    private ViewPager vp;
    private RelativeLayout head;
    private TextView tab;

    @Override
    public void initView() {
        tab = findViewById(R.id.tab);
        head = findViewById(R.id.head);
        scrolllayout = findViewById(R.id.scrolllayout);
        img = findViewById(R.id.img);
        vp = findViewById(R.id.vp);
        GlideApp.with(this).load("http://pic1.win4000.com/wallpaper/c/545088304042f.jpg").centerCrop().transition(DrawableTransitionOptions.withCrossFade()).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.showToast("imgClick");
            }
        });
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.showToast("click");
                if (!scrolllayout.isSticked()) return;
                ValueAnimator valueAnim = ValueAnimator.ofInt(scrolllayout.getMaxY(), 0);
                valueAnim.setInterpolator(new LinearInterpolator());
                valueAnim.setDuration(150);
                valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        ViewHelper.setScrollY(scrolllayout, value - 10);
                        if (animation.getCurrentPlayTime() >= animation.getDuration()) {
                            scrolllayout.setStopMove(false);
                        }
                    }
                });
                valueAnim.start();
            }
        });

        List<DisChildFragment> list = new ArrayList<>();
        list.add(new DisChildFragment());
        list.add(new DisChildFragment());
        list.add(new DisChildFragment());
        list.add(new DisChildFragment());
        vp.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        scrolllayout.getHelper().setCurrentScrollableContainer(list.get(0));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrolllayout.getHelper().setCurrentScrollableContainer(list.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        scrolllayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScrollStop(int currentY, int maxY) {
                Logger.e("------onScrollStop-----");
                if (currentY >= (maxY / 4)) {
//                    scrolllayout.scrollBy(0, );
                    scrolllayout.setStopMove(true);
                    ValueAnimator valueAnim = ValueAnimator.ofInt(currentY, maxY);
                    valueAnim.setInterpolator(new LinearInterpolator());
                    valueAnim.setDuration(350);
                    valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int value = (int) animation.getAnimatedValue();
                            ViewHelper.setScrollY(scrolllayout, value);
                        }
                    });
                    valueAnim.start();
                }
            }

            @Override
            public void onScroll(int currentY, int maxY) {
                Logger.e("---setOnScrollListener:" + currentY + ",maxY:" + maxY, ",isSticked:" + scrolllayout.isSticked());
                scrolllayout.setStopMove((currentY == maxY));
                if (scrolllayout.isSticked()) {
                }

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int layoutResId() {
        return R.layout.fragment_dis;
    }
}
