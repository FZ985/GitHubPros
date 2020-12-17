package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.mine.AvatorBean;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.OrderEvaluateContract;
import com.kmt.pro.mvp.model.OrderEvaluateModel;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Create by JFZ
 * date: 2020-08-05 11:29
 **/
public class OrderEvaluatePresenterImpl extends OrderEvaluateContract.OrderEvaluatePresenter {
    private OrderEvaluateModel model;

    public OrderEvaluatePresenterImpl() {
        model = new OrderEvaluateModel();
    }

    @Override
    public void commitBooking(Loadding loadding, String id, String code, String content, String image) {
        model.commitBooking(id, code, content, image)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (isViewAttached()) {
                            if (loadding != null) {
                                loadding.dismiss();
                            }
                            if (data.getSTATUS().equals("0")) {
                                getView().commitSuccess();
                            } else Tools.showToast(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (isViewAttached()) {
                            if (loadding != null) {
                                loadding.dismiss();
                            }
                            Tools.showToast(e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void uploadImages(int index, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("userFile", file.getName(), requestFile);
        model.headImgRequest("5", body)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<AvatorBean>>() {
                    @Override
                    public void onResponse(CommonResp<AvatorBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().uploadImageSuccess(index, data.OBJECT.getImageURL());
                        } else if (data.getSTATUS().equals("3")) {
                            Login(success -> {
                                if (success) uploadImages(index, file);
                            });
                        } else {
                            getView().uploadImageErr(index, data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().uploadImageErr(index, e.getMessage());
                    }
                });
    }
}
