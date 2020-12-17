package com.kmt.pro.ui.activity.detail;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.adapter.DetailCommentAdapter;
import com.kmt.pro.adapter.banner.StringBannerAdapter;
import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.bean.detail.CommentBean;
import com.kmt.pro.bean.detail.DetailBean;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Login;
import com.kmt.pro.mvp.contract.DetailContract;
import com.kmt.pro.mvp.presenter.DetailPresenterImpl;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.widget.countdownview.CountdownListener;
import com.kmt.pro.widget.countdownview.CountdownTextView;
import com.kmt.pro.widget.countdownview.formatter.DefaultDateFormatter;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.utils.Utils;
import com.kmtlibs.banner.Banner;
import com.kmtlibs.banner.config.IndicatorConfig;
import com.kmtlibs.banner.indicator.RoundLinesIndicator;
import com.kmtlibs.immersionbar.BarHide;
import com.kmtlibs.immersionbar.ImmersionBar;
import com.lxbuytimes.kmtapp.MainActivity;

import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-02 10:11
 **/
public class WaitActivity extends BaseActivity<DetailContract.DetailPresenter> implements DetailContract.CommentView {
    @BindView(R.id.wait_banner)
    Banner waitBanner;
    @BindView(R.id.details_buy_tv)
    TextView detailsBuyTv;
    @BindView(R.id.details_delist_tv)
    TextView detailsDelistTv;
    @BindView(R.id.name_code)
    TextView nameCode;
    @BindView(R.id.time_view)
    CountdownTextView timeView;
    @BindView(R.id.linear_time_view)
    LinearLayout linearTimeView;
    @BindView(R.id.wait_recycler)
    RecyclerView waitRecycler;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.wait_ll)
    LinearLayout waitLl;
    @BindView(R.id.details_tv)
    TextView detailsTv;
    @BindView(R.id.ll_backimg)
    LinearLayout llBackimg;
    @BindView(R.id.ll_refresh)
    LinearLayout llRefresh;
    @BindView(R.id.ll_tittle)
    RelativeLayout llTittle;
    private View headview;
    private TextView descTv;
    private String code;
    private String name;
    //是否有推送消息
    private String isPush = "0";
    private String fitPrice;
    private String status;
    private DetailCommentAdapter adapter;

    @Override
    public int getLayoutId() {
        if (null != getIntent()) {
            code = getIntent().getStringExtra("code");
            name = getIntent().getStringExtra("name");
            isPush = getIntent().getStringExtra("jpush");
            status = getIntent().getStringExtra("status");
        }
        return R.layout.activity_wait;
    }

    @Override
    public void initView() {
        nameCode.setText(name + "(" + code + ")");
        adapter = new DetailCommentAdapter();
        waitRecycler.setLayoutManager(new LinearLayoutManager(this));
        headview = LayoutInflater.from(this).inflate(R.layout.head_wait_detail, null);
        descTv = headview.findViewById(R.id.detail_desc);
        adapter.setHeaderView(headview);
        adapter.setHeaderWithEmptyEnable(true);
        waitRecycler.setAdapter(adapter);
    }

    @Override
    public void initData() {
        sendData();
        if ("5".equals(status)) {
            // 退市
            llTittle.setVisibility(View.VISIBLE);
            detailsTv.setText(name);
        } else {
            waitLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInformationSuccess(DetailBean data) {
        if (data.baseInformation != null) {
            fitPrice = data.baseInformation.investorsFixPrice;
        }
        DetailBean.InvestorsBean investors = data.investors;
        if (investors != null) {
            if (!TextUtils.isEmpty(investors.investorsPcImg)) {
                List<String> images = Arrays.asList(investors.investorsPcImg.split(","));
//                if (images != null && images.size() > 0)
//                    checkInImg = images.get(0);
                waitBanner.setVisibility(View.VISIBLE);
                waitBanner.setAdapter(new StringBannerAdapter(images))//设置适配器
                        .addBannerLifecycleObserver(this)//添加生命周期观察者
                        .setIndicator(new RoundLinesIndicator(this))
                        .setIndicatorSelectedColorRes(R.color.colorAccent)
                        .setIndicatorNormalColorRes(R.color.white)
                        .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                        .setIndicatorSelectedWidth(Utils.dip2px(this, 15))
                        .setIndicatorMargins(new IndicatorConfig.Margins(0, 0, Utils.dip2px(this, 15), Utils.dip2px(this, 10)));
            }

            if ("5".equals(status)) {
                linearTimeView.setVisibility(View.GONE);
                detailsBuyTv.setVisibility(View.GONE);
                detailsDelistTv.setVisibility(View.VISIBLE);
            } else {
                if (investors.preSellTime > 0) {
                    linearTimeView.setVisibility(View.VISIBLE);
                    Logger.e("investors倒计时:" + investors.preSellTime);
                    timeView.setDataFormater(new DefaultDateFormatter("dd天HH时mm分ss秒"))
                            .setCountdownDeadineTime(System.currentTimeMillis() + investors.preSellTime)
                            .setCountdownListener(new CountdownListener() {
                                @Override
                                public void onStartTick() {

                                }

                                @Override
                                public void onStopTick() {
                                    if (!isFinishing()) {
                                        linearTimeView.setVisibility(View.GONE);
                                        detailsBuyTv.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onTick(CountdownTextView CountdownTextView, long startTime, long stopTime) {

                                }
                            }).start();
                } else {
                    linearTimeView.setVisibility(View.GONE);
                    detailsDelistTv.setVisibility(View.GONE);
                    detailsBuyTv.setVisibility(View.VISIBLE);
                }
            }
//            descroption_house = investors.houseDescription;
            descTv.setText(investors.houseDescription);
        }
        requestCommentList();
    }

    private void requestCommentList() {
        if ("5".equals(status)) {
            mPresenter.commentList(code, "1", "100");
        }
    }

    @Override
    public void onCommentList(List<CommentBean> list) {
        if (list.size() > 0) {
            adapter.setNewInstance(list);
        }
    }

    @Override
    public void onInformationErr(String msg) {
        requestCommentList();
        Tools.showToast(msg);
    }

    @OnClick({R.id.details_buy_tv, R.id.ll_back, R.id.ll_backimg, R.id.ll_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.details_buy_tv:
                if (Login.get().isLogin()) {
                    Tools.showToast("buy");
                } else {
                    ActivityHelper.toLogin(this, KConstant.BackPage.normal);
                }
                break;
            case R.id.ll_backimg:
            case R.id.ll_back:
                this.onBackPressed();
                break;
            case R.id.ll_refresh:
                sendData();
                break;
        }
    }

    private void sendData() {
        mPresenter.queryStorkInformation(code);
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        if ("5".equals(status)) {
            // 退市
            immersionBar.barColor(R.color.white).titleBarMarginTop(llTittle).statusBarDarkFont(true).init();
        } else {
            immersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
        }
    }

    @Override
    public void onBackPressed() {
        if ("1".equals(isPush)) {
            //跳主页,并退出当前页面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    protected DetailContract.DetailPresenter getPresenter() {
        return new DetailPresenterImpl<DetailContract.CommentView>();
    }
}
