package com.app.api29.base;

import android.app.Activity;

/**
 * Create by JFZ
 * date: 2019-06-06 17:45
 **/
public interface IPresenter<V extends BaseView> {

    void attachView(V view, Activity activity);

    void dettachView();

}
