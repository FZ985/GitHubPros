package com.kmt.pro.mvp.contract;

import android.app.Activity;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.HomeBannerBean;
import com.kmt.pro.bean.ProductBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;
import com.kmtlibs.banner.Banner;

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-01 15:04
 **/
public interface PriceContract {
    interface Model {
        Observable<CommonResp<HomeBannerBean>> queryBannerList();

        Observable<CommonListResp<ProductBean>> queryStock();
    }

    interface PriceView extends BaseView {

        Banner getBanner();

        void bannerEmpty();

        void onListSucc(int page, List<ProductBean> datas);

        void onListErr(int page, String msg);

        LifecycleOwner lifecycle();
    }

    abstract class PricePresenter extends CommonPresenter<PriceView> {
        public abstract PricePresenter requestBanner();

        public abstract void requestList(int page);

        public abstract void onItemClick(Activity activity, ProductBean item, int position);
    }
}
