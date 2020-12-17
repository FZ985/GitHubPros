package com.kmt.pro.normal.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmtlibs.app.utils.Utils;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


/**
 * Created by JFZ
 * on 2018/5/19.
 */
public class CustomToolbar extends Toolbar {
    private View root;
    public TextView title, left, right, close;
    public View line;

    public CustomToolbar(@NonNull Context context) {
        this(context, null);
    }

    public CustomToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        root = View.inflate(getContext(), R.layout.base_custom_toolbar, null);
        title = (TextView) root.findViewById(R.id.toolbar_title);
        close = (TextView) root.findViewById(R.id.toolbar_close);
        left = (TextView) root.findViewById(R.id.toolbar_left);
        right = (TextView) root.findViewById(R.id.toolbar_right);
        line = root.findViewById(R.id.toolbar_line);
        addView(root);
    }

    public void bottomLine(boolean bool) {
        if (line != null) {
            line.setVisibility(bool ? View.VISIBLE : View.GONE);
        }
    }

    // TODO: 设置左边icon 及 带文字
    public void setLeftTextWithDrawable(String text, Drawable drawable) {
        if (left != null) {
            left.setVisibility(VISIBLE);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            left.setCompoundDrawablePadding(Utils.dip2px(getContext(), 5));
            left.setCompoundDrawables(drawable, null, null, null);
            if (TextUtils.isEmpty(text)) {
                left.setText("");
            } else
                left.setText(text);
        }
    }

    // TODO: 设置左边 icon
    public void setLeftDrawable(Drawable drawable) {
        if (left != null) {
            left.setVisibility(VISIBLE);
            left.setText("");
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            left.setCompoundDrawables(drawable, null, null, null);
        }
    }

    // TODO: 设置右边 icon
    public void setRightDrawable(Drawable drawable) {
        if (right != null) {
            right.setVisibility(VISIBLE);
            right.setText("");
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            right.setCompoundDrawables(null, null, drawable, null);
        }
    }

    // TODO: 设置右边icon 及 带文字
    public void setRightTextWithDrawableForLeft(String text, Drawable drawable) {
        if (right != null) {
            right.setVisibility(VISIBLE);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            right.setCompoundDrawables(drawable, null, null, null);
            if (TextUtils.isEmpty(text)) {
                right.setText("");
            } else
                right.setText(text);
        }
    }

    // TODO: 设置右边icon 及 带文字
    public void setRightTextWithDrawableForRight(String text, Drawable drawable) {
        if (right != null) {
            right.setVisibility(VISIBLE);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            right.setCompoundDrawables(null, null, drawable, null);
            if (TextUtils.isEmpty(text)) {
                right.setText("");
            } else
                right.setText(text);
        }
    }

    public void allTextColor(int color) {
        if (title != null) {
            title.setTextColor(color);
        }
        if (close != null) {
            close.setTextColor(color);
        }
        if (left != null) {
            left.setTextColor(color);
        }
        if (right != null) {
            right.setTextColor(color);
        }
    }

    public void hide() {
        if (root != null) {
            root.setVisibility(View.GONE);
        }
        this.setVisibility(View.GONE);
    }

    public void showBack(int res, final View.OnClickListener listener) {
        if (left == null) return;
        Drawable drawable = ContextCompat.getDrawable(getContext(), res);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        left.setCompoundDrawables(drawable, null, null, null);
        if (listener != null) {
            left.setOnClickListener(listener);
        }
    }

    public void showWhiteBack(final View.OnClickListener listener) {
        showBack(R.mipmap.icon_back_white, listener);
    }

    public void showBlackBack(final View.OnClickListener listener) {
        showBack(R.drawable.selector_backclick, listener);
    }

    public void titleNormal() {
        if (title != null) {
            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }
}
