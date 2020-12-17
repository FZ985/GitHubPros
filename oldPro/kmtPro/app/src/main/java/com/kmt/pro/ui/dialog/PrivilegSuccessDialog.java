package com.kmt.pro.ui.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmtlibs.app.base.BaseDialog;
import com.kmtlibs.app.utils.Utils;

import androidx.annotation.NonNull;

/**
 * Create by JFZ
 * date: 2020-03-30 19:20
 **/
public class PrivilegSuccessDialog extends BaseDialog {
    private String msg;
    private TextView privilege_succ_tv;
    private ImageView privilege_succ_closeiv;
    private ImageView success_image;
    private int ResId = -1;

    public PrivilegSuccessDialog(@NonNull Context context, String msg) {
        super(context);
        this.msg = msg;
        this.ResId = R.mipmap.icon_success_privilege;
        width((int) (Utils.getScreenWidth(context) * 0.8f));
        initData();
    }

    public PrivilegSuccessDialog(@NonNull Context context, int resId, String msg) {
        super(context);
        this.msg = msg;
        this.ResId = resId;
        width((int) (Utils.getScreenWidth(context) * 0.8f));
        initData();
    }

    @Override
    public int resLayoutId() {
        return R.layout.dialog_privilege_success;
    }

    @Override
    public void initView() {
        privilege_succ_tv = findViewById(R.id.privilege_succ_tv);
        privilege_succ_closeiv = findViewById(R.id.privilege_succ_closeiv);
        success_image = findViewById(R.id.success_image);
    }

    @Override
    public void initData() {
        privilege_succ_tv.setText(this.msg + "");
        privilege_succ_closeiv.setOnClickListener(v -> dismiss());
        if (ResId != -1) {
            success_image.setImageResource(ResId);
        }
    }
}
