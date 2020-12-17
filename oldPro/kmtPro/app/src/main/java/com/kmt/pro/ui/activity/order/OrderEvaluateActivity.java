package com.kmt.pro.ui.activity.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.adapter.EvaluateImageAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.callback.impl.OnItemDragListenerImpl;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.OrderEvaluateContract;
import com.kmt.pro.mvp.presenter.OrderEvaluatePresenterImpl;
import com.kmt.pro.ui.dialog.ActionSheetDialog;
import com.kmt.pro.utils.PermissionCheck;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.utils.compressor.Compressor;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.utils.Utils;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jiang.photo.picker.Photo;
import jiang.photo.picker.utils.GridSpacingItemDecoration;
import jiang.photo.picker.utils.PickerFileUtil;

/**
 * Create by JFZ
 * date: 2020-08-04 11:20
 * 评价
 **/
public class OrderEvaluateActivity extends BaseToolBarActivity<OrderEvaluateContract.OrderEvaluatePresenter> implements OrderEvaluateContract.OrderEvaluateView {
    @BindView(R.id.write_evaluate_note)
    EditText writeEvaluateNote;
    @BindView(R.id.enaluate_rv)
    RecyclerView enaluateRv;
    @BindView(R.id.evaluate_commit_btn)
    Button evaluateCommitBtn;
    @BindView(R.id.evaluate_delete)
    Button evaluateDelete;
    @BindView(R.id.evaluate_msg)
    TextView evaluateMsg;
    private String mBookId, investorsCode, houseName;
    private EvaluateImageAdapter adapter;
    private View footer;
    private boolean canDelete;
    //选择相机
    private final static int REQUEST_CAMERA = 67;
    private String cameraPath;
    private String imageUrls = "";
    private List<String> urls;
    private int uploadSize;
    private DefLoad load;
    private List<String> cacheimages;//用来记录上传个数

