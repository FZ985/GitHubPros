package com.kmt.pro.ui.activity.coin;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.adapter.coin.RechargeTabAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.coin.MentionRespBean;
import com.kmt.pro.bean.coin.SymbolRespBean;
import com.kmt.pro.callback.impl.CustomClickListener;
import com.kmt.pro.callback.impl.TextWatcherImpl;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.CoinContract;
import com.kmt.pro.mvp.presenter.CoinPresenterImpl;
import com.kmt.pro.ui.activity.QrCodeCameraActivity;
import com.kmt.pro.ui.dialog.CheckMentionDialog;
import com.kmt.pro.ui.dialog.PrivilegSuccessDialog;
import com.kmt.pro.ui.dialog.RiskWarningDialog;
import com.kmt.pro.utils.PermissionCheck;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.utils.Utils;
import com.kmtlibs.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-23 15:22
 **/
public class MentionMoneyActivity extends BaseToolBarActivity<CoinContract.CoinPresenter> implements CoinContract.MentionMoneyView {

    @BindView(R.id.recharge_choose)
    TextView rechargeChoose;
    @BindView(R.id.recharge_choose_tv)
    TextView rechargeChooseTv;
    @BindView(R.id.tab_recycler)
    RecyclerView tabRecycler;
    @BindView(R.id.mention_url_et)
    EditText mentionUrlEt;
    @BindView(R.id.mention_qrcode)
    ImageView mentionQrcode;
    @BindView(R.id.mention_qrcode_record)
    ImageView mentionQrcodeRecord;
    @BindView(R.id.mention_gbt_et)
    EditText mentionGbtEt;
    @BindView(R.id.tibi_unit1)
    TextView tibiUnit1;
    @BindView(R.id.mention_gbt_all)
    TextView mentionGbtAll;
    @BindView(R.id.symbol_num)
    TextView symbolNum;
    @BindView(R.id.mention_min)
    TextView mentionMin;
    @BindView(R.id.mention_shouxufei)
    TextView mentionShouxufei;
    @BindView(R.id.tibi_unit2)
    TextView tibiUnit2;
    @BindView(R.id.mention_desc)
    TextView mentionDesc;
    @BindView(R.id.recharge_sc)
    NestedScrollView rechargeSc;
    @BindView(R.id.mention_commit)
    TextView mentionCommit;
    private String symbol_id;
    private String tokenSymbol;
    private int offest = Utils.dip2px(BaseApp.getInstance(), 50);
    private RechargeTabAdapter adapter;
    private MentionRespBean.Symbol symbol;
    private RiskWarningDialog riskWarningDialog;
    private CheckMentionDialog checkMentionDialog;

    @Override
    public int getLayoutId() {
        tokenSymbol = getIntent().getStringExtra(KConstant.Key.key_tokenSymbol);
        symbol_id = getIntent().getStringExtra(KConstant.Key.key_symbol_id);
        return R.layout.activity_mention_money;
    }

