package com.kmt.pro.ui.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.kmt.pro.R;
import com.kmt.pro.adapter.MineItemAdapter;
import com.kmt.pro.adapter.MineNoticeAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.mine.LockUpBean;
import com.kmt.pro.bean.mine.MineBean;
import com.kmt.pro.bean.mine.MineItemBean;
import com.kmt.pro.bean.mine.SymbolNumsBean;
import com.kmt.pro.callback.MainFragmentPageCall;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Login;
import com.kmt.pro.helper.UrlJumpHelper;
import com.kmt.pro.mvp.contract.MineContract;
import com.kmt.pro.mvp.presenter.MinePresenterImpl;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.ui.dialog.ActionSheetDialog;
import com.kmt.pro.ui.dialog.UpdateNickNameDialog;
import com.kmt.pro.ui.fragment.ContentFragment;
import com.kmt.pro.utils.PermissionCheck;
import com.kmt.pro.web.WebJump;
import com.kmt.pro.widget.marqueeview.MarqueeView;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.widget.IGridView;
import com.lxbuytimes.kmtapp.span.Span;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import jiang.photo.picker.Photo;
import jiang.photo.picker.activity.CropImageActivity;
import jiang.photo.picker.activity.SelectImageActivity;
import jiang.photo.picker.utils.PickerFileUtil;
import jiang.photo.picker.widget.CropImageView;
import magicindicator.MagicIndicator;

import static android.app.Activity.RESULT_OK;

/**
 * Create by JFZ
 * date: 2020-06-30 18:38
 **/
