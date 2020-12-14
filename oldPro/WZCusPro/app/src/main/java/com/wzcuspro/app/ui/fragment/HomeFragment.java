package com.wzcuspro.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.wzcuspro.R;
import com.wzcuspro.app.base.BaseFragment;
import com.wzcuspro.app.glide.GlideApp;
import com.wzcuspro.app.ui.preview.PreviewImageActivity;
import com.wzcuspro.app.utils.Tools;

import java.util.ArrayList;

import weiying.customlib.recycle.adapter.BaseQuickAdapter;
import weiying.customlib.recycle.adapter.BaseViewHolder;
import weiying.customlib.recycle.adapter.listener.OnItemClickListener;

public class HomeFragment extends BaseFragment {
    private RecyclerView homeRecycler;
    private HomeAdapter adapter;

    @Override
    public void initView() {
        homeRecycler = findViewById(R.id.home_frag_recycle);
        homeRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        homeRecycler.setAdapter(adapter = new HomeAdapter());
        homeRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                PreviewImageActivity.jump(mAct, position, adapter.getDatas());
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int layoutResId() {
        return R.layout.fragment_home;
    }


    public static class HomeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public HomeAdapter() {
            super(R.layout.zztst_item_home, new ArrayList<>());
            setNewData(getDatas());
        }

        public ArrayList<String> getDatas() {
            ArrayList<String> list = new ArrayList<>();
            list.add("http://attach.bbs.miui.com/forum/201807/17/154537ujxutueesyj3mzt0.jpg");
            list.add("https://wx2.sinaimg.cn/large/866a67c7gy1fkaw7ewstng20b208hhdx.gif");
            list.add("http://pic1.win4000.com/wallpaper/0/53b4b747b9094.jpg");
            list.add("http://img008.hc360.cn/hb/MTQ2ODA4MDczNTI5NjIwODEwMzg5Mjk=.jpg");
            list.add("http://pic1.win4000.com/wallpaper/c/545088304042f.jpg");
            return list;
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ImageView home_item_iv = helper.getView(R.id.home_item_iv);
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .override(Tools.getScreenWidth(mContext), Tools.dip2px(mContext, 220));
            GlideApp.with(mContext).asBitmap().load(item).transition(BitmapTransitionOptions.withCrossFade()).apply(options).into(home_item_iv);
        }
    }
}
