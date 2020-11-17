package com.jiaozi.demo.tiktok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.jiaozi.demo.BaseApp;
import com.jiaozi.demo.R;
import com.jiaozi.demo.Tools;
import com.jiaozi.demo.Urls;
import com.jiaozi.demo.ijk.JZMediaIjk;
import com.jiaozi.demo.tiktok.view.JzvdStdTikTok;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;

public class TikTokFragment extends Fragment {
    private View root;

    private View bg;
    private JzvdStdTikTok video;
    private String videoUrl;
    private String playUrl;
    private int index = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        videoUrl = getArguments().getString("url");
        index = getArguments().getInt("index", -1);
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_tiktok_video, null);
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bg = root.findViewById(R.id.bg);
        video = root.findViewById(R.id.jztiktok);
        bg.setBackgroundColor(Tools.randomColor());
        initData();
    }

    private void initData() {
        playUrl = BaseApp.getProxy(BaseApp.getInstance()).getProxyUrl(videoUrl);
        Glide.with(this)
                .load(Urls.image)
                .into(video.posterImageView);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        playUrl = BaseApp.getProxy(BaseApp.getInstance()).getProxyUrl(videoUrl);
    }

    public void play() {
        if (video != null) {
            setSource(playUrl);
            video.startVideoAfterPreloading();
        }
    }
    private void setSource(String url) {
        JZDataSource jzDataSource = new JZDataSource(url, "");
        video.setUp(jzDataSource, Jzvd.SCREEN_NORMAL, JZMediaIjk.class);
    }

    public static TikTokFragment instance(String url, int index) {
        TikTokFragment fragment = new TikTokFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }
}
