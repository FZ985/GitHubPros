package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.DeviceManagerBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-20 15:51
 **/
public interface DeviceManagerContract {

    interface Model {
        Observable<CommonListResp<DeviceManagerBean>> queryDeviceList();

        Observable<CommonResp> deleteDevice(@Query("infoId") String id);

        Observable<CommonResp> remarkDevice(@Query("infoId") String id, @Query("deviceName") String note);
    }

    interface DeviceManagerView extends BaseView {

        void onList(List<DeviceManagerBean> datas);

        void onListErr(String err);

        void onDeleteSuccess(int position);

        void onRemarkDeviceSuccess(int position, String name);
    }

    abstract class DeviceManagerPresenter extends CommonPresenter<DeviceManagerView> {
        public abstract void requestList();

        public abstract void deleterDevice(String id, int position);

        public abstract void remarkDevice(String id, String name, int position);
    }
}
