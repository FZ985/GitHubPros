package com.kmt.pro.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kmt.pro.R;
import com.kmt.pro.utils.chinese2pinyin.CityItem;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
import com.lxbuytimes.kmtapp.sortapi.BaseStickyHeaderAdapter;
import com.lxbuytimes.kmtapp.sortapi.callback.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * Create by JFZ
 * date: 2020-07-06 17:14
 **/
public class CountryCodeAdapter extends BaseStickyHeaderAdapter<CityItem, BaseViewHolder> implements StickyRecyclerHeadersAdapter<BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder holder, CityItem item) {
        holder.setText(R.id.cityName, item.getDisplayInfo() + " +" + item.getCode());
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
        CityItem item = getItem(position);
        holder.setText(R.id.decoration_head, item.getLetter());
    }

    public CountryCodeAdapter() {
        super(R.layout.item_country_code, new ArrayList<>());
    }

    public int getIndex(String str) {
        if (data == null) return -1;
        for (int i = 0; i < data.size(); i++) {
            CityItem item = getItem(i);
            String substring = item.getLetter();
            if (substring.toLowerCase().equals(str.toLowerCase()))
                return i;
        }
        return -1;
    }
}
