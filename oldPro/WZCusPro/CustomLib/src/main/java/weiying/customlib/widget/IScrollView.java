package weiying.customlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


/**
 * Created by JFZ on 2017/9/28 12:19.
 *
 */

public class IScrollView extends ScrollView {

    private IScrollViewListener listener;

    public IScrollView(Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(false);
    }

    public IScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(false);
    }

    public IScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener != null){
            listener.onScrollChanged(l,t,oldl,oldt);
        }
    }

    public void addScrollChangedListener(IScrollViewListener listener){
        this.listener = listener;
    }

    public interface IScrollViewListener {

        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
