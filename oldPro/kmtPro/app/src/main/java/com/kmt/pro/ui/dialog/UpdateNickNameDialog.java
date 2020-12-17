package com.kmt.pro.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.base.BaseDialog;

import androidx.annotation.NonNull;

/**
 * Create by JFZ
 * date: 2020-05-20 20:22
 **/
public class UpdateNickNameDialog extends BaseDialog {
    private String name;
    private EditText dialog_update_et;
    private TextView dialog_update_cancel, dialog_update_commit;
    private UpdateNickCall call;

    public interface UpdateNickCall {
        void onNick(String name);
    }

    public UpdateNickNameDialog(@NonNull Context context, String name, UpdateNickCall call) {
        super(context, R.style.auto_keybroad);
        this.name = TextUtils.isEmpty(name) ? "kmt" : name;
        this.call = call;
        width(WindowManager.LayoutParams.WRAP_CONTENT);
        initData();
    }

    @Override
    public int resLayoutId() {
        return R.layout.dialog_update_nick;
    }

    @Override
    public void initView() {
        dialog_update_et = findViewById(R.id.dialog_update_et);
        dialog_update_cancel = findViewById(R.id.dialog_update_cancel);
        dialog_update_commit = findViewById(R.id.dialog_update_commit);
        dialog_update_et.setFocusable(true);
        dialog_update_et.setFocusableInTouchMode(true);
    }

    @Override
    public void initData() {
        dialog_update_et.setText(name);
        dialog_update_et.setSelection(name.length());
        dialog_update_cancel.setOnClickListener(v -> dismiss());
        dialog_update_commit.setOnClickListener(v -> {
            String newname = dialog_update_et.getText().toString().trim();
            if (TextUtils.isEmpty(newname)) {
                Tools.showToast("名字不能为空");
                return;
            }
            if (newname.equals(name)) {
                dismiss();
                return;
            }
            if (call != null) {
                call.onNick(newname);
                dismiss();
            }
        });
    }
}
