package com.kmt.pro.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.helper.Actions;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.AccountIncomeDetailContract;
import com.kmt.pro.mvp.presenter.AccountIncomeDetailPresenterImpl;
import com.kmt.pro.share.Share;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.dialog.NativeDlg;
import com.kmtlibs.app.receive.SendRecvHelper;
import com.kmtlibs.app.utils.Logger;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-27 13:59
 **/
public class AccountIncomeDetailActivity extends BaseToolBarActivity<AccountIncomeDetailContract.AccountIncomeDetailPresenter> implements AccountIncomeDetailContract.AccountIncomeDetailView {
    @BindView(R.id.head_image)
    ImageView headImage;
    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.head_money)
    TextView headMoney;
    @BindView(R.id.head_status)
    TextView headStatus;
    @BindView(R.id.withdrawal_poundrage_tv)
    TextView withdrawalPoundrageTv;
    @BindView(R.id.withdrawal_type_tv)
    TextView withdrawalTypeTv;
    @BindView(R.id.withdrawal_bankId_tv)
    TextView withdrawalBankIdTv;
    @BindView(R.id.withdrawal_ll)
    LinearLayout withdrawalLl;
    @BindView(R.id.trade_poundage_tv)
    TextView tradePoundageTv;
    @BindView(R.id.trade_poundage_ll)
    LinearLayout tradePoundageLl;
    @BindView(R.id.trade_place_tv)
    TextView tradePlaceTv;
    @BindView(R.id.trade_ll)
    LinearLayout tradeLl;
    @BindView(R.id.recharge_poundrage_tv)
    TextView rechargePoundrageTv;
    @BindView(R.id.recharge_poundrage_ll)
    LinearLayout rechargePoundrageLl;
    @BindView(R.id.recharge_order_type_tv)
    TextView rechargeOrderTypeTv;
    @BindView(R.id.recharge_charge_tv)
    TextView rechargeChargeTv;
    @BindView(R.id.recharge_ll)
    LinearLayout rechargeLl;
    @BindView(R.id.xinquan_place_tv)
    TextView xinquanPlaceTv;
    @BindView(R.id.xingquan_ll)
    LinearLayout xingQuanLl;
    @BindView(R.id.red_check_detail)
    TextView redCheckDetail;
    @BindView(R.id.red_return_record_tv)
    TextView redReturnRecordTv;
    @BindView(R.id.red_return_record_ll)
    LinearLayout redReturnRecordLl;
    @BindView(R.id.red_ll)
    LinearLayout redLl;
    @BindView(R.id.btn_bottom_ll)
    RelativeLayout btnBottomLl;
    @BindView(R.id.default_time_tv)
    TextView defaultTimeTv;
    @BindView(R.id.default_ll)
    LinearLayout defaultLl;
    @BindView(R.id.btn_withdrawl)
    Button btnWithdrawl;
    @BindView(R.id.btn_red)
    Button btnRed;

    private String mRedNote, mRecordType, recordStatus, mType, bankType, bankId;
    private String id, name, freeRate, picture, money, date, owner;
    private String mTitle;//分享标题
    private String mRedId;//分享红包的ID

    @Override
    public int getLayoutId() {
        mType = getIntent().getStringExtra("Type");//是否提现
        bankType = getIntent().getStringExtra("bankType");//提现账号类型
        bankId = getIntent().getStringExtra("bankId");//提现账号
        owner = getIntent().getStringExtra("owner");//账号用户名,红包的交易类型的话,这个值为发行人头像
        date = getIntent().getStringExtra("date");//记录日期
        money = getIntent().getStringExtra("money");//交易金额
        picture = getIntent().getStringExtra("picture");//显示的状态
        freeRate = getIntent().getStringExtra("freeRate");//手续费
        name = getIntent().getStringExtra("name");//发行人名
        id = getIntent().getStringExtra("recordId");//交易记录主键ID
        recordStatus = getIntent().getStringExtra("recordStatus");//交易状态
        mRecordType = getIntent().getStringExtra("recordType");//交易明细
        mRedNote = getIntent().getStringExtra("name");//红包留言
        mTitle = getIntent().getStringExtra("name");//红包留言
        return R.layout.activity_accountincome_detail;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("账单详情");
        Logger.e("detail_mType:" + mType + ",bankType:" + bankType + ",bankId:" + bankId + ",owner:" + owner + ",date:" + date + ",money:" + money);
        Logger.e("detail_picture:" + picture + ",freeRate:" + freeRate + ",name:" + name + ",id:" + id + ",recordStatus:" + recordStatus + ",mRecordType:" + mRecordType);
        if ("提现".equals(mType)) {
            withdrawalLl.setVisibility(View.VISIBLE);
            btnBottomLl.setVisibility(View.VISIBLE);
            initWithDraw();
        } else if ("买卖".equals(mType)) {
            tradeLl.setVisibility(View.VISIBLE);
            initTrade();
        } else if ("充值".equals(mType) || "分销奖励".equals(mType)) {
            rechargeLl.setVisibility(View.VISIBLE);
            if ("分销奖励".equals(mType)) {
                initRecharge(1);
            } else {
                initRecharge(0);
            }
        } else if ("预约".equals(mType)) {
            xingQuanLl.setVisibility(View.VISIBLE);
            initXingQuan();
        } else if (mType.contains("红包")) {
            btnBottomLl.setVisibility(View.VISIBLE);
            redLl.setVisibility(View.VISIBLE);
            initRed();
        } else {
            defaultLl.setVisibility(View.VISIBLE);
            withdrawalLl.setVisibility(View.GONE);
            btnBottomLl.setVisibility(View.GONE);
            tradeLl.setVisibility(View.GONE);
            rechargeLl.setVisibility(View.GONE);
            xingQuanLl.setVisibility(View.GONE);
            redLl.setVisibility(View.GONE);
            initDefault();
        }
    }

    //默认
    private void initDefault() {
        headMoney.setText(money);
        headStatus.setText(recordStatus);
        defaultTimeTv.setText(date);
        headTitle.setText(mRecordType);
    }

    //红包
    private void initRed() {
        mRedId = id;//红包ID
        headMoney.setText(money);
        headTitle.setText(mRecordType);
        if (!TextUtils.isEmpty(owner))
            loadImage(owner);
        if (recordStatus.startsWith("退还")) {
            headStatus.setText("已完成");
            redReturnRecordLl.setVisibility(View.VISIBLE);
            redReturnRecordTv.setText(recordStatus);
        } else {
            headStatus.setText(recordStatus);
        }
        redCheckDetail.setOnClickListener(v -> ActivityHelper.toRedPacketDetailActivity(v.getContext(),mRedId));

        recordStatus = "进行中";
        if ("进行中".equals(recordStatus)) {
            btnBottomLl.setVisibility(View.VISIBLE);
            btnWithdrawl.setVisibility(View.GONE);
            btnRed.setVisibility(View.VISIBLE);
        } else {
            btnBottomLl.setVisibility(View.GONE);
        }
    }

    //行权
    private void initXingQuan() {
        headTitle.setText(name);
        headMoney.setText(money);
        headStatus.setText(recordStatus);
        xinquanPlaceTv.setText(mRecordType);
    }

    //充值或分销
    private void initRecharge(int type) {
        if (type == 1) {
            rechargeOrderTypeTv.setText("订单信息");
        } else {
            rechargeOrderTypeTv.setText("充值说明");
        }
        headTitle.setText(mRecordType);
        headMoney.setText(money);
        headStatus.setText(recordStatus);
        rechargeChargeTv.setText(mRecordType);
        rechargePoundrageTv.setText(freeRate);
        if (null != freeRate && !"".equals(freeRate) && Float.parseFloat(freeRate) > 0) {
            rechargePoundrageLl.setVisibility(View.VISIBLE);
        } else {
            rechargePoundrageLl.setVisibility(View.GONE);
        }
    }

    //交易
    private void initTrade() {
        headMoney.setText(money);
        if (null != freeRate && !"".equals(freeRate) && Float.parseFloat(freeRate) > 0) {
            tradePoundageLl.setVisibility(View.VISIBLE);
            tradePoundageTv.setText(freeRate);
        } else {
            tradePoundageLl.setVisibility(View.GONE);
        }
        headStatus.setText(recordStatus);
        headTitle.setText(name);
        tradePlaceTv.setText(name);
    }

    //提现
    private void initWithDraw() {
        headMoney.setText(money);
        withdrawalBankIdTv.setText(bankId);
        withdrawalTypeTv.setText(bankType);
        headTitle.setText(bankType);
        withdrawalPoundrageTv.setText(freeRate);
        headStatus.setText(recordStatus);
        //这个if 布局不会显示
        if (null != freeRate && !"".equals(freeRate) && Float.parseFloat(freeRate) > 0) {
            tradePoundageLl.setVisibility(View.VISIBLE);
        } else {
            tradePoundageLl.setVisibility(View.GONE);
        }
        recordStatus = "提现中";
        if ("提现中".equals(recordStatus)) {
            btnBottomLl.setVisibility(View.VISIBLE);
            btnWithdrawl.setVisibility(View.VISIBLE);
        } else {
            btnBottomLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        loadImage(picture);
        defaultTimeTv.setText(date);
    }

    private void loadImage(String picture) {
        Glide.with(this)
                .load(KConstant.img_url + picture)
                .error(R.mipmap.default_head_img)
                .transform(new CircleCrop())
                .into(headImage);
    }

    @OnClick({R.id.btn_withdrawl, R.id.btn_red})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_withdrawl:
                NativeDlg.create(this)
                        .msg("确定要取消提现吗?")
                        .msgTextSize(19)
                        .okClickListener("确定", ContextCompat.getColor(BaseApp.getInstance(), R.color.colorAccent), (dialog, v) -> {
                            presenter.reqCancel(id);
                            dialog.dismiss();
                        }).cancelClickListener("取消", (dialog, v) -> dialog.dismiss())
                        .show();
                break;
            case R.id.btn_red:
                share(mRedNote);
                break;
        }
    }

    @Override
    public void onCancelSuccess() {
        Tools.showToast("取消成功");
        SendRecvHelper.send(BaseApp.getInstance(), Actions.CANCEL_WITHDRAW_SUCCESS);
        finish();
    }

    private void share(String desc) {
        //微信地址
        String wxUrl = KConstant.RED_SHARE + mRedId + "&channel=2";
        //朋友圈地址
        String tmlineUrl = KConstant.RED_SHARE + mRedId + "&channel=4";

        String title = "邀请你一起买网红时间，获得带货分红";
        Share.share();
    }

    @Override
    protected AccountIncomeDetailContract.AccountIncomeDetailPresenter getPresenter() {
        return new AccountIncomeDetailPresenterImpl();
    }
}
