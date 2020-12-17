package com.kmt.pro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.utils.PermissionCheck;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.immersionbar.ImmersionBar;
import com.lxbuytimes.kmtapp.zxing.QRCodeDecoder;
import com.lxbuytimes.kmtapp.zxing.ZXingView;
import com.lxbuytimes.kmtapp.zxing.core.QRCodeView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import jiang.photo.picker.Photo;

/**
 * Create by JFZ
 * date: 2020-04-14 19:58
 **/
public class QrCodeCameraActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.zxingview)
    ZXingView zxingview;
    @BindView(R.id.qrcode_light)
    ImageView qrcode_light;
    @BindView(R.id.qrcamera_finish)
    ImageView qrcameraFinish;
    @BindView(R.id.qrcamera_photo)
    ImageView qrcameraPhoto;

    boolean isOpenLight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode_camera;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        zxingview.setDelegate(this);
        qrcode_light.setImageResource(R.mipmap.icon_light_open);
        qrcode_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenLight) {
                    zxingview.closeFlashlight();
                    isOpenLight = false;
                    qrcode_light.setImageResource(R.mipmap.icon_light_open);
                } else {
                    zxingview.openFlashlight(); // 打开闪光灯
                    isOpenLight = true;
                    qrcode_light.setImageResource(R.mipmap.icon_light_close);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        zxingview.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        zxingview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zxingview.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (!isFinishing()) {
            zxingview.startSpot(); // 开始识别
            result(result);
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Logger.e("打开相机出错");
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.fullScreen(true).transparentStatusBar().init();
    }

    @OnClick({R.id.qrcamera_finish, R.id.qrcamera_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qrcamera_finish:
                finish();
                break;
            case R.id.qrcamera_photo:
                if (PermissionCheck.checkReadWritePermission(this)) {
                    Photo.with()
                            .mode(Photo.SelectMode.SINGLE)
                            .camera(false)
                            .crop(false)
                            .into(this, images -> {
                                String path = images.get(0);
                                Logger.e("回调jfz_图片地址：" + path);
                                readImage(path);
                            });
                } else {
                    PermissionCheck.reqPermission(this, PermissionCheck.PERMISSIONS_READ_AND_WRITE, 201);
                }
                break;
        }
    }

    private void readImage(String path) {
        Glide.with(this)
                .asBitmap()
                .load(new File(path))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            new Thread(() -> {
                                String result = QRCodeDecoder.syncDecodeQRCode(resource);
                                Message message = Message.obtain();
                                message.what = 1;
                                message.obj = result;
                                if (!isFinishing()) {
                                    mHandler.sendMessage(message);
                                }
                            }).start();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Tools.showToast("解析图片二维码失败");
                    }
                });
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            String res = (String) msg.obj;
            if (!TextUtils.isEmpty(res)) {
                result(res);
            } else {
                Tools.showToast("解析图片二维码失败");
            }
        }
        return super.handleMessage(msg);
    }

    private void result(String result) {
        Intent intent = new Intent();
        intent.putExtra("qrcode_url", result);
        setResult(RESULT_OK, intent);
        finish();
    }
}
