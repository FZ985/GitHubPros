package com.kmt.pro.ui.activity;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.immersionbar.ImmersionBar;

import jiang.photo.picker.widget.PickerTouchImageView;

/**
 * Create by JFZ
 * date: 2020-08-21 11:33
 **/
public class BigPicturePreviewActivity extends BaseActivity {
    private String path;
    private PickerTouchImageView image;

    @Override
    public int getLayoutId() {
        path = getIntent().getStringExtra(KConstant.Key.picturePath);
        return R.layout.activity_bigpicture_preview;
    }

    @Override
    public void initView() {
        image = findViewById(R.id.image);
    }

    @Override
    public void initData() {
        Glide.with(this)
                .load(KConstant.img_url + path)
                .error(R.drawable.photo_default)
                .into(image);
        image.setOnLongClickListener(v -> false);
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.init();
    }
}
