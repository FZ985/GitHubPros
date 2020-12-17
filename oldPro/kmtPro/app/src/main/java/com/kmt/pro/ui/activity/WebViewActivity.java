package com.kmt.pro.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.web.HtmlCall;
import com.kmt.pro.web.WebHelp;
import com.kmt.pro.widget.BasicWebView;
import com.kmtlibs.app.utils.Logger;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-07-07 10:57
 **/
public class WebViewActivity extends BaseToolBarActivity implements HtmlCall {

    @BindView(R.id.web)
    BasicWebView web;

    private String loadUrl;
    private String web_title, web_shareTitle, web_shareText;
    private boolean web_isAppendparams, web_isShowShareButton;
    private int shareLogo;

    @Override
    public int getLayoutId() {
        loadUrl = getIntent().getStringExtra(KConstant.Key.web_url);
        web_title = getIntent().getStringExtra(KConstant.Key.web_title);
        web_shareTitle = getIntent().getStringExtra(KConstant.Key.web_shareText);
        web_shareText = getIntent().getStringExtra(KConstant.Key.web_shareText);
        shareLogo = getIntent().getIntExtra(KConstant.Key.web_shareLogo, R.mipmap.applogo);
        web_isAppendparams = getIntent().getBooleanExtra(KConstant.Key.web_isAppendParams, false);
        web_isShowShareButton = getIntent().getBooleanExtra(KConstant.Key.web_isAppendParams, false);
        return R.layout.layout_webview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        if (!TextUtils.isEmpty(web_title)) {
            mToolbar.title.setText(web_title);
        }
        if (!web_isShowShareButton) {//取消分享按钮
        }
        mToolbar.left.setOnClickListener(v -> onBackPressed());
        web.getSettings().setJavaScriptEnabled(true);
//        web.addJavascriptInterface(helper, "mobile");
        web.setWebChromeClient(new BasicWebView.WebChromeClient(web.getProgressBar()) {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
            }
        });
        web.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                try {
                    if (url.startsWith("scheme:") || url.startsWith("intent:")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                } catch (Exception e) {

                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }
        });
        web.setWebChromeClient(new BasicWebView.WebChromeClient(web.getProgressBar()) {
            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                if (TextUtils.isEmpty(web_title) && !TextUtils.isEmpty(title)) {
                    mToolbar.title.setText(title);
                }
            }
        });
    }

    @Override
    public void initData() {
        loadUrl();
    }

    private void loadUrl() {
        if (web_isAppendparams) {
            loadUrl = WebHelp.appendParams(loadUrl);
        }
        Logger.e("加载url:" + loadUrl);
        web.loadUrl(loadUrl);
    }

    @Override
    public boolean canGoBack() {
        return (web != null) && web.canGoBack();
    }

    @Override
    public void goBack() {
        if (web != null) web.goBack();
    }

    @Override
    public void finishThis() {
        finish();
    }

    @Override
    public void hideShareButton() {
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void load(String method) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = method;
        mHandler.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 0://关闭分享按钮
                mToolbar.right.setVisibility(View.GONE);
                break;
            case 1://加载函数
                if (web != null) {
                    String methods = (String) msg.obj;
                    Logger.e("----js--Methods:" + methods);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        web.evaluateJavascript(methods, null);
                    } else {
                        web.loadUrl(methods);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (web != null) {
            if (web.canGoBack()) {
                web.goBack();
            } else
                finish();
        } else finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            web.onResume();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            web.onPause();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (web != null) {
                deleteDatabase("webview.db");
                deleteDatabase("webviewCache.db");
                web.clearHistory();
                web.freeMemory();
                web.setVisibility(View.GONE);
                ViewGroup parent = (ViewGroup) web.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(web);
                }
                web.removeAllViews();
                web.destroy();
            }
            web = null;
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}
