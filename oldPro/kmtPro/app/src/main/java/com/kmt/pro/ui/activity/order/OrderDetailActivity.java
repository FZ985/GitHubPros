package com.kmt.pro.ui.activity.order;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.order.OrderDetailbean;
import com.kmt.pro.event.ConfimOrderEvent;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.OrderContract;
import com.kmt.pro.mvp.presenter.OrderDetailPresenterImpl;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.dialog.NativeDlg;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.utils.Utils;
import com.lxbuytimes.kmtapp.glide.RoundedCornersTransformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-08-05 14:29
 **/
public class OrderDetailActivity extends BaseToolBarActivity<OrderContract.OrderDetailPresenter> implements OrderContract.OrderDetailView {
    @BindView(R.id.img_status)
    ImageView imgStatus;
    @BindView(R.id.order_status)
    TextView orderStatus;
    @BindView(R.id.order_detail_top_ll)
    LinearLayout orderDetailTopLl;
    @BindView(R.id.order_remind)
    TextView orderRemind;
    @BindView(R.id.weixinImg)
    ImageView weixinImg;
    @BindView(R.id.weixinName)
    TextView weixinName;
    @BindView(R.id.weixinCreate)
    TextView weixinCreate;
    @BindView(R.id.order_detail_l1)
    RelativeLayout orderDetailL1;
    @BindView(R.id.order_phone)
    TextView orderPhone;
    @BindView(R.id.weixinHao)
    TextView weixinHao;
    @BindView(R.id.weixinCode)
    ImageView weixinCode;
    @BindView(R.id.order_detail_l2)
    LinearLayout orderDetailL2;
    @BindView(R.id.order_pay_days)
    TextView orderPayDays;
    @BindView(R.id.house_worth)
    TextView houseWorth;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_date)
    TextView orderDate;
    @BindView(R.id.order_detail_l3)
    LinearLayout orderDetailL3;
    @BindView(R.id.order_cancle_deteal)
    TextView orderCancleDeteal;
    @BindView(R.id.order_contact)
    TextView orderContact;
    @BindView(R.id.order_pay_btn)
    Button orderPayBtn;
    private String bookId; // 订单号
    private String investorsCode; // 房子发行代码
    private boolean fromOrderList;
    private String cancle_detele_remind; // 取消或则删除的提示语
    private int cancle_detele; // 0:取消 1:删除
    private String weixinCodePath; // 维信二维码

    @Override
    public int getLayoutId() {
        bookId = getIntent().getStringExtra(KConstant.Key.order_id);
        fromOrderList = getIntent().getBooleanExtra(KConstant.Key.fromOrderList, false);
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("订单详情");
        toolbarColor(R.color.themeColor3);
        mToolbar.showWhiteBack(v -> finish());
    }

    @Override
    public void initData() {
        refresh();
    }

    @Override
    public void completeBookingSuccess() {
        refresh();
    }

    @Override
    public void onDetailData(OrderDetailbean data) {
        investorsCode = data.bookingInvestorsId;
        if ("1".equals(data.bookingStatus)) {
            //"等待支付"
            Glide.with(this).load(R.mipmap.icon_order_normal).into(imgStatus);
            orderStatus.setText("等待支付");
            orderRemind.setVisibility(View.VISIBLE);
            orderRemind.setText("请您在24小时之内完成支付，否则订单会自动取消");
            orderCancleDeteal.setVisibility(View.VISIBLE);
            orderCancleDeteal.setText(getString(R.string.cancle_order));
            orderPayBtn.setVisibility(View.VISIBLE);
            orderPayBtn.setText("支付");
            cancle_detele_remind = "确认取消兑换？";
            cancle_detele = 0;
            orderPayBtn.setOnClickListener(v -> ActivityHelper.toConfimOrderActivity(this, bookId, 1));
        } else if ("2".equals(data.bookingStatus)) {
            //待进群
            Glide.with(this).load(R.mipmap.icon_order_normal).into(imgStatus);
            orderStatus.setText("已支付");
            orderRemind.setVisibility(View.INVISIBLE);
            if (data.completionFlag) {
                // 完成入住
                orderCancleDeteal.setVisibility(View.GONE);
                orderPayBtn.setVisibility(View.VISIBLE);
                orderPayBtn.setText("确认完成");
                orderPayBtn.setOnClickListener(v -> presenter.completeBooking(bookId));
            } else {
                orderCancleDeteal.setVisibility(View.VISIBLE);
                orderCancleDeteal.setText(getString(R.string.cancle_order));
                orderPayBtn.setVisibility(View.GONE);
                cancle_detele_remind = "确认取消兑换？";
                cancle_detele = 0;
            }
        } else if ("3".equals(data.bookingStatus)) {
            // 交易完成待评价
            Glide.with(this).load(R.mipmap.icon_order_normal).into(imgStatus);
            orderStatus.setText("交易成功");
            orderRemind.setVisibility(View.INVISIBLE);
            orderCancleDeteal.setVisibility(View.VISIBLE);
            orderCancleDeteal.setText(getString(R.string.detele_order));
            orderPayBtn.setVisibility(View.VISIBLE);
            orderPayBtn.setText("评价");
            cancle_detele_remind = "确认删除兑换？";
            cancle_detele = 1;
            orderPayBtn.setOnClickListener(v -> ActivityHelper.toOrderEvaluateActivity(this, bookId, investorsCode, data.houseName));
        } else if ("4".equals(data.bookingStatus)) {
            //交易关闭已评价
            Glide.with(this).load(R.mipmap.icon_order_normal).into(imgStatus);
            orderStatus.setText("交易关闭");
            orderRemind.setVisibility(View.INVISIBLE);
            orderCancleDeteal.setVisibility(View.VISIBLE);
            orderCancleDeteal.setText(getString(R.string.detele_order));
            orderPayBtn.setVisibility(View.GONE);
            cancle_detele_remind = "确认删除兑换？";
            cancle_detele = 1;
        } else if ("31".equals(data.bookingStatus)
                || "33".equals(data.bookingStatus)) {
            Glide.with(this).load(R.mipmap.icon_order_cancle).into(imgStatus);
            orderStatus.setText("已取消");
            orderRemind.setVisibility(View.VISIBLE);
            orderRemind.setText("用户取消订单");
            orderCancleDeteal.setVisibility(View.VISIBLE);
            orderCancleDeteal.setText(getString(R.string.detele_order));
            orderPayBtn.setVisibility(View.GONE);
            cancle_detele_remind = "确认删除兑换？";
            cancle_detele = 1;
        } else if ("35".equals(data.bookingStatus)) {
            Glide.with(this).load(R.mipmap.icon_order_cancle).into(imgStatus);
            orderStatus.setText("待退款");
            orderRemind.setVisibility(View.VISIBLE);
            orderRemind.setText("订单已取消，等待退款");
            orderCancleDeteal.setVisibility(View.GONE);
            orderPayBtn.setVisibility(View.GONE);
        } else {
            orderPayBtn.setVisibility(View.GONE);
        }

        //发行人信息
        Glide.with(BaseApp.getInstance())
                .load(KConstant.img_url + data.houseAvatar)
                .placeholder(R.mipmap.icon_default_rect_headimg)
                .error(R.mipmap.icon_default_rect_headimg)
                .transform(new CenterCrop(), new RoundedCornersTransformation(Utils.dip2px(BaseApp.getInstance(), 20), 0,
                        RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_RIGHT))
                .into(weixinImg);
        weixinName.setText(data.houseName);
        weixinCreate.setText(data.createUserName);
        orderPayDays.setText(data.bookingTimesSecondsLong + getString(R.string.trade_unit));
        houseWorth.setText(data.bookingTimeValue + "GBT");
        orderNumber.setText(data.bookingOrderNo);
        orderDate.setText(data.bookingAuditingTime);
        orderPhone.setText(data.userTel);
        weixinHao.setText(data.wxNum);
        weixinCodePath = data.wxImg;
        Glide.with(BaseApp.getInstance()).load(KConstant.img_url + weixinCodePath)
                .placeholder(R.mipmap.icon_weixin_code).error(R.mipmap.icon_weixin_code)
                .into(weixinCode);
        //取消预约或则删除
        //根据cancle_detele值判断是否是取消还是删除 0是取消 1是删除
        //cancle_detele_remind 取消或则删除的提示语
        orderCancleDeteal.setOnClickListener(v -> {
            NativeDlg.create(this)
                    .msg(cancle_detele_remind)
                    .msgTextSize(17)
                    .cancelClickListener("取消", (dialog, v1) -> dialog.dismiss())
                    .okClickListener("确定", ContextCompat.getColor(BaseApp.getInstance(), R.color.colorAccent), (dialog, v12) -> presenter.cancleBook(bookId, cancle_detele + "")).show();
        });
    }

    @Override
    public void onCancelOrder(int type) {
        if (type == 0) {
            Tools.showToast("取消兑换成功");
            completeBookingSuccess();
        } else if (type == 1) {
            Tools.showToast("删除兑换成功");
            if (fromOrderList) {
                // 从订单列表进入的
                EventBus.getDefault().post(new ConfimOrderEvent(2));
                finish();
            } else {
                ActivityHelper.toExchangeOrderActivity(this);
                finish();
            }
        }
    }

    @OnClick({R.id.weixinCode, R.id.order_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.weixinCode:
                ActivityHelper.toBigPricturePreviewActivity(this, weixinCodePath);
                break;
            case R.id.order_contact:
                ActivityHelper.toKeFu(this);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfimOrderEvent(ConfimOrderEvent event) {
        Logger.e("====提交订单回调:" + event.getType());
        if (event.getType() == 1 && !isFinishing()) {
            refresh();
        }
    }

    private void refresh() {
        if (!TextUtils.isEmpty(bookId)) {
            presenter.queryBookingDetails(bookId);
        }
    }

    @Override
    protected OrderContract.OrderDetailPresenter getPresenter() {
        return new OrderDetailPresenterImpl();
    }
}
