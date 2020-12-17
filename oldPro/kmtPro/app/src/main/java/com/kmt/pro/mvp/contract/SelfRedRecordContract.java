package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.bean.redpacket.RedPacketRecordBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-28 16:42
 **/
public interface SelfRedRecordContract {
    interface Model {
        Observable<CommonResp<RedPacketRecordBean>> redRecordRequest(@Query("type") String id);

        Observable<CommonListResp<RedDetailListBean>> myRedGrayRequest(@Query("pageNo") String no, @Query("pageSize") String size);

        Observable<CommonListResp<RedDetailListBean>> myRedSendRequest(@Query("pageNo") String no, @Query("pageSize") String size);
    }

    interface SelfRedRecordView extends BaseView {
        void onHeadInfo(RedPacketRecordBean data);

        void onRedListSucc(int page, List<RedDetailListBean> datas);

        void onRedListErr(int page, String err);
    }

    abstract class SelfRedRecordPresenter extends CommonPresenter<SelfRedRecordView> {
        public abstract void reqHead(String type);

        public abstract void reqSendList(int page);

        public abstract void reqGetList(int page);
    }
}
