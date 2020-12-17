package com.kmt.pro.adapter;

import android.view.View;
import android.widget.ImageView;

import com.kmt.pro.R;
import com.kmt.pro.bean.DeviceManagerBean;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;

/**
 * Create by JFZ
 * date: 2020-07-20 15:48
 **/
public class DevcieManagerAdapter extends BaseQuickAdapter<DeviceManagerBean, BaseViewHolder> {

    private boolean isEdit;//编辑状态

    @Override
    protected void convert(BaseViewHolder holder, DeviceManagerBean item) {
        ImageView deleteIv = holder.getView(R.id.item_device_delete);
        if (isEdit) {
            deleteIv.setVisibility(View.VISIBLE);
        } else deleteIv.setVisibility(View.GONE);

        holder.setText(R.id.item_decive_name, item.deviceName);
        holder.setText(R.id.item_login_time, item.deviceLastTime);
    }

    public DevcieManagerAdapter() {
        super(R.layout.item_device_manager, new ArrayList<>());
        addChildClickViewIds(new int[]{R.id.item_device_delete});
    }

    public void setEdit(boolean b){
        this.isEdit = b;
        notifyDataSetChanged();
    }

    public boolean isEdit() {
        return isEdit;
    }
}
