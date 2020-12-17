package com.kmt.pro.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.kmt.pro.R;
import com.tencent.smtt.sdk.WebView;

/**
 * Create by JFZ
 * date: 2020-07-07 11:02
 **/
public class BasicWebView extends com.tencent.smtt.sdk.WebView {
    private ProgressBar progressBar;


    public BasicWebView(Context context) {
        super(context);
        init(context);
    }

    public BasicWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public BasicWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (progressBar != null) {
            removeView(progressBar);
        }
        progressBar = null;
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 5));
        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_webview);
        progressBar.setProgressDrawable(drawable);
        addView(progressBar);

        getSettings().setJavaScriptEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setBlockNetworkImage(false);//解决图片不显示
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setTextZoom(100);
        getSettings().setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON);//视频播放相关配置

        getSettings().setDefaultTextEncodingName("utf-8");
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAllowContentAccess(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setRenderPriority(com.tencent.smtt.sdk.WebSettings.RenderPriority.HIGH);
        getSettings().setLoadsImagesAutomatically(true);

        getSettings().setTextSize(com.tencent.smtt.sdk.WebSettings.TextSize.NORMAL);
        getSettings().setTextZoom(100);
        clearCache(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            getSettings().setDisplayZoomControls(false);
        }
        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public static class WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {
        private ProgressBar progressBar;

        public WebChromeClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        public WebChromeClient() {
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar != null) {
                if (newProgress == 100) {
                    progressBar.setVisibility(GONE);
                } else {
                    if (progressBar.getVisibility() == GONE)
                        progressBar.setVisibility(VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

    }
}
