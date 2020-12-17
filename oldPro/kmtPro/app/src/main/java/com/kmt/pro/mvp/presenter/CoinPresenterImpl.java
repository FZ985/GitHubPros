package com.kmt.pro.mvp.presenter;

import com.kmt.pro.adapter.coin.FlagBean;
import com.kmt.pro.bean.coin.CoinRecordBean;
import com.kmt.pro.bean.coin.MentionRespBean;
import com.kmt.pro.bean.coin.RechargeSearchBean;
import com.kmt.pro.bean.coin.RecordDetailBean;
import com.kmt.pro.bean.coin.SymbolRespBean;
import com.kmt.pro.helper.PinyinComparatorHelp;
import com.kmt.pro.http.HttpUrl;
import com.kmt.pro.http.ok;
import com.kmt.pro.http.request.BlockHashReq;
import com.kmt.pro.http.request.MentionCommitReq;
import com.kmt.pro.http.request.RechargeRecordReq;
import com.kmt.pro.http.request.SymbolIdReq;
import com.kmt.pro.http.request.UserIdReq;
import com.kmt.pro.http.request.V2BaseReq;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.CoinContract;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.okhttp.callback.OkRequestCallback;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;

import java.util.Collections;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-22 17:58
 **/
public class CoinPresenterImpl extends CoinContract.CoinPresenter {
    @Override
    public void reqCoinList() {
        V2BaseReq<UserIdReq> req = new V2BaseReq<>();
        req.setPars(new UserIdReq());
        ok.get().postJson(HttpUrl.theCoinList, new OkRequestCallback<CommonListResp<RechargeSearchBean>>() {
            @Override
            public void onResponse(CommonListResp<RechargeSearchBean> data) {
                if (!isViewAttached()) return;
                if (data.getCode().equals("ok")) {
                    if (getView() instanceof CoinContract.CoinListView) {
                        List<RechargeSearchBean> list = data.getData();
                        Collections.sort(list, new PinyinComparatorHelp<RechargeSearchBean>());
                        ((CoinContract.CoinListView) getView()).onCoinList(list);
                    }
                } else {
                    if (getView() instanceof CoinContract.CoinListView)
                        ((CoinContract.CoinListView) getView()).onCoinListEmpty(data.getRetMsg());
                }
            }

            @Override
            public void onError(int code, Exception e) {
                if (getView() != null && getView() instanceof CoinContract.CoinListView)
                    ((CoinContract.CoinListView) getView()).onCoinListEmpty(e.getMessage());
            }
        }, req, DefLoad.use(getActivity()));
    }

    @Override
    public void reqRechargeInfo(String symbol_id) {
        V2BaseReq<SymbolIdReq> req = new V2BaseReq<>();
        req.setPars(new SymbolIdReq(symbol_id));
        ok.get().postJson(HttpUrl.recharge, new OkRequestCallback<CommonListResp<SymbolRespBean>>() {
            @Override
            public void onResponse(CommonListResp<SymbolRespBean> data) {
                if (!isViewAttached()) return;
                if (data.getCode().equals("ok")) {
                    if (getView() instanceof CoinContract.RechargeView) {
                        ((CoinContract.RechargeView) getView()).onRechargeData(data.getData());
                    }
                } else {
                    if (getView() instanceof CoinContract.RechargeView) {
                        ((CoinContract.RechargeView) getView()).onRechargeErr(data.getRetMsg());
                    }
                }
            }

            @Override
            public void onError(int code, Exception e) {
                if (!isViewAttached()) return;
                if (getView() instanceof CoinContract.RechargeView) {
                    ((CoinContract.RechargeView) getView()).onRechargeErr(e.getMessage());
                }
            }
        }, req, DefLoad.use(getActivity()));
    }

