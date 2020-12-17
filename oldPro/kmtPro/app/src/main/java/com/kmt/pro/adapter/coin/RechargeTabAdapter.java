package com.kmt.pro.adapter.coin;

import android.graphics.Color;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.bean.coin.SymbolRespBean;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;


/**
 * Create by JFZ
 * date: 2020-04-15 13:49
 **/
public class RechargeTabAdapter extends BaseQuickAdapter<SymbolRespBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, SymbolRespBean item) {
        TextView recharge_tab = helper.getView(R.id.recharge_tab);
        if (item.check) {
            recharge_tab.setBackgroundResource(R.drawable.shape_recharge_choose_money);
            recharge_tab.setTextColor(Color.parseColor("#2569BE"));
        } else {
            recharge_tab.setBackgroundResource(R.drawable.shape_recharge_choose_money_normal);
            recharge_tab.setTextColor(Color.parseColor("#A1ADB7"));
        }
        recharge_tab.setText(item.tab);
    }

    public RechargeTabAdapter() {
        super(R.layout.item_recharge_tabs, new ArrayList<SymbolRespBean>());
    }

    public void check(int pos) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (i == pos) {
                    data.get(i).check = true;
                } else {
                    data.get(i).check = false;
                }
            }
            notifyDataSetChanged();
        }
    }

    public SymbolRespBean getCheckItem() {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).check) {
                    return data.get(i);
                }
            }
        }
        return null;
    }
}