    @Override
    public int getLayoutId() {
        mBookId = getIntent().getStringExtra(KConstant.Key.order_id);
        investorsCode = getIntent().getStringExtra(KConstant.Key.investorsCode);
        houseName = getIntent().getStringExtra(KConstant.Key.houseName);
        return R.layout.activity_order_evaluate;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("评价");
        enaluateRv.setLayoutManager(new GridLayoutManager(this, 3));
        enaluateRv.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dip2px(BaseApp.getInstance(), 5), false));
        int size = (Utils.getScreenWidth(BaseApp.getInstance()) - Utils.dip2px(BaseApp.getInstance(), 34)) / 3;
        adapter = new EvaluateImageAdapter(size);
        adapter.getDraggableModule().setDragEnabled(true);
        adapter.setFooterViewAsFlow(true);
        footer = LayoutInflater.from(this).inflate(R.layout.item_evaluate_image_add, null);
        footer.setLayoutParams(new RecyclerView.LayoutParams(size, size));
        footer.setOnClickListener(v -> selectImage());
        adapter.setFooterView(footer);
        adapter.getDraggableModule().setOnItemDragListener(new OnItemDragListenerImpl() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                evaluateDelete.setVisibility(View.VISIBLE);
                canDelete = false;
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
//                Logger.e("moving_End:" + pos);
                evaluateDelete.setVisibility(View.GONE);
                if (canDelete) {
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                    adapter.removeAt(pos);
                    canDelete = false;
                    checkSize();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                Logger.e("moving_childdraw");
                int[] location = new int[2];
                viewHolder.itemView.getLocationInWindow(location);
                int[] delLocation = new int[2];
                evaluateDelete.getLocationInWindow(delLocation);
                int itemViewHeight = viewHolder.itemView.getMeasuredHeight();
                if (location[1] > (delLocation[1] - itemViewHeight)) {
                    canDelete = true;
                    viewHolder.itemView.setAlpha(0.5f);
                } else {
                    canDelete = false;
                    viewHolder.itemView.setAlpha(1f);
                }
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy, long superDuration) {
//                Logger.e("moving_duration:" + animateDx + ",ay:" + animateDy + ",duration:" + superDuration);
                return canDelete ? 0 : super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy, superDuration);
            }
        });
        enaluateRv.setAdapter(adapter);
        checkSize();
    }

    @Override
    public void initData() {
    }

    @Override
    public void commitSuccess() {
        Tools.showToast("评价成功!");
        ActivityHelper.toOrderEvaluateFinishActivity(this, mBookId, investorsCode, houseName);
    }

    @Override
    public void uploadImageSuccess(int index, String url) {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        urls.add(url);
        commit();
    }

    @Override
    public void uploadImageErr(int index, String err) {
        Tools.showToast("第" + (index + 1) + "张上传异常");
        uploadSize = uploadSize - 1;
        cacheimages.remove(index);
        if (uploadSize == cacheimages.size()) {
            //如果最后一张上传失败
            commit();
        }
    }

    private void commit() {
        if (urls != null) {
            int size = urls.size();
            if (size == uploadSize) {
                for (int i = 0; i < urls.size(); i++) {
                    if (i == urls.size() - 1) {
                        imageUrls += urls.get(i);
                    } else {
                        imageUrls += urls.get(i) + ",";
                    }
                }
                presenter.commitBooking(load, mBookId, investorsCode, writeEvaluateNote.getText().toString().trim(), imageUrls);
            }
        }
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.evaluate_commit_btn)
    public void onViewClicked() {
        String content = writeEvaluateNote.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Tools.showToast("评论内容不能为空");
            return;
        }
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(content);
        if (emojiMatcher.find()) {
            Tools.showToast("暂不支持表情");
            return;
        }
        if (adapter.getData().size() > 0) {
            urls = new ArrayList<>();
            urls.clear();
            uploadSize = adapter.getData().size();
            //先上传图片
            load = DefLoad.use(this);
            load.show();
            for (int i = 0; i < adapter.getData().size(); i++) {
                final int index = i;
                new Compressor(BaseApp.getInstance())
                        .compressToFileAsObservable(new File(adapter.getItem(i)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(file -> presenter.uploadImages(index, file), throwable -> presenter.uploadImages(index, new File(adapter.getItem(index))));
            }
        } else {
            imageUrls = "";
            load = DefLoad.use(this);
            load.show();
            presenter.commitBooking(load, mBookId, investorsCode, content, imageUrls);
        }
    }

    @Override
    protected OrderEvaluateContract.OrderEvaluatePresenter getPresenter() {
        return new OrderEvaluatePresenterImpl();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("=======回调=====onActivityResult：" + resultCode + "," + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                adapter.addData(cameraPath);
                checkSize();
            }
        }
    }

    //选择图片
    private void selectImage() {
        new ActionSheetDialog(OrderEvaluateActivity.this).builder()
                .setTitle("您希望如何设置照片?")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍摄新照片", ActionSheetDialog.SheetItemColor.Blue, which -> {
                    if (PermissionCheck.checkCameraPermission(this)) {
                        startCamera();
                    } else {
                        PermissionCheck.reqCamera(this, 100);
                    }
                })
                .addSheetItem("从照片库选取", ActionSheetDialog.SheetItemColor.Blue, which -> {
                    if (PermissionCheck.checkReadWritePermission(this)) {
                        Photo.with()
                                .mode(Photo.SelectMode.MULTIPLE)
                                .maxSelectNums(6 - adapter.getData().size())
                                .into(this, images -> {
                                    adapter.addData(images);
                                    checkSize();
                                });
                    } else
                        PermissionCheck.reqPermission(this, PermissionCheck.PERMISSIONS_READ_AND_WRITE, 101);
                }).show();
    }

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

    private void checkSize() {
        cacheimages = new ArrayList<>();
        cacheimages.clear();
        if (adapter.getData().size() < 6) {
            footer.setVisibility(View.VISIBLE);
            evaluateMsg.setVisibility(adapter.getData().size() > 1 ? View.VISIBLE : View.INVISIBLE);
            cacheimages.addAll(adapter.getData());
        } else {
            evaluateMsg.setVisibility(View.VISIBLE);
            footer.setVisibility(View.GONE);
            urls = new ArrayList<>();
            urls.clear();
        }
    }

}
