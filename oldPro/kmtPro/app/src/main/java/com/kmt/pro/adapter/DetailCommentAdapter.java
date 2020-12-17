package com.kmt.pro.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.kmt.pro.R;
import com.kmt.pro.bean.detail.CommentBean;
import com.kmt.pro.helper.KConstant;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;
import com.kmtlibs.app.ActivityManager;
import com.kmtlibs.app.widget.IGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jiang.photo.picker.Photo;

/**
 * Create by JFZ
 * date: 2020-07-02 17:07
 **/
public class DetailCommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder holder, CommentBean item) {
        ImageView commentHead = holder.getView(R.id.comment_head);
        Glide.with(commentHead.getContext())
                .load(KConstant.img_url + item.headURL)
                .placeholder(R.mipmap.default_head_img)
                .error(R.mipmap.default_head_img)
                .transform(new CircleCrop())
                .into(commentHead);
        //图片
        IGridView gridview = holder.getView(R.id.comment_gridview);
        if (!TextUtils.isEmpty(item.images)) {
            gridview.setVisibility(View.VISIBLE);
            final List<String> photos = Arrays.asList(item.images.split(","));
            final ArrayList<String> urls = new ArrayList<>();
            gridview.setAdapter(new FriendsImgAdapter(photos));
            gridview.setOnItemClickListener((adapterView, view, pos, l) -> {
//                //点击浏览图片
                Photo.with().defaultPreview(ActivityManager.getAppInstance().currentActivity(), urls, pos);
            });
            for (String url : photos) {
                urls.add(KConstant.img_url + url);
            }
        } else {
            gridview.setVisibility(View.GONE);
        }
        holder.setText(R.id.comment_content, item.content);
        holder.setText(R.id.comment_time, item.postTime);
        holder.setText(R.id.comment_username, item.userName);
    }

    public DetailCommentAdapter() {
        super(R.layout.item_detail_comment);
    }
}
