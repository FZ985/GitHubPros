package com.kmt.pro.ui.activity.redpacket;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.kmt.pro.R;
import com.kmt.pro.adapter.redpacket.RedpacketDetailListAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.bean.redpacket.RedPacketRecordBean;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.SelfRedRecordContract;
import com.kmt.pro.mvp.presenter.SelfRedRecordPresneterImpl;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.ui.dialog.ActionSheetDialog;
import com.kmtlibs.adapter.base.listener.OnLoadMoreListener;
import com.kmtlibs.app.utils.Utils;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by JFZ
 * date: 2020-07-28 16:05
 **/
public class SelfRedRecordActivity extends BaseToolBarActivity<SelfRedRecordContract.SelfRedRecordPresenter> implements OnLoadMoreListener, SelfRedRecordContract.SelfRedRecordView {
    @BindView(R.id.redrecord_recycler)
    RecyclerView redrecordRecycler;
    private int page;
    private String type = "2";//默认为2, 1,发出的/2，收到的
    private RedpacketDetailListAdapter adapter;
    private View head, emptyHead;
    private ViewHolder holder;

    @Override
    public int getLayoutId() {
        return R.layout.activity_selfred_record;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("收到的红包");
        toolbarColor(R.color.red_ff5555);
        mToolbar.title.setTextColor(Color.parseColor("#fff2b6"));
        mToolbar.setLeftDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_red_close));
        mToolbar.setRightDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_red_more));
        mToolbar.right.setOnClickListener(v -> showMore());
        redrecordRecycler.setLayoutManager(new LinearLayoutManager(this));
        head = LayoutInflater.from(this).inflate(R.layout.head_redpacket_record, null);
        emptyHead = LayoutInflater.from(this).inflate(R.layout.layout_empty1, null);
        emptyHead.setPadding(0,Utils.dip2px(this,60),0,0);
        holder = new ViewHolder(head);
        adapter = new RedpacketDetailListAdapter();
        adapter.setHeaderView(head);
        adapter.setHeaderWithEmptyEnable(true);
        redrecordRecycler.setAdapter(adapter);
        holder.redrecordUsername.setText(UserSp.get().getNickName());
        Glide.with(this).load(KConstant.img_url + UserSp.get().getHeadImg())
                .placeholder(R.mipmap.icon_default_rect_headimg).error(R.mipmap.icon_default_rect_headimg)
                .transform(new CenterCrop(), new RoundedCorners(Utils.dip2px(BaseApp.getInstance(), 2)))
                .into(holder.redrecordHead);
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            try {
                RedDetailListBean item = adapter.getItem(position);
                if (item != null && !TextUtils.isEmpty(item.envelopId)) {
                    ActivityHelper.toRedPacketDetailActivity(this, item.envelopId);
                }
            } catch (Exception e) {

            }
        });
    }

    @Override
    public void initData() {
        page = 1;
        request(page, type);
    }

    private void request(int page, String type) {
        presenter.reqHead(type);
        requestlist(page, type);
    }

    private void requestlist(int page, String type) {
        if (type.equals("2")) {
            presenter.reqGetList(page);
        } else {
            presenter.reqSendList(page);
        }
    }

    @Override
    public void onHeadInfo(RedPacketRecordBean data) {
        if (isFinishing() || data == null) return;
        if ("2".equals(type)) {
            holder.redrecordMygrayLl.setVisibility(View.VISIBLE);
            holder.sendRedNum.setVisibility(View.GONE);
            holder.redrecordOpenedTv.setText(data.sum);
            holder.redcoredGrayRedNum.setText(data.times);
            holder.grayRedLucknum.setText(null != data.best ? data.best : "0");
        } else {
            holder.redrecordMygrayLl.setVisibility(View.GONE);
            holder.sendRedNum.setVisibility(View.VISIBLE);
            holder.redrecordOpenedTv.setText(data.sum);
            holder.sendRedNum.setText("发出红包" + data.times + "个");
        }
    }

    @Override
    public void onRedListSucc(int page, List<RedDetailListBean> datas) {
        if (isFinishing()) return;
        if (page == 1) {
            adapter.removeHeaderView(emptyHead);
            if (datas.size() > 0) {
                adapter.setNewInstance(datas);
                adapter.getLoadMoreModule().setOnLoadMoreListener(this);
            } else {
                adapter.addHeaderView(emptyHead);
            }
        } else {
            if (datas.size() > 0) {
                adapter.addData(datas);
                adapter.getLoadMoreModule().loadMoreComplete();
            } else adapter.getLoadMoreModule().loadMoreEnd(true);
        }
    }

    @Override
    public void onRedListErr(int page, String err) {
        if (isFinishing()) return;
        if (page == 1) {
            adapter.addHeaderView(emptyHead);
        } else {
            adapter.removeHeaderView(emptyHead);
            adapter.getLoadMoreModule().loadMoreEnd(true);
        }
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(() -> {
            if (!isFinishing()) {
                page = page + 1;
                requestlist(page, type);
            }
        }, 250);
    }

    private void showMore() {
        new ActionSheetDialog(this).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("我收到的红包", ActionSheetDialog.SheetItemColor.Blue, which -> {
                    type = "2";
                    initData();
                    mToolbar.title.setText("收到的红包");
                })
                .addSheetItem("我发出的红包", ActionSheetDialog.SheetItemColor.Blue, which -> {
                    type = "1";
                    mToolbar.title.setText("发出的红包");
                    initData();
                }).show();
    }

    static class ViewHolder {
        @BindView(R.id.redrecord_head)
        ImageView redrecordHead;
        @BindView(R.id.redrecord_username)
        TextView redrecordUsername;
        @BindView(R.id.redrecord_opened_tv)
        TextView redrecordOpenedTv;
        @BindView(R.id.redcored_gray_red_num)
        TextView redcoredGrayRedNum;
        @BindView(R.id.redcored_gray_red_lucknum)
        TextView grayRedLucknum;
        @BindView(R.id.redrecord_mygray_ll)
        LinearLayout redrecordMygrayLl;
        @BindView(R.id.redcored_send_num)
        TextView sendRedNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    protected SelfRedRecordContract.SelfRedRecordPresenter getPresenter() {
        return new SelfRedRecordPresneterImpl();
    }
}
