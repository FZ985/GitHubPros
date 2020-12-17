package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.SplashBean;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SplashContract;
import com.kmt.pro.mvp.model.SplashModel;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-06-29 17:39
 **/
public class SplashPresenterImpl extends SplashContract.SplashPresenter {
    private SplashModel model;

    public SplashPresenterImpl() {
        model = new SplashModel();
    }

    @Override
    public void queryBackground() {
        model.getSplash()
                .compose(RxHelper.observableIO2Main())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ICall<CommonResp<SplashBean>>() {
                    @Override
                    public void onResponse(CommonResp<SplashBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().background(KConstant.img_url + data.OBJECT.bootImage);
                        } else {
                            getView().background("");
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().background("");
                    }
                });
    }
}