    @Override
    public void reqRecordList(int type, int page, String id) {
        V2BaseReq<RechargeRecordReq> req = new V2BaseReq<>();
        req.setPars(new RechargeRecordReq(page, id));
        OkRequestCallback<CommonListResp<CoinRecordBean>> callback = new OkRequestCallback<CommonListResp<CoinRecordBean>>() {
            @Override
            public void onResponse(CommonListResp<CoinRecordBean> data) {
                if (!isViewAttached()) return;
                if (data.getCode().equals("ok")) {
                    if (getView() instanceof CoinContract.RecordView) {
                        ((CoinContract.RecordView) getView()).onRecordList(page, data.getData());
                    }
                } else {
                    if (getView() instanceof CoinContract.RecordView) {
                        ((CoinContract.RecordView) getView()).onRecordErr(page, data.getRetMsg());
                    }
                }
            }

            @Override
            public void onError(int code, Exception e) {
                if (!isViewAttached()) return;
                if (getView() instanceof CoinContract.RecordView) {
                    ((CoinContract.RecordView) getView()).onRecordErr(page, "暂无历史记录");
                }
            }
        };
        if (type == 1) {//充币详情
            ok.get().postJson(HttpUrl.recharge_history, callback, req, (page == 1) ? DefLoad.use(getActivity()) : null);
        }
        if (type == 2) {//提币详情
            ok.get().postJson(HttpUrl.mention_record, callback, req, (page == 1) ? DefLoad.use(getActivity()) : null);
        }
    }

    @Override
    public void reqRrecordDetail(int type, String blockHash) {
        OkRequestCallback callback = new OkRequestCallback<CommonResp<RecordDetailBean>>() {
            @Override
            public void onResponse(CommonResp<RecordDetailBean> data) {
                if (!isViewAttached()) return;
                if (data.getCode().equals("ok") && data.data != null) {
                    if (getView() instanceof CoinContract.RecordDetailView) {
                        ((CoinContract.RecordDetailView) getView()).onDetailSucc(data.data);
                    }
                }
            }

            @Override
            public void onError(int code, Exception e) {

            }
        };
        V2BaseReq<BlockHashReq> req = new V2BaseReq<>();
        req.setPars(new BlockHashReq(blockHash));
        if (type == 2) {
            ok.get().postJson(HttpUrl.mention_detail, callback, req, DefLoad.use(getActivity()));
        } else {
            ok.get().postJson(HttpUrl.recharge_record_detail, callback, req, DefLoad.use(getActivity()));
        }
    }

    @Override
    public void reqMentionInit(boolean isLoadding, String symbol_id) {
        V2BaseReq<SymbolIdReq> req = new V2BaseReq<>();
        req.setPars(new SymbolIdReq(symbol_id));
        ok.get().postJson(HttpUrl.mention, new OkRequestCallback<CommonResp<MentionRespBean>>() {
            @Override
            public void onResponse(CommonResp<MentionRespBean> data) {
                if (data.getCode().equals("ok")) {
                    if (getView() instanceof CoinContract.MentionMoneyView) {
                        ((CoinContract.MentionMoneyView) getView()).onMentionData(data.data);
                    }
                } else Tools.showToast(data.getRetMsg());
            }

            @Override
            public void onError(int code, Exception e) {
                Tools.showToast("网络异常,稍后重试!");
            }
        }, req, isLoadding ? DefLoad.use(getActivity()) : null);
    }

    @Override
    public void reqMentionCommit(String smsCode, String symbol_id, String symbol_num, String address, String network_id, String serviceCharge) {
        V2BaseReq<MentionCommitReq> req = new V2BaseReq<>();
        req.setPars(new MentionCommitReq(smsCode, symbol_id, symbol_num, serviceCharge, address, network_id));
        ok.get().postJson(HttpUrl.mention_commit, new OkRequestCallback<CommonResp<FlagBean>>() {
            @Override
            public void onResponse(CommonResp<FlagBean> data) {
                if (!isViewAttached()) return;
                if (getView() instanceof CoinContract.MentionMoneyView)
                    if (data.getCode().equals("ok")) {
                        if (data.data != null) {
                            ((CoinContract.MentionMoneyView) getView()).onCommit(data.data.flag, data.getRetMsg());
                        } else {
                            ((CoinContract.MentionMoneyView) getView()).onCommit(-1, "提币失败!");
                        }
                    } else {
                        ((CoinContract.MentionMoneyView) getView()).onCommit(-1, data.getRetMsg());
                    }
            }

            @Override
            public void onError(int code, Exception e) {
                if (!isViewAttached()) return;
                if (getView() instanceof CoinContract.MentionMoneyView)
                    ((CoinContract.MentionMoneyView) getView()).onCommit(-1, "提币失败!");
            }
        }, req, null);
    }
}
