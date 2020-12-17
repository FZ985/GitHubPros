package com.kmt.pro.ui.activity.coin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.adapter.coin.RechargeTabAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.coin.SymbolRespBean;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Save;
import com.kmt.pro.mvp.contract.CoinContract;
import com.kmt.pro.mvp.presenter.CoinPresenterImpl;
import com.kmt.pro.utils.PermissionCheck;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.utils.Utils;
import com.kmtlibs.okhttp.HttpImpl;
import com.lxbuytimes.kmtapp.zxing.QRCodeEncoder;
import com.lxbuytimes.kmtapp.zxing.core.BGAQRCodeUtil;

import java.io.File;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-23 9:46
 **/
public class RechargeCoinActivity extends BaseToolBarActivity<CoinContract.CoinPresenter> implements CoinContract.RechargeView {

    @BindView(R.id.recharge_choose)
    TextView rechargeChoose;
    @BindView(R.id.tab_recycler)
    RecyclerView tabRecycler;
    @BindView(R.id.recharge_qrcode_iv)
    ImageView rechargeQrcodeIv;
    @BindView(R.id.recharge_url)
    TextView rechargeUrl;
    @BindView(R.id.recharge_desc)
    TextView rechargeDesc;
    @BindView(R.id.recharge_sc)
    NestedScrollView rechargeSc;
    private String symbol_id;
    private String tokenSymbol;
    private String address;
    private int offest = Utils.dip2px(BaseApp.getInstance(), 50);
    private RechargeTabAdapter adapter;

    @Override
    public int getLayoutId() {
        tokenSymbol = getIntent().getStringExtra(KConstant.Key.key_tokenSymbol);
        symbol_id = getIntent().getStringExtra(KConstant.Key.key_symbol_id);
        return R.layout.activity_recharge_coin;
    }

    @Override
    public void initView() {
        tabRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mToolbar.setRightDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_history));
        mToolbar.right.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(symbol_id)) {
                ActivityHelper.toRechargeRecord(RechargeCoinActivity.this, symbol_id);
            }
        });
        rechargeChoose.setText("" + tokenSymbol);
        rechargeSc.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 >= offest) {
                mToolbar.title.setText("充值");
                mToolbar.bottomLine(true);
            } else {
                mToolbar.title.setText("");
                mToolbar.bottomLine(false);
            }
        });
    }

    @Override
    public void initData() {
        mToolbar.bottomLine(false);
        presenter.reqRechargeInfo(symbol_id);
    }

    @Override
    public void onRechargeData(List<SymbolRespBean> data) {
        if (isFinishing()) return;
        if (data == null) return;
        if (data.size() > 0) {
            tabRecycler.setAdapter(adapter = new RechargeTabAdapter());
            data.get(0).check = true;
            adapter.setNewData(data);
            setTabData(data.get(0));
            adapter.setOnItemClickListener((quickAdapter, view, position) -> {
                SymbolRespBean item = adapter.getItem(position);
                if (item != null) {
                    setTabData(item);
                    adapter.check(position);
                }
            });
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap != null) {
                    rechargeQrcodeIv.setImageBitmap(bitmap);
                }
                break;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onRechargeErr(String err) {

    }

    private void setTabData(final SymbolRespBean data) {
        address = data.address;
        rechargeUrl.setText(data.address + "");
        if (!TextUtils.isEmpty(data.explain)) {
            rechargeDesc.setText(Html.fromHtml(data.explain));
        }
        HttpImpl.THREAD_POOL.execute(() -> {
            try {
                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(data.address, BGAQRCodeUtil.dp2px(RechargeCoinActivity.this, 160));
                if (!isFinishing() && mHandler != null) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = bitmap;
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
            }
        });
    }

    @OnClick({R.id.recharge_choose_tv, R.id.recharge_save_image, R.id.recharge_copy_url})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge_choose_tv:
                finish();
                break;
            case R.id.recharge_save_image:
                Bitmap bitmap = Utils.getViewBitmap(rechargeQrcodeIv);
                if (bitmap == null) return;
                if (PermissionCheck.checkReadWritePermission(this)) {
                    File file = Save.saveSymbolImage(bitmap, address + "");
                    if (file.exists()) {
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                        Tools.showToast("保存成功");
                    }
                } else
                    PermissionCheck.reqPermission(this, PermissionCheck.PERMISSIONS_READ_AND_WRITE, 200);
                break;
            case R.id.recharge_copy_url:
                String url = rechargeUrl.getText().toString().trim();
                if (!TextUtils.isEmpty(url)) {
                    Utils.copyString(url, this);
                    Tools.showToast("复制成功");
                }
                break;
        }
    }

    @Override
    protected CoinContract.CoinPresenter getPresenter() {
        return new CoinPresenterImpl();
    }
}