    @Override
    public void initView() {
        tabRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tabRecycler.setAdapter(adapter = new RechargeTabAdapter());
        mToolbar.setRightDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_history));
        mToolbar.right.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(symbol_id)) {
                ActivityHelper.toMentionRecord(MentionMoneyActivity.this, symbol_id);
            }
        });
        rechargeChoose.setText("" + tokenSymbol);
        rechargeSc.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 >= offest) {
                mToolbar.title.setText("提币");
                mToolbar.bottomLine(true);
            } else {
                mToolbar.title.setText("");
                mToolbar.bottomLine(false);
            }
        });
        tibiUnit1.setText(tokenSymbol);
        tibiUnit2.setText(tokenSymbol);
        mentionUrlEt.addTextChangedListener(new TextWatcherImpl() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (TextUtils.isEmpty(s)) {
                    mentionCommit.setEnabled(false);
                } else {
                    String numEt = mentionGbtEt.getText().toString();
                    mentionCommit.setEnabled(!TextUtils.isEmpty(numEt));
                }
            }
        });
        mentionGbtEt.addTextChangedListener(new TextWatcherImpl() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (TextUtils.isEmpty(s)) {
                    mentionCommit.setEnabled(false);
                    mentionMin.setText("");
                } else {
                    String numEt = mentionUrlEt.getText().toString();
                    mentionCommit.setEnabled(!TextUtils.isEmpty(numEt));
                    mentionGbtEt.setSelection(s.length());
                    if (symbol != null) {
                        try {
                            float nums = Float.parseFloat(s.toString());
                            float current = Float.parseFloat(symbol.getSymbol_num());
                            float service = Float.parseFloat(symbol.getServiceCharge());
                            if (nums > current) {
                                Tools.showToastCenter("可用数量不足");
                                mentionMin.setText("");
                                return;
                            }
                            float keti = nums - service;
                            if (keti > 0) {
                                mentionMin.setText("实际可提" + (nums - service));
//                                if (keti < 2) {
////                                    Tools.showToastCenter("实际可提数量不足");
////                                }
                            } else {
                                mentionMin.setText("实际可提0");
                            }
//                            if (nums < 3) {//最低提币2
//                                mentionCommit.setEnabled(false);
//                            }
                            if (nums > 0) {//最低提币2
                                mentionCommit.setEnabled(!TextUtils.isEmpty(numEt));
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        });
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            adapter.check(position);
            SymbolRespBean item = adapter.getItem(position);
            if (item != null) setData(item);
        });
        mentionCommit.setOnClickListener(new CustomClickListener() {
            @Override
            protected void onSingleClick(View view) {
                commit();
            }
        });
    }

    @Override
    public void initData() {
        mToolbar.bottomLine(false);
        presenter.reqMentionInit(true, symbol_id);
    }

    @Override
    public void onMentionData(MentionRespBean data) {
        if (isFinishing()) return;
        if (data != null) {
            if (data.symbol != null) {
                symbol = data.symbol;
                mentionShouxufei.setText(data.symbol.getServiceCharge());
                tibiUnit1.setText(data.symbol.tokenSymbol);
                tibiUnit2.setText(data.symbol.tokenSymbol);
                symbolNum.setText("可用" + data.symbol.getSymbol_num());
            }
            if (data.getTab().size() > 0) {
                data.getTab().get(0).check = true;
                setData(data.getTab().get(0));
                adapter.setNewData(data.getTab());
            }
        }
    }

    @Override
    public void onCommit(int flag, String msg) {
        mentionCommit.setClickable(true);
        if (isFinishing()) return;
        if (flag == 1) {
            new PrivilegSuccessDialog(this, R.mipmap.v2_invite_buy_success, msg).show();
            //提币成功,刷新数据
            presenter.reqMentionInit(false, symbol_id);
        } else {
            new PrivilegSuccessDialog(this, R.mipmap.v2_invite_buy_err, msg).show();
        }
    }

    @OnClick({R.id.recharge_choose_tv, R.id.mention_qrcode, R.id.mention_gbt_all, R.id.mention_qrcode_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge_choose_tv:
                finish();
                break;
            case R.id.mention_qrcode:
                if (PermissionCheck.checkCameraPermission(this)) {
                    startActivityForResult(new Intent(this, QrCodeCameraActivity.class), 101);
                } else {
                    PermissionCheck.reqPermission(this, PermissionCheck.perms, 200);
                }
                break;
            case R.id.mention_gbt_all:
                if (symbol != null) {
                    String nums = symbol.getSymbol_num();
                    if (nums.equals("0")) {
                        Tools.showToast("可用数量不足");
                        return;
                    }
                    mentionGbtEt.setText(symbol.getSymbol_num());
                }
                break;
            case R.id.mention_qrcode_record:
                startActivity(new Intent(this, QrCodeAddressRecordActivity.class));
                break;
        }
    }

    private void commit() {
        hideSoftKeyBoard();
        if (symbol == null) return;
        String url = mentionUrlEt.getText().toString().trim();
        String nums = mentionGbtEt.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            Tools.showToastCenter("请输入提币地址");
            return;
        }
        if (TextUtils.isEmpty(nums)) {
            Tools.showToastCenter("请输入提币数量");
            return;
        }
        String finalNums = "0";
        try {
            float numsFloat = Float.parseFloat(nums);
            float currentFloat = Float.parseFloat(symbol.getSymbol_num());
            float service = Float.parseFloat(symbol.getServiceCharge());
            finalNums = String.valueOf((numsFloat - service));
//            if ((numsFloat - service) < 2) {
//                Tools.showToastCenter("最小实际提币数量为2");
//                return;
//            }
            if ((numsFloat - service) <= 0) {
                Tools.showToastCenter("实际提币数量不足");
                return;
            }

            if (numsFloat > currentFloat) {
                Tools.showToastCenter("可用数量不足");
                return;
            }
            SymbolRespBean checkItem = adapter.getCheckItem();
            if (checkItem != null) {
                warning(symbol_id, finalNums, url, checkItem.network_id, symbol.getServiceCharge());
            }
        } catch (NumberFormatException e) {
            Tools.showToast(e.getMessage());
            return;
        }
    }

    private void warning(final String symbol_id, final String finalNums, final String url, final String network_id, final String serviceCharge) {
        hideSoftKeyBoard();
        if (riskWarningDialog == null) {
            riskWarningDialog = new RiskWarningDialog(MentionMoneyActivity.this, dialog -> {
                if (dialog != null) {
                    dialog.dismiss();
                }
                riskWarningDialog = null;
                if (checkMentionDialog == null) {
                    checkMentionDialog = new CheckMentionDialog(MentionMoneyActivity.this, smsCode -> {
                        checkMentionDialog.dismiss();
                        checkMentionDialog = null;
                        hideSoftKeyBoard();
                        mentionCommit.setClickable(false);
                        presenter.reqMentionCommit(smsCode, symbol_id, finalNums, url, network_id, serviceCharge);
                    });
                }
                checkMentionDialog.show();
            });
        }
        riskWarningDialog.show();
    }

    private void setData(SymbolRespBean item) {
        if (!TextUtils.isEmpty(item.explain)) {
            mentionDesc.setText(Html.fromHtml(item.explain));
            mentionDesc.setVisibility(View.VISIBLE);
        } else mentionDesc.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (data != null) {
                String qrcodeUrl = data.getStringExtra("qrcode_url");
                mentionUrlEt.setText(qrcodeUrl + "");
            }
        }
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.keyboardEnable(true).statusBarDarkFont(true).init();
    }

    @Override
    protected CoinContract.CoinPresenter getPresenter() {
        return new CoinPresenterImpl();
    }
}
