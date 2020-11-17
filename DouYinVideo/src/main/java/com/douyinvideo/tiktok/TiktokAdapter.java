package com.douyinvideo.tiktok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.douyinvideo.BaseApp;
import com.douyinvideo.R;
import com.douyinvideo.Urls;
import com.douyinvideo.tiktok.ijk.JZMediaIjk;
import com.douyinvideo.tiktok.view.JzvdStdTikTok;

import java.util.List;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;

public class TiktokAdapter extends RecyclerView.Adapter<TiktokAdapter.TikTokViewHolder> {
    private List<String> list;

    public TiktokAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TikTokViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TikTokViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiktok, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TikTokViewHolder holder, int position) {
        //这个缓存下一个
        if (holder.getLayoutPosition() + 1 < getItemCount()) {
            //缓存下一个 10秒
            BaseApp.getProxy(BaseApp.getInstance()).preLoad(list.get(holder.getLayoutPosition() + 1), 10);
        }

        JZDataSource jzDataSource = new JZDataSource(BaseApp.getProxy(BaseApp.getInstance()).getProxyUrl(list.get(position)));
        jzDataSource.looping = true;
        //不保存播放进度
        Jzvd.SAVE_PROGRESS = false;
        //取消播放时在非WIFIDialog提示
        Jzvd.WIFI_TIP_DIALOG_SHOWED = true;
        holder.video.setUp(jzDataSource, Jzvd.SCREEN_NORMAL, JZMediaIjk.class);
        Glide.with(holder.video.getContext())
                .load(Urls.image)
                .into(holder.video.posterImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TikTokViewHolder extends RecyclerView.ViewHolder {
        public JzvdStdTikTok video;

        public TikTokViewHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.videoplayer);
        }
    }
}
