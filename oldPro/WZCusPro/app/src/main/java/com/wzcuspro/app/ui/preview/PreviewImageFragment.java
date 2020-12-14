package com.wzcuspro.app.ui.preview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wzcuspro.R;
import com.wzcuspro.app.base.BaseApp;
import com.wzcuspro.app.base.BaseFragment;
import com.wzcuspro.app.glide.GlideApp;
import com.wzcuspro.app.ui.preview.view.UpDownFingerPanGroup;

public class PreviewImageFragment extends BaseFragment {
    private UpDownFingerPanGroup group;
    private PreviewImageView preview_iv;
    private ProgressBar preview_progress;
    private String imgUrl;

    @Override
    public void initView() {
        group = findViewById(R.id.group);
        group.setEnable(true);
        preview_iv = findViewById(R.id.preview_iv);
        preview_progress = findViewById(R.id.preview_progress);
    }

    @Override
    public void initData() {
        preview_progress.setVisibility(View.VISIBLE);
        if (imgUrl.contains("gif") || imgUrl.contains("GIF")) {
            RequestOptions options = new RequestOptions();
            options.error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            SimpleTarget<GifDrawable> target = new SimpleTarget<GifDrawable>() {
                @Override
                public void onResourceReady(GifDrawable resource, Transition<? super GifDrawable> transition) {
                    resource.start();
                    preview_iv.setImageDrawable(resource);
                    preview_iv.setZoom(1f);
                    preview_progress.setVisibility(View.GONE);
                }

            };
            GlideApp.with(BaseApp.getInstance()).asGif().load(imgUrl).apply(options).into(target);
        } else {
            GlideApp.with(this).asBitmap().load(imgUrl).transition(BitmapTransitionOptions.withCrossFade()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    preview_iv.setImageBitmap(resource);
                    preview_progress.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public int layoutResId() {
        imgUrl = getArguments().getString("imgUrl");
        return R.layout.fragment_preview;
    }

    public static PreviewImageFragment instance(String url) {
        PreviewImageFragment fragment = new PreviewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imgUrl", url);
        fragment.setArguments(bundle);
        return fragment;
    }
}
