package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.redpacket.OpenRedBean;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.RedPacketDetailContract;
import com.kmt.pro.mvp.model.RedPacketDetailModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-28 11:11
 **/
public class RedPacketDetailPresenterImpl extends RedPacketDetailContract.RedPacketDetailPresenter {
    private RedPacketDetailContract.Model model;

    public RedPacketDetailPresenterImpl() {
        model = new RedPacketDetailModel();
    }

    @Override
    public void reqRedPacketInfo(String red_id) {
        model.openRedRequest(red_id)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<OpenRedBean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp<OpenRedBean> data) {
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
    public void reqRedList(int page, String red_id) {
        model.grapRedListRequest(red_id, page + "", "15")
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<RedDetailListBean>>() {
                    @Override
                    public void onResponse(CommonListResp<RedDetailListBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onRedListSucc(page, data.getLIST());
                        } else getView().onRedListErr(page, data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().onRedListErr(page, e.getMessage());
                    }
                });
    }
}
