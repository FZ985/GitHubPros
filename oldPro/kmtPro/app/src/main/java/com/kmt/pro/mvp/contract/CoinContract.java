package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.coin.CoinRecordBean;
import com.kmt.pro.bean.coin.MentionRespBean;
import com.kmt.pro.bean.coin.RechargeSearchBean;
import com.kmt.pro.bean.coin.RecordDetailBean;
import com.kmt.pro.bean.coin.SymbolRespBean;
import com.kmt.pro.mvp.CommonPresenter;

import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-22 17:42
 **/
public interface CoinContract {

    interface CoinListView extends BaseView {
        void onCoinList(List<RechargeSearchBean> list);

        void onCoinListEmpty(String msg);
    }

    interface RechargeView extends BaseView {
        void onRechargeData(List<SymbolRespBean> datas);

        void onRechargeErr(String err);
    }

    interface RecordView extends BaseView {

        void onRecordList(int page, List<CoinRecordBean> datas);

        void onRecordErr(int page, String err);
    }

    interface RecordDetailView extends BaseView {
        void onDetailSucc(RecordDetailBean data);
    }

    interface MentionMoneyView extends BaseView {

        void onMentionData(MentionRespBean data);

        void onCommit(int flag, String msg);
    }

    abstract class CoinPresenter extends CommonPresenter<BaseView> {

        public abstract void reqCoinList();

        public abstract void reqRechargeInfo(String symbol_id);

        public abstract void reqRecordList(int type, int page, String id);

        public abstract void reqRrecordDetail(int type, String blockHash);

        public abstract void reqMentionInit(boolean isLoadding, String symbol_id);

        public abstract void reqMentionCommit(String smsCode, String symbol_id, String symbol_num, String address, String network_id, String serviceCharge);
    }


}
