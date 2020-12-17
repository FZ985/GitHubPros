package com.kmt.pro.adapter.coin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kmt.pro.R;
import com.kmt.pro.bean.coin.RechargeSearchBean;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
import com.lxbuytimes.kmtapp.sortapi.BaseStickyHeaderAdapter;
import com.lxbuytimes.kmtapp.sortapi.callback.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * Create by JFZ
 * date: 2020-07-22 18:08
 **/
public class CoinListAdapter extends BaseStickyHeaderAdapter<RechargeSearchBean, BaseViewHolder> implements StickyRecyclerHeadersAdapter<BaseViewHolder> {
    public CoinListAdapter() {
        super(R.layout.item_thecoin_list, new ArrayList<RechargeSearchBean>());
    }

    @Override
    protected void convert(BaseViewHolder holder, RechargeSearchBean item) {
        holder.setText(R.id.item_thecoin_tv, item.tokenSymbol + "");
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getFullName().charAt(0);
    }

    @Override
    public BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.decoration_country, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int position) {
        RechargeSearchBean item = getItem(position);
        holder.setText(R.id.decoration_head, item.getLetter());
    }

    public int getIndex(String str) {
        if (data == null) return -1;
        for (int i = 0; i < data.size(); i++) {
            RechargeSearchBean item = getItem(i);
            String substring = item.getLetter();
            if (substring.toLowerCase().equals(str.toLowerCase()))
                return i;
        }
        return -1;
    }
}
