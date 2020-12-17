package com.kmt.pro.mvp.presenter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import com.kmt.pro.adapter.CommonViewPageAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseFragment;
import com.kmt.pro.bean.mine.AvatorBean;
import com.kmt.pro.bean.mine.LockUpBean;
import com.kmt.pro.bean.mine.MineBean;
import com.kmt.pro.bean.mine.MineItemBean;
import com.kmt.pro.bean.mine.SymbolNumsBean;
import com.kmt.pro.http.ok;
import com.kmt.pro.http.HttpUrl;
import com.kmt.pro.http.request.SymbolNumReq;
import com.kmt.pro.http.request.V2BaseReq;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.MineContract;
import com.kmt.pro.mvp.model.MineModel;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.ui.fragment.mine.MineHorBarChartFragment;
import com.kmt.pro.ui.fragment.mine.MinePieChartFragment;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.utils.compressor.Compressor;
import com.kmt.pro.widget.ScaleCircleNavigator;
import com.kmtlibs.okhttp.callback.OkRequestCallback;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import magicindicator.MagicIndicator;
import magicindicator.ViewPagerHelper;
import magicindicator.buildins.UIUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Create by JFZ
 * date: 2020-07-09 12:05
 **/
public class MinePresenterImpl extends MineContract.MinePresenter {
    private MineContract.Model model;

    public MinePresenterImpl() {
        model = new MineModel();
    }

    @Override
    public void reqInfo() {
        model.requestPerson()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<MineBean>>() {
                    @Override
                    public void onResponse(CommonResp<MineBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().respInfo(data.OBJECT);
                        } else if (data.getSTATUS().equals("3")) {
                            Login((b) -> {
                                if (b) reqInfo();
                                else Tools.showToast(data.MSG);
                            });
                        } else {
                            Tools.showToast(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }

    @Override
    public void reqItems() {
        model.getMineItem()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<MineItemBean>>() {
                    @Override
                    public void onResponse(CommonListResp<MineItemBean> data) {
                        if (!isViewAttached()) return;
                        if (data != null && data.getLIST().size() > 0) {
                            getView().respItems(data.getLIST());
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }

    @Override
    public void reqNotice() {
        model.rollNotices()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<String>>() {
                    @Override
                    public void onResponse(CommonListResp<String> data) {
                        if (!isViewAttached()) return;
                        getView().respNotice(data.getLIST());
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }

    @Override
    public void reqLockup() {
        model.lockUp().compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<LockUpBean>>() {
                    @Override
                    public void onResponse(CommonResp<LockUpBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().respLockup(data.OBJECT);
                        } else {
                            getView().respLockup(null);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().respLockup(null);
                    }
                });
    }

    @Override
    public void reqSymbols() {
        V2BaseReq<SymbolNumReq> req = new V2BaseReq<>();
        req.setPars(new SymbolNumReq("1"));
        ok.get().postJson(HttpUrl.walletget, new OkRequestCallback<CommonResp<SymbolNumsBean>>() {
            @Override
            public void onResponse(CommonResp<SymbolNumsBean> data) {
                if (!isViewAttached()) return;
                if (data.getCode().equals("ok")) {
                    getView().respSymbol(data.data);
                } else {
                    getView().respSymbol(null);
                }
            }

            @Override
            public void onError(int code, Exception e) {
                if (!isViewAttached()) return;
                getView().respSymbol(null);
            }
        }, req, null);
    }

    @Override
    public void updateNickName(String name) {
        model.modifyPersonMessage("0", name)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            UserSp.get().setNickName(name);
                            getView().respNickname();
                            Tools.showToast("设置成功");
                        } else if (data.getSTATUS().equals("3")) {
                            Login((b) -> {
                                if (b) updateNickName(name);
                                Tools.showToast(data.MSG);
                            });
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void compressorAvator(String path) {
        //压缩图片
        new Compressor(BaseApp.getInstance())
                .compressToFileAsObservable(new File(path))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> uploadAvator(file), throwable -> uploadAvator(new File(path)));
    }

    @Override
    public void uploadAvator(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("userFile", file.getName(), requestFile);
        model.headImgRequest("0", body)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<AvatorBean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp<AvatorBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            modifyPersonMessage(data.OBJECT.getImageURL());
                            Tools.showToast("上传成功");
                        } else if (data.getSTATUS().equals("3")) {
                            Login((b) -> uploadAvator(file));
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }

    private void modifyPersonMessage(String path) {
        model.modifyPersonMessage("22", path)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            UserSp.get().setHeadImg(path).setHeadImg(path);
                            getView().respAvator();
                        } else if (data.getSTATUS().equals("3")) {
                            Login((b) -> {
                                if (b) modifyPersonMessage(path);
                                else Tools.showToast(data.MSG);
                            });
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        Tools.showToast(e.getMessage());
                    }
                });
    }

    public void bindChart(ViewPager mineViewpager, MagicIndicator mineIndicator, MineBean mine, FragmentManager fragmentManager) {
        List<BaseFragment> mContents = new ArrayList<>();
        MineHorBarChartFragment first = new MineHorBarChartFragment();
        MinePieChartFragment second = new MinePieChartFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mine);//发行人数据
        first.setArguments(bundle);
        second.setArguments(bundle);
        mContents.add(first);
        mContents.add(second);
        mineViewpager.setOffscreenPageLimit(mContents.size());
        //实例化适配器
        final CommonViewPageAdapter mAdapter = new CommonViewPageAdapter(fragmentManager, mContents);
        mineViewpager.setAdapter(mAdapter);
        mineViewpager.clearOnPageChangeListeners();
        mineViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int postion) {
                if (postion == 1) {
                    ((MinePieChartFragment) mAdapter.getItem(postion)).isShow();
                } else if (postion == 0) {
                    ((MineHorBarChartFragment) mAdapter.getItem(postion)).isShow();
                }
            }
        });
        ScaleCircleNavigator circleNavigator = new ScaleCircleNavigator(mineViewpager.getContext());
        circleNavigator.setCircleCount(2);
        circleNavigator.setMaxRadius(UIUtil.dip2px(BaseApp.getInstance(), 4));
        circleNavigator.setNormalCircleColor(Color.parseColor("#8E8EA3"));
        circleNavigator.setSelectedCircleColor(Color.parseColor("#0212AB"));
        circleNavigator.setCircleClickListener(index -> mineViewpager.setCurrentItem(index));
        mineIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mineIndicator, mineViewpager);
    }
}
