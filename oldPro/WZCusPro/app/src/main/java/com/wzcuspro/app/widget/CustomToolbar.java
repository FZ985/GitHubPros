package com.wzcuspro.app.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzcuspro.R;


/**
 * Created by JFZ
 * on 2018/5/19.
 */
public class CustomToolbar extends android.support.v7.widget.Toolbar {
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

}
