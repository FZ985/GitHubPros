package com.jiaozi.demo.tiktok;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.jiaozi.demo.R;
import com.jiaozi.demo.Urls;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

import static cn.jzvd.Jzvd.backPress;

public class TiktokActivity extends AppCompatActivity {
    private ViewPager2 vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiktok);
        getSupportActionBar().hide();
        vp = findViewById(R.id.vp);
        if (vp.getChildAt(0) != null && vp.getChildAt(0) instanceof RecyclerView) {
            vp.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        List<Fragment> frags = new ArrayList<>();
        for (int i = 0; i < Urls.urls.length; i++) {
            frags.add(TikTokFragment.instance(Urls.urls[i], i));
        }
        FragmentVpAdapter adapter = new FragmentVpAdapter(this, frags);
        vp.setOffscreenPageLimit(Urls.urls.length);
        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("page_onPageScrolled:" + position);
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("page_onPageSelected:" + position);
                Fragment fragment = adapter.createFragment(position);
                if (fragment != null && fragment instanceof TikTokFragment) {
                    ((TikTokFragment) fragment).play();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("page_onPageScrollStateChanged:" + state);
            }
        });
        vp.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Jzvd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    public void onBackPressed() {
        if (backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
    }
}
