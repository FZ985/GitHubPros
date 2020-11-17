package com.douyinvideo.tiktok;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.douyinvideo.R;
import com.douyinvideo.Urls;
import com.douyinvideo.tiktok.view.JzvdStdTikTok;

import java.util.Arrays;

import cn.jzvd.Jzvd;

public class TikTokActivity extends AppCompatActivity {

    private RecyclerView recycle;
    private ViewPagerLayoutManager manager;private int mCurrentPosition = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiktok);
        recycle = findViewById(R.id.recycle);
        manager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        recycle.setLayoutManager(manager);
        recycle.setAdapter(new TiktokAdapter(Arrays.asList(Urls.urls)));
        initVideo();
    }

    private void initVideo() {
        manager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                //自动播放第一条
                autoPlayVideo(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (mCurrentPosition == position) {
                    Jzvd.releaseAllVideos();
                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                if (mCurrentPosition == position) {
                    return;
                }
                autoPlayVideo(position);
                mCurrentPosition = position;
            }
        });

        recycle.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null && jzvd.jzDataSource != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });
    }
    private void autoPlayVideo(int postion) {
        if (recycle == null || recycle.getChildAt(0) == null) {
            return;
        }
        JzvdStdTikTok player = recycle.getChildAt(0).findViewById(R.id.videoplayer);
        if (player != null) {
            player.startVideoAfterPreloading();
        }
    }
    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
