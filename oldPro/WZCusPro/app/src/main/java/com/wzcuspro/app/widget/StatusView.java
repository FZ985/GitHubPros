package com.wzcuspro.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.wzcuspro.app.utils.Tools;


/**
 * Created by JFZ
 * on 2018/5/19.
 */

public class StatusView extends AppCompatTextView {
    public StatusView(Context context) {
        super(context);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, Tools.getStatusHeight(getContext()));
    }
}
