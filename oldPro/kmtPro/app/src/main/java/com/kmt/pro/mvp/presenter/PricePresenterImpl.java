package com.kmt.pro.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.kmt.pro.adapter.banner.HomeBannerAdapter;
import com.kmt.pro.bean.HomeBannerBean;
import com.kmt.pro.bean.ProductBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.PriceContract;
import com.kmt.pro.mvp.model.PriceModel;
import com.kmt.pro.sp.DetailSp;
import com.kmt.pro.ui.activity.detail.WaitActivity;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-01 15:40
 **/
public class PricePresenterImpl extends PriceContract.PricePresenter {
    private PriceContract.Model model;

    public PricePresenterImpl() {
        model = new PriceModel();
    }

    @Override
    public PriceContract.PricePresenter requestBanner() {
        model.queryBannerList()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<HomeBannerBean>>() {
                    @Override
                    public void onResponse(CommonResp<HomeBannerBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            setBanner(data.OBJECT.getBannerList());
                        } else getView().bannerEmpty();
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (getView() != null) {
                            getView().bannerEmpty();
                        }
                    }
                });
        return this;
    }

    private void setBanner(List<HomeBannerBean.BannerBean> bannerList) {
        if (getView().getBanner() == null) return;
        if (bannerList.size() > 0) {
            HomeBannerAdapter adapter = new HomeBannerAdapter(bannerList);
            getView().getBanner().setAdapter(adapter)//设置适配器
                    .addBannerLifecycleObserver(getView().lifecycle())//添加生命周期观察者
                    .isAutoLoop(bannerList.size() > 1)
                    .setOnBannerListener((data, position) -> {
                        Tools.showToast(position + "");
                    });
        } else {
            getView().getBanner().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void requestList(int page) {
        model.queryStock().compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<ProductBean>>() {
                    @Override
                    public void onResponse(CommonListResp<ProductBean> data) {
                        if (!isViewAttached()) return;
                        if ("0".equals(data.getSTATUS())) {
                            getView().onListSucc(page, data.getLIST());
                        } else {
                            getView().onListSucc(page, new ArrayList<ProductBean>());
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().onListErr(page, e.getMessage());
                    }
                });
    }

    @Override
    public void onItemClick(Activity activity, ProductBean item, int position) {
        if (activity == null) return;
        if (item == null) return;
        Intent i = new Intent();
        if (item.investorsStatus == 0) {
            if (item.presellFlag == 2) {
//                i = new Intent(activity, DetailsActivity.class);
                i = new Intent(activity, WaitActivity.class);
                DetailSp.get().setmPresell(item.presellFlag).setInvestorsFixPrice(item.investorsFixPrice);
            } else {
                i = new Intent(activity, WaitActivity.class);
            }
        } else if (item.investorsStatus == 2 || item.investorsStatus == 6) {
//            i = new Intent(activity, DetailsActivity.class);
            i = new Intent(activity, WaitActivity.class);
        } else if (item.investorsStatus == 5) { // 退市
            i = new Intent(activity, WaitActivity.class);
            i.putExtra("status", "5");
        }
        DetailSp.get().setName(item.investorsName).setCode(item.investorsCode);
        i.putExtra("name", item.investorsName);
        i.putExtra("code", item.investorsCode);
        activity.startActivity(i);
    }
}
