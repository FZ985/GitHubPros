package com.kmt.pro.adapter;

import android.content.Context;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.widget.marqueeview.base.CommonAdapter;
import com.kmt.pro.widget.marqueeview.base.ViewHolder;

import java.util.List;


/**
 * Created by JFZ
 * on 2020/2/12.
 */

public class MineNoticeAdapter extends CommonAdapter<String> {

    public MineNoticeAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_notice_one, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        TextView tv = viewHolder.getView(R.id.notice_msg);
        tv.setText(item + "");
    }
}
