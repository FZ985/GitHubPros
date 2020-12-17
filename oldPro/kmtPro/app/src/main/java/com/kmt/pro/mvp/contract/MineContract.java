package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.mine.AvatorBean;
import com.kmt.pro.bean.mine.LockUpBean;
import com.kmt.pro.bean.mine.MineBean;
import com.kmt.pro.bean.mine.MineItemBean;
import com.kmt.pro.bean.mine.SymbolNumsBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-09 11:55
 **/
public interface MineContract {

    interface Model {
        Observable<CommonResp<MineBean>> requestPerson();

        Observable<CommonResp<LockUpBean>> lockUp();

        Observable<CommonListResp<MineItemBean>> getMineItem();

        Observable<CommonListResp<String>> rollNotices();

        Observable<CommonResp> modifyPersonMessage(@Query("type") String type, @Query("content") String content);

        Observable<CommonResp<AvatorBean>> headImgRequest(@Query("type") String type, @Part MultipartBody.Part file);
    }

    interface MineView extends BaseView {

        void respInfo(MineBean mine);

        void respLockup(LockUpBean lock);

        void respItems(List<MineItemBean> list);

        void respSymbol(SymbolNumsBean symbolNumsBean);

        void respNotice(List<String> list);

        void respNickname();

        void respAvator();
    }


    abstract class MinePresenter extends CommonPresenter<MineView> {
        //请求个人中心数据
        public abstract void reqInfo();

        //请求聊表条目
        public abstract void reqItems();

        //请求通知
        public abstract void reqNotice();

        //请求锁仓
        public abstract void reqLockup();

        //请求数据货币
        public abstract void reqSymbols();

        public abstract void updateNickName(String name);

        public abstract void compressorAvator(String path);

        public abstract void uploadAvator(File file);
    }

}
