package com.kmtlibs.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by JFZ on 2017/8/24 12:03.
 */

public class IGridView extends GridView {
    public IGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IGridView(Context context) {
        super(context);
    }

    public IGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //该自定义控件只是重写了GridView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套ListView也是同样的道理，不再赘述。
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
