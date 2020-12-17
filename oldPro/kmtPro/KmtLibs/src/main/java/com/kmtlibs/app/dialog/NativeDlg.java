package com.kmtlibs.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmtlibs.R;
import com.kmtlibs.app.utils.Utils;

import androidx.annotation.NonNull;


/**
 * Created by JFZ on 2017/8/15 16:40.
 */

public class NativeDlg extends Dialog {

    public NativeDlg(@NonNull Context context) {
        super(context, R.style.dialog_custom);
        setContentView(layoutID());
        initWindow(context);
        initView();
    }

    private TextView title, msg, ok, cancel;
    private LinearLayout bottomlayout;

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        msg = (TextView) findViewById(R.id.msg);
        ok = (TextView) findViewById(R.id.ok);
        cancel = (TextView) findViewById(R.id.cancel);
        bottomlayout = (LinearLayout) findViewById(R.id.bottomlayout);
    }

    public NativeDlg title(String titleStr) {
        if (titleStr != null) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(titleStr);
        }
        return this;
    }

    public NativeDlg title(String titleStr, int textColor) {
        if (titleStr != null) {
            this.title.setTextColor(textColor);
            title(titleStr);
        }
        return this;
    }

    public NativeDlg titleGravity(int gravity) {
        if (this.title != null) {
            this.title.setGravity(gravity);
        }
        return this;
    }

    public NativeDlg titleTextSize(float size) {
        if (this.title != null) {
            this.title.setTextSize(size);
        }
        return this;
    }

    public NativeDlg msgTextSize(float size) {
        if (this.msg != null) {
            this.msg.setTextSize(size);
        }
        return this;
    }

    public NativeDlg msgGravity(int gravity) {
        if (this.msg != null) {
            this.msg.setGravity(gravity);
        }
        return this;
    }

    public NativeDlg msg(String msgStr) {
        if (msgStr != null) {
            this.msg.setVisibility(View.VISIBLE);
            this.msg.setText(msgStr);
        }
        return this;
    }

    public NativeDlg msg(String msgStr, int textColor) {
        if (msgStr != null) {
            this.msg.setTextColor(textColor);
            msg(msgStr);
        }
        return this;
    }

    public NativeDlg info(String title, String msg, int infoColor) {
        this.title.setTextColor(infoColor);
        this.msg.setTextColor(infoColor);
        info(title, msg);
        return this;
    }

    public NativeDlg info(String title, String msg, int titleColor, int msgColor) {
        this.title.setTextColor(titleColor);
        this.msg.setTextColor(msgColor);
        info(title, msg);
        return this;
    }

    public NativeDlg info(String title, String msg) {
        title(title);
        msg(msg);
        return this;
    }


    public NativeDlg okClickListener(String text, int textColor, final AlertDlgInterface listener) {
        if (text != null) {
            this.bottomlayout.setVisibility(View.VISIBLE);
            this.ok.setTextColor(textColor);
            okClickListener(text, listener);
        }
        return this;
    }

    public NativeDlg okClickListener(String text, final AlertDlgInterface listener) {
        if (text != null) {
            this.bottomlayout.setVisibility(View.VISIBLE);
            this.ok.setVisibility(View.VISIBLE);
            this.ok.setText(text);
            this.ok.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(NativeDlg.this, v);
                } else {
                    NativeDlg.this.dismiss();
                }
            });
        }
        return this;
    }

    public NativeDlg cancelClickListener(String text, final AlertDlgInterface listener) {
        if (text != null) {
            this.bottomlayout.setVisibility(View.VISIBLE);
            this.cancel.setVisibility(View.VISIBLE);
            this.cancel.setText(text);
            this.cancel.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(NativeDlg.this, v);
                } else {
                    NativeDlg.this.dismiss();
                }
            });
        }
        return this;
    }

    public NativeDlg cancelable(boolean flag) {
        setCancelable(flag);
        return this;
    }

    public NativeDlg canceledOnTouchOutside(boolean flag) {
        setCanceledOnTouchOutside(flag);
        return this;
    }

    public NativeDlg cancelClickListener(String text, int textColor, final AlertDlgInterface listener) {
        if (text != null) {
            this.cancel.setTextColor(textColor);
            cancelClickListener(text, listener);
        }
        return this;
    }

    private void initWindow(Context context) {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = Utils.getScreenWidth(context)
                - Utils.getDipValue(context, 60);
        lp.dimAmount = 0.5f;
        window.setAttributes(lp);
    }

    private int layoutID() {
        return R.layout.base_dialog_native;
    }

    public static NativeDlg create(Context context) {
        NativeDlg dialog = new NativeDlg(context);
        return dialog;
    }

    public interface AlertDlgInterface {
        void onClick(NativeDlg dialog, View v);
    }
}
