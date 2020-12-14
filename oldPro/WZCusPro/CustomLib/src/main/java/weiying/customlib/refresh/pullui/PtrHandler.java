package weiying.customlib.refresh.pullui;

import android.view.View;


/**
 * Created by JFZ
 * on 2017/10/16 10:13.
 */

public interface PtrHandler {
    /**
     * Check can do refresh or not. For example the content is empty or the first child is in view.
     * <p/>
     * { in.srain.cube.views.ptr.PtrDefaultHandler#checkContentCanBePulledDown}
     */
    public boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header);

    /**
     * When refresh begin
     *
     * @param frame
     */
    public void onRefreshBegin(final PtrFrameLayout frame);
}