public class MineFragment extends ContentFragment<MinePresenterImpl> implements MineContract.MineView {
    @BindView(R.id.mine_marquee)
    MarqueeView mineMarquee;
    @BindView(R.id.person_totalvalue)
    TextView personTotalvalue;
    @BindView(R.id.mine_viewpager)
    ViewPager mineViewpager;
    @BindView(R.id.mine_indicator)
    MagicIndicator mineIndicator;
    @BindView(R.id.mine_login_layout)
    LinearLayout mineLoginLayout;
    @BindView(R.id.mine_nologin_img)
    ImageView mineNologinImg;
    @BindView(R.id.mine_recharge_ll)
    LinearLayout mineRechargeLl;
    @BindView(R.id.mine_withdraw_ll)
    LinearLayout mineWithdrawLl;
    @BindView(R.id.mine_top_layout)
    LinearLayout mineTopLayout;
    @BindView(R.id.mine_lockup_money)
    TextView mineLockupMoney;
    @BindView(R.id.mine_lockup_status)
    TextView mineLockupStatus;
    @BindView(R.id.mine_lockup_ll)
    LinearLayout mineLockupLl;
    @BindView(R.id.mine_grid)
    IGridView mineGrid;
    @BindView(R.id.mine_scroll)
    NestedScrollView mineScroll;
    @BindView(R.id.mine_refresh)
    SwipeRefreshLayout mineRefresh;
    @BindView(R.id.mine_headimg)
    ImageView mineHeadimg;
    @BindView(R.id.mine_nickname)
    TextView mineNickname;
    @BindView(R.id.mine_set)
    ImageView mineSet;
    @BindView(R.id.mine_toolbar_ll)
    LinearLayout mineToolbarLl;
    @BindView(R.id.mine_nologin_layout)
    RelativeLayout mineNologinLayout;
    @BindView(R.id.mine_symbol_nums)
    TextView mine_symbol_nums;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        mineRefresh.setColorSchemeResources(R.color.colorPrimary);
        mineRefresh.setOnRefreshListener(() -> request(true));
    }

    @Override
    public void initData() {

    }

    @Override
    public void refresh() {
    }

    @Override
    public void onResume() {
        super.onResume();
        layoutInit();
    }

    private void mineData() {
        Glide.with(BaseApp.getInstance()).load(KConstant.img_url + UserSp.get().getHeadImg())
                .transform(new CircleCrop()).placeholder(R.mipmap.default_head_img).error(R.mipmap.default_head_img)
                .into(mineHeadimg);
        mineNickname.setText(UserSp.get().getNickName());
    }

    private void layoutInit() {
        if (Login.get().isLogin()) {
            mineTopLayout.setBackgroundResource(R.drawable.bg_mine_top_login);
            mineLoginLayout.setVisibility(View.VISIBLE);
            mineNologinLayout.setVisibility(View.GONE);
            mineNickname.setVisibility(View.VISIBLE);
            mineHeadimg.setVisibility(View.VISIBLE);
            mineRefresh.setEnabled(true);
            mineData();
            request(true);
        } else {
            mineRefresh.setEnabled(false);
            mineTopLayout.setBackgroundResource(R.drawable.bg_mine_top_no_login);
            mineNickname.setVisibility(View.GONE);
            mineHeadimg.setVisibility(View.GONE);
            mineLockupLl.setVisibility(View.GONE);
            mineLoginLayout.setVisibility(View.GONE);
            mineNologinLayout.setVisibility(View.VISIBLE);
        }
        if (call != null) {
            call.onFragmentPage(4, 0);
        }
        presenter.reqItems();
    }

    private void request(boolean bool) {
        presenter.reqInfo();
        presenter.reqLockup();
        if (mineMarquee.getVisibility() != View.VISIBLE) {
            presenter.reqNotice();
        }
        presenter.reqSymbols();
    }

    @Override
    public void respInfo(MineBean mine) {
        mineRefresh.setRefreshing(false);
        if (mine != null) {
            if (!TextUtils.isEmpty(mine.userName)) {
                mineNickname.setText(mine.userName);
            }
            if (!TextUtils.isEmpty(mine.userAvatar)) {
                Glide.with(mineHeadimg.getContext()).load(KConstant.img_url + mine.userAvatar)
                        .placeholder(R.mipmap.default_head_img).error(R.mipmap.default_head_img).transform(new CircleCrop())
                        .into(mineHeadimg);
            }
            personTotalvalue.setText(mine.generalAssets);
            chartData(mine);
        }
    }

    @Override
    public void respLockup(LockUpBean lock) {
        if (lock != null) {
            mineLockupLl.setVisibility(View.VISIBLE);
            Span.impl().append(Span.builder("锁仓金额").textSize(13).textColor(Color.parseColor("#6C430F")))
                    .append(Span.builder(lock.getLockupMoney()).textSize(18).textColor(Color.parseColor("#6B430F")))
                    .append(Span.builder("GBT").textSize(13).textColor(Color.parseColor("#6C430F")))
                    .totalClickListener((text, widget) -> onViewClicked(mineLockupLl))
                    .into(mineLockupMoney);
            mineLockupStatus.setText("锁仓预警:" + lock.getPrediction());
        }
    }

    @Override
    public void respItems(List<MineItemBean> list) {
        if (mineGrid != null) {
            MineItemBean m1 = new MineItemBean();
            m1.url = KConstant.UrlContants.customerService;
            m1.title = "客服";
            list.add(m1);

            mineGrid.setAdapter(new MineItemAdapter(list));
            mineGrid.setOnItemClickListener((parent, view, position, id) -> UrlJumpHelper.mineJump(list.get(position).url));
        }
    }

    @Override
    public void respSymbol(SymbolNumsBean data) {
        if (data != null) {
            mine_symbol_nums.setVisibility(View.VISIBLE);
            mine_symbol_nums.setText("=" + data.getSymbolNum() + data.getTokenSymbol());
        } else {
            mine_symbol_nums.setVisibility(View.GONE);
        }
    }

    @Override
    public void respNotice(List<String> list) {
        if (!isDetached() && list.size() > 0) {
            mineMarquee.setVisibility(View.VISIBLE);
            MineNoticeAdapter mAdapter = new MineNoticeAdapter(getActivity(), list);
            mineMarquee.setAdapter(mAdapter);
        }
    }

    @Override
    public void respNickname() {
        mineNickname.setText(UserSp.get().getNickName());
    }

    //资产图表添加
    private void chartData(MineBean mine) {
        presenter.bindChart(mineViewpager, mineIndicator, mine, getChildFragmentManager());
    }

    public static MineFragment instance(MainFragmentPageCall call) {
        MineFragment fragment = new MineFragment();
        fragment.call = call;
        return fragment;
    }

    private UpdateNickNameDialog dialog;

    @OnClick({R.id.mine_recharge_ll, R.id.mine_withdraw_ll, R.id.mine_headimg, R.id.mine_nickname})
    public void onClickedView(View view) {
        if (Login.get().isLogin()) {
            switch (view.getId()) {
                case R.id.mine_recharge_ll:
                    ActivityHelper.toRechargeCoin(mContext);
                    break;
                case R.id.mine_withdraw_ll:
                    ActivityHelper.toTiCoin(mContext);
                    break;
                case R.id.mine_headimg:
                    new ActionSheetDialog(mAct).builder()
                            .setTitle("您希望如何设置头像?").setCancelable(false).setCanceledOnTouchOutside(true)
                            .addSheetItem("拍摄新照片", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                if (PermissionCheck.checkCameraPermission(mContext)) {
                                    startCamera();
                                } else {
                                    PermissionCheck.reqCamera(mAct, 99);
                                }
                            })
                            .addSheetItem("从照片库选取", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                if (PermissionCheck.checkReadWritePermission(mContext)) {
                                    chooseImage();
                                } else {
                                    PermissionCheck.reqPermission(mAct, PermissionCheck.PERMISSIONS_READ_AND_WRITE, 100);
                                }
                            }).show();
                    break;
                case R.id.mine_nickname:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    dialog = null;
                    dialog = new UpdateNickNameDialog(mAct, mineNickname.getText().toString().trim(), name -> presenter.updateNickName(name));
                    dialog.setOnDismissListener(dialog1 -> hideSoftKeyBoard());
                    dialog.show();
                    break;
            }
        } else {
            ActivityHelper.toLogin(mContext);
        }
    }

    @OnClick({R.id.mine_nologin_iv, R.id.mine_lockup_ll, R.id.mine_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_nologin_iv:
                ActivityHelper.toLogin(mContext);
                break;
            case R.id.mine_lockup_ll:
                WebJump.toWebNoShare(mContext, KConstant.Html.suocang, false);
                break;
            case R.id.mine_set:
                ActivityHelper.toSet(mContext);
                break;
        }
    }

    private final static int REQUEST_CAMERA = 67;
    private String cameraPath;

    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(BaseApp.getInstance().getPackageManager()) != null) {
            File cameraFile = PickerFileUtil.createCameraFile(mContext);
            cameraPath = cameraFile.getAbsolutePath();
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider并且这样可以解决MIUI系统上拍照返回size为0的情况
                uri = FileProvider.getUriForFile(mContext, BaseApp.getInstance().getPackageName() + ".fileprovider", cameraFile);
            } else {
                uri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    private void chooseImage() {
        Photo.with().mode(Photo.SelectMode.SINGLE).crop(true).cropMode(CropImageView.CropMode.SQUARE)
                .into(mContext, images -> presenter.compressorAvator(images.get(0)));
    }

    @Override
    public void respAvator() {
        if (!isDetached()) {
            Glide.with(mineHeadimg.getContext()).load(KConstant.img_url + UserSp.get().getHeadImg())
                    .placeholder(R.mipmap.default_head_img).error(R.mipmap.default_head_img).transform(new CircleCrop())
                    .into(mineHeadimg);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Logger.e("=======回调=====onActivityResult：" + resultCode + "," + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Intent cropIntent = new Intent(mContext, CropImageActivity.class);
                cropIntent.putExtra(CropImageActivity.KEY_CROP_PATH, cameraPath);
                startActivityForResult(cropIntent, SelectImageActivity.SELECT_CROP_REQUEST);
            } else if (requestCode == SelectImageActivity.SELECT_CROP_REQUEST) {
                if (data != null) {
                    int crop = data.getIntExtra(CropImageActivity.CROP_TYPE, -1);
                    if (crop == CropImageActivity.CROP_SUCCESS && data.getData() != null) {
                        String path = data.getData().getPath();
                        presenter.compressorAvator(path);
                    }
                }
            }
        }

    }

    @Override
    protected MinePresenterImpl getPresenter() {
        return new MinePresenterImpl();
    }
}
