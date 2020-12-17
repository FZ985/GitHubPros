package com.kmt.pro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.kmt.pro.R;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.app.utils.Utils;

import java.util.List;

/**
 * Create by JFZ
 * date: 2020-07-02 18:24
 **/
public class FriendsImgAdapter extends BaseAdapter {

    private List<String> ls;

    public FriendsImgAdapter(List<String> ls) {
        super();
        this.ls = ls;
    }

    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public Object getItem(int position) {
        return ls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nine_image, null);
            holder.img = (ImageView) view.findViewById(R.id.item_nine_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(holder.img.getContext())
                .load(KConstant.img_url + ls.get(position))
                .placeholder(R.mipmap.fail_image)
                .error(R.mipmap.fail_image)
                .transform(new CenterCrop(), new RoundedCorners(Utils.dip2px(holder.img.getContext(), 2)))
                .into(holder.img);
        return view;
    }

    class ViewHolder {
        ImageView img;
    }
}
