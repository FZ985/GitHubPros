package com.kmt.pro.adapter.coin;

import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.bean.coin.CoinRecordBean;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;

/**
 * Create by JFZ
 * date: 2020-07-23 14:32
 **/
public class CoinRecordListAdapter extends BaseQuickAdapter<CoinRecordBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder holder, CoinRecordBean item) {
        TextView item_record_num = holder.getView(R.id.item_record_num);
        TextView item_record_status = holder.getView(R.id.item_record_status);
        TextView item_record_date = holder.getView(R.id.item_record_date);
        item_record_num.setText(item.getValue());
        if (type == 1) {
            if (item.status == 0) {
                item_record_status.setText("确认中");
            } else if (item.status == 1) {
                item_record_status.setText("已完成");
            } else {
                item_record_status.setText("失败 ");
            }
        } else {
            if (item.status == 0) {
                item_record_status.setText("待审核");
            } else if (item.status == 1) {
                item_record_status.setText("确认中");
            } else if (item.status == 2) {
                item_record_status.setText("转账成功");
            } else if (item.status == 3) {
                item_record_status.setText("转账失败");
            } else {
                item_record_status.setText("失败 ");
            }
        }
        item_record_date.setText(item.created_at);
    }
    private int type;
    public CoinRecordListAdapter(int type) {
        super(R.layout.item_coin_record, new ArrayList<CoinRecordBean>());
        this.type = type;
    }
}
