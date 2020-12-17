package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.bean.redpacket.RedPacketRecordBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.SelfRedRecordContract;
import com.kmt.pro.mvp.model.SelfRedRecordModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-28 16:55
 **/
public class SelfRedRecordPresneterImpl extends SelfRedRecordContract.SelfRedRecordPresenter {
    private SelfRedRecordContract.Model model;

    public SelfRedRecordPresneterImpl() {
        model = new SelfRedRecordModel();
    }

    @Override
    public void reqHead(String type) {
        model.redRecordRequest(type)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<RedPacketRecordBean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp<RedPacketRecordBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onHeadInfo(data.OBJECT);
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }

    @Override
    public void reqSendList(int page) {
        model.myRedSendRequest(page + "", "15")
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<RedDetailListBean>>() {
                    @Override
                    public void onResponse(CommonListResp<RedDetailListBean> data) {
                        if (!isViewAttached())return;
                        if (data.getSTATUS().equals("0")){
                            getView().onRedListSucc(page,data.getLIST());
                        }else getView().onRedListErr(page,data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached())return;
                        getView().onRedListErr(page,e.getMessage());
                    }
                });
    }

    @Override
    public void reqGetList(int page) {
        model.myRedGrayRequest(page + "", "15")
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<RedDetailListBean>>() {
                    @Override
                    public void onResponse(CommonListResp<RedDetailListBean> data) {
                        if (!isViewAttached())return;
                        if (data.getSTATUS().equals("0")){
                            getView().onRedListSucc(page,data.getLIST());
                        }else getView().onRedListErr(page,data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached())return;
                        getView().onRedListErr(page,e.getMessage());
                    }
                });
    }
}
