package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.detail.CommentBean;
import com.kmt.pro.bean.detail.DetailBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.DetailContract;
import com.kmt.pro.mvp.model.DetailModel;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-02 16:06
 **/
public class DetailPresenterImpl<V extends DetailContract.DetailView> extends DetailContract.DetailPresenter<V> {
    private DetailContract.Model model;

    public DetailPresenterImpl() {
        model = new DetailModel();
    }

    @Override
    public void queryStorkInformation(String code) {
        model.queryStorkInformation(code)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<DetailBean>>() {
                    @Override
                    public void onResponse(CommonResp<DetailBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onInformationSuccess(data.OBJECT);
                        } else {
                            getView().onInformationErr(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().onInformationErr(e.getMessage());
                    }
                });
    }

    @Override
    public void commentList(String code, String current, String size) {
        model.commentList(code, current, size)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<CommentBean>>() {
                    @Override
                    public void onResponse(CommonListResp<CommentBean> data) {
                        if (!isViewAttached()) return;
                        if ("0".equals(data.getSTATUS())) {
                            if (getView() instanceof DetailContract.CommentView) {
                                ((DetailContract.CommentView) getView()).onCommentList(data.getLIST());
                            }
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }
}
