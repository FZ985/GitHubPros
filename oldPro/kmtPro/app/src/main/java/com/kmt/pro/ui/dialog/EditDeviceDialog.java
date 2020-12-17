package com.kmt.pro.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.base.BaseDialog;

import androidx.annotation.NonNull;

/**
 * Create by JFZ
 * date: 2020-07-20 16:53
 **/
public class EditDeviceDialog extends BaseDialog {
    private TextView device_close;
    private EditText device_edit;
    private Button device_confirm;
    private String name;
    private EditCall call;

    public interface EditCall {
        void edit(Dialog dialog, String name);
    }

    public EditDeviceDialog(@NonNull Context context, String name, EditCall call) {
        super(context);
        this.name = name;
        this.call = call;
        setCanceledOnTouchOutside(false);
        initData();
    }

    @Override
    public int resLayoutId() {
        return R.layout.dialog_edit_device;
    }

    @Override
    public void initView() {
        device_close = findViewById(R.id.device_close);
        device_edit = findViewById(R.id.device_edit);
        device_confirm = findViewById(R.id.device_confirm);
    }

    @Override
    public void initData() {
        device_edit.setHint(name);
        device_close.setOnClickListener(v -> dismiss());
        device_confirm.setOnClickListener(v -> {
            String newName = device_edit.getText().toString().trim();
            if (TextUtils.isEmpty(newName)) {
                Tools.showToast("内容不能为空");
                return;
            }
            if (newName.equals(name)) {
                Tools.showToast("请输入不同名称");
                return;
            }
            call.edit(this, newName);
        });
    }
}
