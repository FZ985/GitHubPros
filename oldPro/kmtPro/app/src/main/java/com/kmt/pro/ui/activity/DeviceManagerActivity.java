package com.kmt.pro.ui.activity;

import android.view.View;

import com.kmt.pro.R;
import com.kmt.pro.adapter.DevcieManagerAdapter;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.DeviceManagerBean;
import com.kmt.pro.mvp.contract.DeviceManagerContract;
import com.kmt.pro.mvp.presenter.DeviceManagerPresenterImpl;
import com.kmt.pro.ui.dialog.EditDeviceDialog;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.app.dialog.NativeDlg;
import com.kmtlibs.app.utils.Utils;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-07-20 15:19
 **/
public class DeviceManagerActivity extends BaseToolBarActivity<DeviceManagerContract.DeviceManagerPresenter> implements DeviceManagerContract.DeviceManagerView {
    @BindView(R.id.device_recycle)
    RecyclerView deviceRecycle;
    @BindView(R.id.device_state)
    Statelayout deviceState;
    private DevcieManagerAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_device_manager;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("登录设备管理");
        deviceRecycle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DevcieManagerAdapter();
        deviceRecycle.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if (view.getId() == R.id.item_device_delete) {
                NativeDlg.create(this)
                        .canceledOnTouchOutside(false)
                        .msg("确定删除该设备?")
                        .msgTextSize(19)
                        .cancelClickListener("取消", (dialog, v) -> dialog.dismiss())
                        .okClickListener("确定", ContextCompat.getColor(mContext, R.color.colorPrimary), (dialog, v) -> {
                            presenter.deleterDevice(adapter.getItem(position).infoId, position);
                            dialog.dismiss();
                        })
                        .show();
            }
        });
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            if (adapter.isEdit()) {
                DeviceManagerBean item = adapter.getItem(position);
                new EditDeviceDialog(this, item.deviceName, (dialog, name) -> {
                    presenter.remarkDevice(item.infoId, name, position);
                    dialog.dismiss();
                }).show();
            }
        });
    }

    @Override
    public void onRemarkDeviceSuccess(int position, String name) {
        adapter.getData().get(position).deviceName = name;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initData() {
        presenter.requestList();
    }

    @Override
    public void onList(List<DeviceManagerBean> datas) {
        deviceState.showContent();
        if (datas.size() > 0) {
            boolean bool = adapter.isEdit();
            if (!bool) {
                mToolbar.right.setText("编辑");
            } else {
                mToolbar.right.setText("完成");
            }
            adapter.setNewInstance(datas);
            mToolbar.right.setOnClickListener(editListener);
        } else {
            mToolbar.right.setText("");
            mToolbar.right.setOnClickListener(null);
            deviceState.showEmpty();
        }
    }

    @Override
    public void onDeleteSuccess(int position) {
        adapter.removeAt(position);
    }

    private View.OnClickListener editListener = v -> {
        boolean bool = adapter.isEdit();
        if (bool) {
            mToolbar.right.setText("编辑");
            initData();
        } else {
            mToolbar.right.setText("完成");
        }
        adapter.setEdit(!bool);
    };
    private View.OnClickListener questListener = v -> initData();

    @Override
    public void onListErr(String err) {
        Tools.showToast(err);
        if (Utils.isConnection(this)) {
            deviceState.showError(questListener);
        } else deviceState.showNoNetwork(questListener);
    }

    @Override
    protected DeviceManagerContract.DeviceManagerPresenter getPresenter() {
        return new DeviceManagerPresenterImpl();
    }
}
