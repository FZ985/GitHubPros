package com.wzcuspro.mvp.presenter;


import com.wzcuspro.mvp.contract.TestContract;
import com.wzcuspro.mvp.model.TestModel;

public class TestPresenterImpl implements TestContract.TestPresenter {
    private TestContract.TestView view;
    private TestModel model;

    public TestPresenterImpl(TestContract.TestView view) {
        this.view = view;
        model = new TestModel();
    }

    @Override
    public void req() {
        model.req();
    }
}
