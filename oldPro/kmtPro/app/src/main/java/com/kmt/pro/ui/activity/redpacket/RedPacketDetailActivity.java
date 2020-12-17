package com.kmt.pro.ui.activity.redpacket;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.kmt.pro.R;
import com.kmt.pro.adapter.redpacket.RedpacketDetailListAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.redpacket.OpenRedBean;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.event.DepositoryEvent;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Login;
import com.kmt.pro.helper.UrlJumpHelper;
import com.kmt.pro.mvp.contract.RedPacketDetailContract;
import com.kmt.pro.mvp.presenter.RedPacketDetailPresenterImpl;
import com.kmtlibs.adapter.base.listener.OnLoadMoreListener;
import com.kmtlibs.app.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-28 9:52
 **/
public class RedPacketDetailActivity extends BaseToolBarActivity<RedPacketDetailContract.RedPacketDetailPresenter> implements RedPacketDetailContract.RedPacketDetailView, OnLoadMoreListener {
    @BindView(R.id.red_recycle)
    RecyclerView redRecycle;
    @BindView(R.id.red_isshow_txt)
    TextView redIsshowTxt;
    private View head;

    private String red_id;
    private RedpacketDetailListAdapter adapter;
    private ViewHolder headHolder;
    private int page;

    @Override
    public int getLayoutId() {
        red_id = getIntent().getStringExtra(KConstant.Key.key_red_id);
        return R.layout.activity_redpacket_detail;
    }

