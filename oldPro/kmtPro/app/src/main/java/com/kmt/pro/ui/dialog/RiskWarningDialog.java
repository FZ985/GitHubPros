package com.kmt.pro.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmtlibs.app.base.BaseDialog;
import com.kmtlibs.app.utils.Utils;

import androidx.annotation.NonNull;

/**
 * Create by JFZ
 * date: 2020-04-24 12:03
 **/
public class RiskWarningDialog extends BaseDialog {
    private TextView warning_cancel;
    private TextView warning_ok;
    private DialogInterface.OnDismissListener listener;

    public RiskWarningDialog(@NonNull Context context, DialogInterface.OnDismissListener listener) {
        super(context);
        this.listener = listener;
        width((int) (Utils.getScreenWidth(context) * 0.82f));
        setCanceledOnTouchOutside(false);
        initData();
    }

    @Override
    public int resLayoutId() {
        return R.layout.dialog_riskwarning;
    }

    @Override
    public void initView() {
        warning_cancel = findViewById(R.id.warning_cancel);
        warning_ok = findViewById(R.id.warning_ok);
    }

    @Override
    public void initData() {
        warning_ok.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDismiss(RiskWarningDialog.this);
            }
        });
        warning_cancel.setOnClickListener(v -> dismiss());
    }
}
