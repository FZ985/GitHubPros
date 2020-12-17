package com.kmt.pro.adapter.coin;

import android.widget.ImageView;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;

/**
 * Create by JFZ
 * date: 2020-04-15 19:13
 **/
public class QrcodeAddressAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView item_address_select = helper.getView(R.id.item_address_select);
        TextView item_address_tv = helper.getView(R.id.item_address_tv);

        int pos = helper.getAdapterPosition();

        if (pos == 0) {
            item_address_select.setImageResource(R.mipmap.icon_address_select);
        } else {
            item_address_select.setImageResource(R.mipmap.icon_address_normal);
        }
    }

    public QrcodeAddressAdapter() {
        super(R.layout.item_qrcode_address, new ArrayList<String>());
    }
}
