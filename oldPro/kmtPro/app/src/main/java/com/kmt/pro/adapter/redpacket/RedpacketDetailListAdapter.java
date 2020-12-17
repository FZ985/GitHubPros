package com.kmt.pro.adapter.redpacket;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
import com.kmtlibs.app.utils.Utils;

import java.util.ArrayList;

/**
 * Create by JFZ
 * date: 2020-07-28 10:14
 **/
public class RedpacketDetailListAdapter extends BaseQuickAdapter<RedDetailListBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder holder, RedDetailListBean item) {
        holder.setText(R.id.reditem_date, item.stateTime);
        holder.setText(R.id.reditem_username, item.userName);
        holder.setText(R.id.reditem_time, item.amount + BaseApp.getInstance().getString(R.string.trade_unit));
        if (null != item.isBest && "1".equals(item.isBest)) {
            holder.setVisible(R.id.reditem_mostluck, true);
        } else {
            holder.setVisible(R.id.reditem_mostluck, false);
        }
        TextView state = holder.getView(R.id.reditem_state);
        if (null != item.state && !"".equals(item.state)) {
            if ("2".equals(item.state)) {
                holder.setGone(R.id.reditem_mostluck, false);
                state.setVisibility(View.VISIBLE);
                state.setText("已领完");
            } else if ("3".equals(item.state)) {
                holder.setGone(R.id.reditem_mostluck, false);
                state.setVisibility(View.VISIBLE);
                state.setText("已过期");
            } else if ("1".equals(item.state)) {
                holder.setGone(R.id.reditem_mostluck, false);
                state.setVisibility(View.VISIBLE);
                state.setText("进行中");
            }
        } else {
            state.setVisibility(View.GONE);
        }
        ImageView reditem_userhead = holder.getView(R.id.reditem_userhead);
        Glide.with(BaseApp.getInstance())
                .load(KConstant.img_url + item.userAvatar)
                .placeholder(R.mipmap.icon_default_rect_headimg)
                .error(R.mipmap.icon_default_rect_headimg)
                .transform(new CenterCrop(), new RoundedCorners(Utils.dip2px(BaseApp.getInstance(), 2)))
                .into(reditem_userhead);
    }

    public RedpacketDetailListAdapter() {
        super(R.layout.item_redpacket_detail, new ArrayList<>());
    }
}
