package com.kmt.pro.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.kmt.pro.R;


/**
 * Created by JFZ
 * on 2020/2/19.
 */

public class MaxHeightScrollView extends ScrollView {

    private int maxHeight;

    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
        maxHeight = typedArray.getDimensionPixelSize(R.styleable.MaxHeightScrollView_mhsv_max_height,
                200);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST));
    }
}
