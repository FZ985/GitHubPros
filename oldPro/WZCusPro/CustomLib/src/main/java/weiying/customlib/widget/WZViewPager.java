package weiying.customlib.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class WZViewPager extends ViewPager {

    public WZViewPager(Context paramContext) {
        super(paramContext);
    }

    public WZViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        try {
            boolean bool = super.onInterceptTouchEvent(paramMotionEvent);
            return bool;
        } catch (Exception localException) {
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        try {
            boolean bool = super.onTouchEvent(paramMotionEvent);
            return bool;
        } catch (Exception localException) {
        }
        return false;
    }
}
