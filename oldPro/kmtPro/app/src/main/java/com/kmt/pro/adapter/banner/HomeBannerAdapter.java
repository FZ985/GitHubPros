package com.kmt.pro.adapter.banner;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kmt.pro.R;
import com.kmt.pro.bean.HomeBannerBean;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，图片
 */
public class HomeBannerAdapter extends BannerAdapter<HomeBannerBean.BannerBean, HomeBannerImageHolder> {

    public HomeBannerAdapter(List<HomeBannerBean.BannerBean> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
    }

    //更新数据
    public void updateData(List<HomeBannerBean.BannerBean> data) {
        //这里的代码自己发挥，比如如下的写法等等
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public HomeBannerImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new HomeBannerImageHolder(imageView);
    }

    @Override
    public void onBindView(HomeBannerImageHolder holder, HomeBannerBean.BannerBean data, int position, int size) {
        Glide.with(holder.imageView.getContext())
                .load(KConstant.img_url + data.imageUrl)
                .placeholder(R.mipmap.fail_image)
                .error(R.mipmap.fail_image)
                .into(holder.imageView);
    }

}
