package com.kmt.pro.ui.activity.coin;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.coin.RecordDetailBean;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.CoinContract;
import com.kmt.pro.mvp.presenter.CoinPresenterImpl;

import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-07-23 14:53
 **/
public class CoinRecordDetailActivity extends BaseToolBarActivity<CoinContract.CoinPresenter> implements CoinContract.RecordDetailView {
    @BindView(R.id.detail_nums)
    TextView detailNums;
    @BindView(R.id.detail_type)
    TextView detailType;
    @BindView(R.id.detail_status)
    TextView detailStatus;
    @BindView(R.id.url_tv)
    TextView urlTv;
    @BindView(R.id.detail_url)
    TextView detailUrl;
    @BindView(R.id.url_ll)
    LinearLayout urlLl;
    @BindView(R.id.detail_shouxufei_tv)
    TextView detailShouxufeiTv;
    @BindView(R.id.detail_shouxufei_ll)
    LinearLayout detailShouxufeiLl;
    @BindView(R.id.detail_jiaoyi_id)
    TextView detailJiaoyiId;
    @BindView(R.id.detail_date)
    TextView detailDate;
    private int gbt_type;
    private String blockHash = "blockHash";

    @Override
    public int getLayoutId() {
        gbt_type = getIntent().getIntExtra(KConstant.Key.key_gbt_type, 1);
        blockHash = getIntent().getStringExtra(KConstant.Key.key_blockHash);
        return R.layout.activity_coin_record_detail;
    }

    @Override
    public void initView() {
        if (gbt_type == 2) {
            detailShouxufeiLl.setVisibility(View.VISIBLE);
            urlLl.setVisibility(View.VISIBLE);
            urlTv.setText("提币地址");
            detailType.setText("普通提币");
        } else {
            detailType.setText("普通充币");
            urlTv.setText("充值地址");
            urlLl.setVisibility(View.GONE);
            detailShouxufeiLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        presenter.reqRrecordDetail(gbt_type, blockHash);
        mToolbar.bottomLine(false);
    }


    @Override
    public void onDetailSucc(RecordDetailBean data) {
        if (isFinishing())return;
        detailNums.setText(data.getValue() + " " + data.tokenSymbol);
        detailJiaoyiId.setText(data.blockHash);
        if (gbt_type == 1) {//充值地址
            detailUrl.setText(data.from);
            if (data.status == 0) {
                detailStatus.setText("确认中");
            } else if (data.status == 1) {
                detailStatus.setText("已完成");
            } else if (data.status == 2) {
                detailStatus.setText("失败");
            }
        } else if (gbt_type == 2) {//提币地址
            detailUrl.setText(data.to);
            if (data.status == 0) {
                detailStatus.setText("待审核");
            } else if (data.status == 1) {
                detailStatus.setText("确认中");
            } else if (data.status == 2) {
                detailStatus.setText("转账成功");
            } else if (data.status == 3) {
                detailStatus.setText("转账失败");
            } else if (data.status == 4) {
                detailStatus.setText("失败 ");
            }
        }
        detailDate.setText(data.created_at);
        detailShouxufeiTv.setText(data.serviceCharge + data.tokenSymbol + "");
    }

    @Override
    protected CoinContract.CoinPresenter getPresenter() {
        return new CoinPresenterImpl();
    }

}
