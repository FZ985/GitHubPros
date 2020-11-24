package api29.libs.adapter.base.module;


import androidx.annotation.Nullable;
import api29.libs.adapter.base.BaseQuickAdapter;
import api29.libs.adapter.base.listener.OnUpFetchListener;
import api29.libs.adapter.base.listener.UpFetchListenerImp;

/**
 * Create by JFZ
 * date: 2020-05-07 12:17
 * 需要【向上加载更多】功能的，[BaseQuickAdapter]继承此接口
 **/
public class BaseUpFetchModule implements UpFetchListenerImp {

    private OnUpFetchListener mUpFetchListener;
    boolean isUpFetchEnable = false;
    boolean isUpFetching = false;
    /**
     * start up fetch position, default is 1.
     */
    int startUpFetchPosition = 1;

    public BaseUpFetchModule(BaseQuickAdapter adapter) {

    }

    public void autoUpFetch(int position) {
        if (!isUpFetchEnable || isUpFetching) {
            return;
        }
        if (position <= startUpFetchPosition) {
            if (mUpFetchListener != null) {
                mUpFetchListener.onUpFetch();
            }
        }
    }

    @Override
    public void setOnUpFetchListener(@Nullable OnUpFetchListener listener) {
        this.mUpFetchListener = listener;
    }
}
