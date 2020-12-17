package com.kmt.pro.adapter.order;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.order.ExchangeOrderBean;
import com.kmt.pro.callback.ExCahangeOrderClickCall;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
import com.kmtlibs.app.utils.Utils;
import com.lxbuytimes.kmtapp.glide.RoundedCornersTransformation;

/**
 * Create by JFZ
 * date: 2020-07-29 10:20
 **/
public class ExchangeOrderListAdapter extends BaseQuickAdapter<ExchangeOrderBean, BaseViewHolder> {
    private ExCahangeOrderClickCall call;

    @Override
    protected void convert(BaseViewHolder holder, ExchangeOrderBean item) {
        TextView deleteBtn = holder.getView(R.id.orider_cancle_delete);
        TextView finishBtn = holder.getView(R.id.orider_pay_finish_evaluate);

        TextView order_number = holder.getView(R.id.order_number);
        TextView order_status = holder.getView(R.id.order_status);
        TextView order_weixin_name = holder.getView(R.id.order_weixin_name);
        TextView order_weixin_create = holder.getView(R.id.order_weixin_create);
        TextView order_total_time = holder.getView(R.id.order_total_time);

        order_number.setText(item.bookingOrderNo);
        order_weixin_name.setText(item.houseName);
        order_weixin_create.setText(item.createUserName);
        order_total_time.setText(item.bookingTimesSecondsLong + "份");

        ImageView order_house_iv = holder.getView(R.id.order_house_iv);
        Glide.with(BaseApp.getInstance())
                .load(KConstant.img_url + item.houseAvatar)
                .placeholder(R.mipmap.icon_default_rect_headimg)
                .error(R.mipmap.icon_default_rect_headimg)
                .transform(new CenterCrop(), new RoundedCornersTransformation(Utils.dip2px(BaseApp.getInstance(), 20), 0,
                        RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_RIGHT))
                .into(order_house_iv);

        if (item.bookingStatus.equals("1")) {
            order_status.setText("等待支付");
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setText("取消兑换");
            finishBtn.setVisibility(View.VISIBLE);
            finishBtn.setText("支付");
            finishBtn.setOnClickListener(v -> {
                if (call != null) {
                    call.status1(item.bookingId, 2);
                }
            });
        } else if (item.bookingStatus.equals("2")) {
            order_status.setText("已支付");
            if (item.completionFlag) {
                finishBtn.setVisibility(View.VISIBLE);
                finishBtn.setText("确认完成");
                finishBtn.setOnClickListener(v -> {
                    if (call != null) {
                        call.status2(item.bookingId);
                    }
                });
            } else {
                deleteBtn.setVisibility(View.VISIBLE);
                deleteBtn.setText("取消兑换");
            }
        } else if (item.bookingStatus.equals("3")) {
            order_status.setText("交易成功");
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setText("删除订单");
            finishBtn.setVisibility(View.VISIBLE);
            finishBtn.setText("评价");
            finishBtn.setOnClickListener(v -> {
                if (call != null)
                    call.status3(item.bookingId, item.bookingInvestorsId, item.houseName);
            });
        } else if (item.bookingStatus.equals("4")) {
            order_status.setText("交易关闭");
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setText("删除订单");
        } else if (item.bookingStatus.equals("31") || item.bookingStatus.equals("33")) {
            order_status.setText("已取消");
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setText("删除订单");
        } else if (item.bookingStatus.equals("35")) {
            order_status.setText("待退款");
        } else {
            order_status.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }

        //取消预约或则删除
        //根据cancle_detele值判断是否是取消还是删除 0是取消 1是删除
        //cancle_detele_remind 取消或则删除的提示语
        deleteBtn.setOnClickListener(v -> {
            String message = "";
            String delType = "";
            if ("1".equals(item.bookingStatus)) {
                message = "确认取消兑换？";
                delType = "0";
            } else if ("2".equals(item.bookingStatus)) {
                // 24小时之内的退款提示
                message = item.flag ? "确认取消兑换？\n当前取消兑换订单将不予退款" : "确认取消兑换？";
                delType = "0";
            } else if ("3".equals(item.bookingStatus) || "4".equals(item.bookingStatus) || "31".equals(item.bookingStatus)
                    || "33".equals(item.bookingStatus)) {
                message = "确认删除兑换？";
                delType = "1";
            }
            if (call != null) {
                call.showDialog(message, delType,item.bookingId);
            }
        });

    }

    public ExchangeOrderListAdapter(ExCahangeOrderClickCall call) {
        super(R.layout.item_exchange_order);
        this.call = call;
    }
}
