package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.detail.CommentBean;
import com.kmt.pro.bean.detail.DetailBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-02 15:47
 **/
public interface DetailContract {
    interface Model {
        Observable<CommonResp<DetailBean>> queryStorkInformation(@Query("pcode") String code);

        Observable<CommonListResp<CommentBean>> commentList(@Query("investorsCode") String code, @Query("currentPage") String current, @Query("pageSize") String size);

    }

    interface DetailView extends BaseView {
        void onInformationSuccess(DetailBean data);

        void onInformationErr(String msg);
    }

    //评论
    interface CommentView extends DetailView {
        void onCommentList(List<CommentBean> list);
    }

    abstract class DetailPresenter<V extends BaseView> extends CommonPresenter<V> {
        public abstract void queryStorkInformation(String code);

        public abstract void commentList(String code, String current, String size);
    }
}
