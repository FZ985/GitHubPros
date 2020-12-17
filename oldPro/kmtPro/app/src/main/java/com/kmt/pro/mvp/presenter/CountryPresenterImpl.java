package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.CountryBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.mvp.contract.CountryCodeContract;
import com.kmt.pro.mvp.model.CountryModel;
import com.kmt.pro.utils.chinese2pinyin.CityItem;
import com.kmt.pro.utils.chinese2pinyin.PinYin;
import com.kmt.pro.utils.chinese2pinyin.PinyinComparator;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-06 15:06
 **/
public class CountryPresenterImpl extends CountryCodeContract.CountryPresenter {
    private CountryCodeContract.Model model;

    public CountryPresenterImpl() {
        model = new CountryModel();
    }

    @Override
    public void requestCountryCode() {
        model.getConturyList()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<CountryBean>>() {
                    @Override
                    public void onResponse(CommonListResp<CountryBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            List<CityItem> list = new ArrayList<>();
                            try {
                                for (int i = 0; i < data.getLIST().size(); i++) {
                                    String cityName = data.getLIST().get(i).getName();
                                    list.add(new CityItem(cityName, PinYin.getPinYin(cityName), data.getLIST().get(i).phoneAreaCode));
                                }
                            } catch (Exception e) {

                            }
                            Collections.sort(list, new PinyinComparator());
                            getView().onList(list);
                        } else getView().onListErr(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().onListErr(e.getMessage());
                    }
                });
    }
}
