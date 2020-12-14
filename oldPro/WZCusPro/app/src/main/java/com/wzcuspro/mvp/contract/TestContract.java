package com.wzcuspro.mvp.contract;

import com.wzcuspro.mvp.BasePresenter;
import com.wzcuspro.mvp.BaseView;

public interface TestContract {

    public interface TestView extends BaseView {
        void ok(String ok);
    }

    public interface TestPresenter extends BasePresenter {
        void req();
    }

}
