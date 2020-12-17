package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.CountryBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.mvp.CommonPresenter;
import com.kmt.pro.utils.chinese2pinyin.CityItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-06 15:03
 **/
public interface CountryCodeContract {
    interface Model {
        Observable<CommonListResp<CountryBean>> getConturyList();
    }

    interface CountryCodeView extends BaseView {
        void onList(List<CityItem> data);

        void onListErr(String err);
    }

    abstract class CountryPresenter extends CommonPresenter<CountryCodeView> {
        public abstract void requestCountryCode();
    }
}
