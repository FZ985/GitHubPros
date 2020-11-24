package api29.libs.app.base;

import android.view.View;

/**
 * Create by JFZ
 * date: 2020-04-30 16:08
 **/
public interface BaseInitCallback {

    int getLayoutId();

    void initView();

    void initData();

    View createView();
}
