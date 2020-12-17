package com.kmt.pro.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.bean.AccountIncomeBean;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;

/**
 * Create by JFZ
 * date: 2020-07-24 11:08
 **/
public class AccountIncomeAdapter extends BaseQuickAdapter<AccountIncomeBean, BaseViewHolder> {
    public AccountIncomeAdapter() {
        super(R.layout.item_account_income, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder holder, AccountIncomeBean item) {
        holder.setText(R.id.bill_record_time, item.getRecordTradeDate());
        holder.setText(R.id.bill_record_trade, item.getRecordType());
        holder.setText(R.id.bill_record_price, item.getRecordTradeMoney());
        TextView stausTv = holder.getView(R.id.bill_record_status);
        stausTv.setText(item.getRecordStatus());
        if (item.getRecordStatus().startsWith("退还")) {
            stausTv.setTextColor(context.getResources().getColor(R.color.red_ff5555));
        } else {
            stausTv.setTextColor(Color.parseColor("#7F8394"));
        }
    }
}
