package com.kmt.pro.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.ProductBean;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.utils.DateUtils;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
import com.kmtlibs.app.utils.Utils;
import com.lxbuytimes.kmtapp.glide.RoundedCornersTransformation;
import com.lxbuytimes.kmtapp.span.Span;

import androidx.core.content.ContextCompat;

/**
 * Create by JFZ
 * date: 2020-07-01 16:00
 **/
public class PriceListAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, ProductBean item) {
        ImageView house_avatar = helper.getView(R.id.price_avatar);
        TextView house_name = helper.getView(R.id.house_name);
        TextView house_description = helper.getView(R.id.house_description);
        TextView house_new_price = helper.getView(R.id.house_new_price);
        TextView updown_ratio = helper.getView(R.id.updown_ratio);
        TextView publish_date = helper.getView(R.id.house_publish);
        TextView house_create_person = helper.getView(R.id.house_create_person);
        TextView price_suffix = helper.getView(R.id.price_suffix);
        ImageView home_list_arrow = helper.getView(R.id.home_list_arrow);

        Glide.with(BaseApp.getInstance())
                .load(KConstant.img_url + item.investorsAvatar)
                .placeholder(R.mipmap.icon_default_rect_headimg)
                .error(R.mipmap.icon_default_rect_headimg)
                .transform(new CenterCrop(),new RoundedCornersTransformation(Utils.dip2px(BaseApp.getInstance(), 20), 0,
                        RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_RIGHT))
                .into(house_avatar);

        house_name.setText(item.investorsName);//房子名称
        house_description.setText(item.houseDescription);//房子名称
        house_create_person.setText(item.createUserName);// 创办人
        house_create_person.setTextColor(Color.parseColor("#8E8EA1"));
        house_new_price.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.black33));
        publish_date.setVisibility(View.GONE);
        home_list_arrow.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(item.suffix)) {
            price_suffix.setText(item.suffix);
            price_suffix.setVisibility(View.VISIBLE);
            if (item.suffix.equals("涨停")) {
                price_suffix.setBackgroundResource(R.drawable.shape_price_zhangting);
            } else if (item.suffix.equals("跌停")) {
                price_suffix.setBackgroundResource(R.drawable.shape_price_dieting);
            } else {
                price_suffix.setVisibility(View.GONE);
            }
        } else {
            price_suffix.setVisibility(View.GONE);
        }
        if (item.investorsStatus == 0) {
            if (item.presellFlag == 1) {
                house_create_person.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.ff9500));
                updown_ratio.setText(BaseApp.getInstance().getString(R.string.wait_subscription));
                house_create_person.setText(DateUtils.getDateFormat(item.investorsPresellDate) + "开抢");
                updown_ratio.setTextColor(Color.parseColor("#FF7D00"));
            } else {
                updown_ratio.setText(BaseApp.getInstance().getString(R.string.immediately_subscription));
                publish_date.setVisibility(View.VISIBLE);
                publish_date.setText("开盘日期:" + item.investorsPublishDate);
                updown_ratio.setTextColor(ContextCompat.getColor(BaseApp.getInstance(), R.color.ff9500));
            }
            Span.impl()
                    .append(Span.builder(item.investorsFixPrice).textSize(17))
                    .append(Span.builder("GBT").textSize(13))
                    .into(house_new_price);
        } else if (item.investorsStatus == 2 || item.investorsStatus == 5) {
            home_list_arrow.setVisibility(View.VISIBLE);

            Span.impl()
                    .append(Span.builder(item.newOrderPrice).textSize(17))
                    .append(Span.builder("GBT").textSize(13))
                    .into(house_new_price);
            updown_ratio.setText(item.uplowPrice);//涨跌百分比
            if (item.uplowStatus == 2) {
                // 下跌
                house_new_price.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.v3_homelist_price));
                updown_ratio.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.v3_homelist_price_down));
                home_list_arrow.setImageResource(R.mipmap.v3_arrow_down);
            } else {
                //涨或则平
                house_new_price.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.v3_homelist_price));
                updown_ratio.setTextColor(BaseApp.getInstance().getResources().getColor(R.color.v3_homelist_ratio));
                home_list_arrow.setImageResource(R.mipmap.v3_arrow_up);
            }
        }
    }

    public PriceListAdapter() {
        super(R.layout.item_price_list);
    }
}