    @Override
    public void initView() {
        toolbarColor(R.color.red_ff5555);
        mToolbar.title.setText("快买他红包");
        mToolbar.title.setTextColor(Color.parseColor("#fff2b6"));
        mToolbar.showWhiteBack(v -> finish());
        redRecycle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RedpacketDetailListAdapter();
        head = LayoutInflater.from(this).inflate(R.layout.head_redpacket_detail, null);
        headHolder = new ViewHolder(head);
        adapter.setHeaderView(head);
        adapter.setHeaderWithEmptyEnable(true);
        redRecycle.setAdapter(adapter);
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
        presenter.reqRedPacketInfo(red_id);
        presenter.reqRedList(page, red_id);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHeadInfo(OpenRedBean data) {
        if (data == null || isFinishing()) return;
        String mCode = data.code + "";
        if (data.head != null) {
            headHolder.headredUsername.setText(data.head.userName + "的红包");
            headHolder.headredStokeNameTv.setText(data.head.investorsName);
            headHolder.headredNote.setText(null != data.head.title ? data.head.title : getString(R.string.grab_red_envelope));

            if (Login.get().getData() != null && Login.get().getData().userId.equals(data.head.userId)) {
                headHolder.headredTotalNumTv.setText("已领取" + data.head.childrensUsed + "/"
                        + data.head.childrens + getString(R.string.red_total_num) + data.head.amount
                        + getString(R.string.red_total_money) + data.head.totalPrice + "GBT");
            }
            Glide.with(this).load(KConstant.img_url + data.head.userAvatar)
                    .placeholder(R.mipmap.icon_default_rect_headimg).error(R.mipmap.icon_default_rect_headimg)
                    .transform(new CenterCrop(), new RoundedCorners(Utils.dip2px(BaseApp.getInstance(), 1)))
                    .into(headHolder.headredAvator);

            if ("1".equals(mCode)) {
                headHolder.headredOpenRl.setVisibility(View.VISIBLE);
                headHolder.headredOvertimeTv.setVisibility(View.GONE);
                headHolder.headredOpenedTv.setVisibility(View.VISIBLE);
                headHolder.headredTotalNumTv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(data.amount)) {
                    headHolder.headredOpenedTv.setText(data.amount);
                }
                //跳持仓
                headHolder.headredChicanTv.setVisibility(View.VISIBLE);
                headHolder.headredChicanTv.setOnClickListener(v -> {
                    EventBus.getDefault().post(new DepositoryEvent(data.head.investorsCode));
                    UrlJumpHelper.jump(RedPacketDetailActivity.this, KConstant.UrlContants.bottomSwitch + 2);
                });
                if (Login.get().getData() != null && Login.get().getData().userId.equals(data.head.userId)) {
                    if (Integer.parseInt(data.head.childrensUsed) == Integer.parseInt(data.head.childrens)) {
                        headHolder.headredTotalNumTv.setText(data.head.childrens + getString(R.string.red_total_num)
                                + data.head.amount + getString(R.string.red_total_money) + data.head.totalPrice + "GBT");
                    } else {
                        headHolder.headredTotalNumTv.setText("已领取" + data.head.childrensUsed + "/" + data.head.childrens
                                + getString(R.string.red_total_num) + data.head.amount + getString(R.string.red_total_money) + data.head.totalPrice + "GBT");
                    }
                } else {
                    if (Integer.parseInt(data.head.childrensUsed) == Integer.parseInt(data.head.childrens)) {
                        headHolder.headredTotalNumTv.setText(data.head.childrens + "个红包已被抢完");
                    } else {
                        headHolder.headredTotalNumTv.setText("领取" + data.head.childrensUsed + "/" + data.head.childrens + "个");
                    }
                }
            } else if ("2".equals(mCode)) {
                headHolder.headredOpenRl.setVisibility(View.GONE);
                headHolder.headredOvertimeTv.setVisibility(View.GONE);
                headHolder.headredOpenedTv.setVisibility(View.GONE);
                headHolder.headredTotalNumTv.setVisibility(View.VISIBLE);
                if (Login.get().getData() != null && Login.get().getData().userId.equals(data.head.userId)) {
                    headHolder.headredTotalNumTv.setText(data.head.childrens + getString(R.string.red_total_num) + data.head.amount
                            + getString(R.string.red_total_money) + data.head.totalPrice + "GBT");
                } else {
                    headHolder.headredTotalNumTv.setText(data.head.childrens + "个红包已被抢完");
                }

            } else if ("3".equals(mCode)) {
                headHolder.headredOpenRl.setVisibility(View.GONE);
                headHolder.headredOvertimeTv.setVisibility(View.VISIBLE);
                headHolder.headredOpenedTv.setVisibility(View.GONE);
                headHolder.headredTotalNumTv.setVisibility(View.GONE);
                headHolder.headredOvertimeTv.setText("该红包已过期。已领取" + data.head.childrensUsed + "/" + data.head.childrens
                        + getString(R.string.red_total_num) + data.head.amountUsed + "/" + data.head.amount
                        + getString(R.string.red_total_money) + data.head.totalPrice + "GBT");
                redIsshowTxt.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRedListSucc(int page, List<RedDetailListBean> datas) {
        if (isFinishing()) return;
        if (page == 1) {
            if (datas.size() > 0) {
                adapter.setNewInstance(datas);
                adapter.getLoadMoreModule().setOnLoadMoreListener(this);
            }
        } else {
            if (datas.size() > 0) {
                adapter.addData(datas);
                adapter.getLoadMoreModule().loadMoreComplete();
            } else adapter.getLoadMoreModule().loadMoreEnd(true);
        }
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(() -> {
            if (!isFinishing()) {
                page = page + 1;
                presenter.reqRedList(page, red_id);
            }
        }, 250);
    }

    @Override
    public void onRedListErr(int page, String err) {
        if (isFinishing()) return;
        adapter.getLoadMoreModule().loadMoreEnd(true);
    }

    @OnClick(R.id.red_check_record)
    public void onViewClicked() {
        ActivityHelper.toSelfRedRecordActivity(this);
    }

    static class ViewHolder {
        @BindView(R.id.headred_avator)
        ImageView headredAvator;
        @BindView(R.id.headred_username)
        TextView headredUsername;
        @BindView(R.id.headred_note)
        TextView headredNote;
        @BindView(R.id.headred_opened_tv)
        TextView headredOpenedTv;
        @BindView(R.id.headred_miao_tv)
        TextView headredMiaoTv;
        @BindView(R.id.headred_stoke_name_tv)
        TextView headredStokeNameTv;
        @BindView(R.id.headred_open_rl)
        RelativeLayout headredOpenRl;
        @BindView(R.id.headred_chican_tv)
        TextView headredChicanTv;
        @BindView(R.id.headred_total_num_tv)
        TextView headredTotalNumTv;
        @BindView(R.id.headred_overtime_tv)
        TextView headredOvertimeTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    protected RedPacketDetailContract.RedPacketDetailPresenter getPresenter() {
        return new RedPacketDetailPresenterImpl();
    }
}
