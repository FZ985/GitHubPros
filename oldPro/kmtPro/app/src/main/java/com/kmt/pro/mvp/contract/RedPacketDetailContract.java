package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.redpacket.OpenRedBean;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-28 10:57
 **/
public interface RedPacketDetailContract {

    interface Model {
        Observable<CommonResp<OpenRedBean>> openRedRequest(@Query("envelopId") String id);

        Observable<CommonListResp<RedDetailListBean>> grapRedListRequest(@Query("envelopId") String id, @Query("pageNo") String no, @Query("pageSize") String size);
    }

    interface RedPacketDetailView extends BaseView {
        void onHeadInfo(OpenRedBean data);

        void onRedListSucc(int page, List<RedDetailListBean> datas);

        void onRedListErr(int page, String err);
    }

    abstract class RedPacketDetailPresenter extends CommonPresenter<RedPacketDetailView> {
        public abstract void reqRedPacketInfo(String red_id);

        public abstract void reqRedList(int page, String red_id);
    }

}
