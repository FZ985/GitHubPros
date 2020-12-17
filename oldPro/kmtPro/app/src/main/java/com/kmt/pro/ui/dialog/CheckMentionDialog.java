package com.kmt.pro.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.http.HttpUrl;
import com.kmt.pro.http.ok;
import com.kmt.pro.http.request.MentionSmsCheckReq;
import com.kmt.pro.http.request.V2BaseReq;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.utils.Check;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.base.BaseDialog;
import com.kmtlibs.okhttp.callback.OkRequestCallback;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Create by JFZ
 * date: 2020-04-24 16:03
 **/
public class CheckMentionDialog extends BaseDialog {

    private CheckMentionCall call;

    private TextView dialog_mention_cancel, dialog_mention_phone, dialog_mention_timedown, dialog_mention_commit;
    private EditText dialog_mention_et;

    public interface CheckMentionCall {
        void call(String smsCode);
    }

    public CheckMentionDialog(@NonNull Context context, CheckMentionCall call) {
        super(context);
        this.call = call;
        animStyle(R.style.dialog_anim_bottom);
        gravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(false);
        initData();
        setOnDismissListener(dialog -> stopTimedown());
    }

    @Override
    public int resLayoutId() {
        return R.layout.dialog_checkmention;
    }

    @Override
    public void initView() {
        dialog_mention_cancel = findViewById(R.id.dialog_mention_cancel);
        dialog_mention_phone = findViewById(R.id.dialog_mention_phone);
        dialog_mention_commit = findViewById(R.id.dialog_mention_commit);
        dialog_mention_timedown = findViewById(R.id.dialog_mention_timedown);
        dialog_mention_et = findViewById(R.id.dialog_mention_et);
    }

    @Override
    public void initData() {
        dialog_mention_phone.setText(Check.getGoneCenterMobile(UserSp.get().getMobile()));
        dialog_mention_cancel.setOnClickListener(v -> dismiss());
        dialog_mention_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    dialog_mention_commit.setEnabled(true);
                } else {
                    dialog_mention_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog_mention_commit.setOnClickListener(v -> {
            String smsCode = dialog_mention_et.getText().toString().trim();
            if (TextUtils.isEmpty(smsCode)) {
                Tools.showToast("请输入手机验证码");
                return;
            }
            if (call != null) {
                call.call(smsCode);
                stopTimedown();
            }
        });

        dialog_mention_timedown.setOnClickListener(v -> {
            downTime();
            downReq();
        });
    }

    boolean isFinish;

    @Override
    public void show() {
        isFinish = false;
        super.show();
    }

    @Override
    public void dismiss() {
        isFinish = true;
        super.dismiss();
    }

    private void downReq() {
        V2BaseReq<MentionSmsCheckReq> req = new V2BaseReq<>();
        req.setPars(new MentionSmsCheckReq(UserSp.get().getMobile()));
        ok.get().postJson(HttpUrl.smsCode, new OkRequestCallback<CommonResp>() {
            @Override
            public void onResponse(CommonResp data) {
                if (isFinish) return;
//                if (data.getCode().equals("ok")){
//
//                }
                Tools.showToast(data.getRetMsg());
            }

            @Override
            public void onError(int code, Exception e) {
                if (isFinish) return;
                Tools.showToast(code + ",发送失败,稍后重试！");
            }
        }, req, null);
    }

    private CountDownTimer timer;

    private void downTime() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (dialog_mention_timedown == null) return;
                dialog_mention_timedown.setText((millisUntilFinished / 1000) + "s");
                dialog_mention_timedown.setEnabled(false);
                dialog_mention_timedown.setClickable(false);
                dialog_mention_timedown.setTextColor(Color.parseColor("#8C9CA9"));
            }

            @Override
            public void onFinish() {
                if (dialog_mention_timedown == null) return;
                dialog_mention_timedown.setText("获取验证码");
                dialog_mention_timedown.setEnabled(true);
                dialog_mention_timedown.setClickable(true);
                dialog_mention_timedown.setTextColor(ContextCompat.getColor(BaseApp.getInstance(), R.color.colorAccent));
            }
        }.start();
    }

    private void stopTimedown() {
        if (timer != null) {
            timer.onFinish();
            timer.cancel();
            timer = null;
        }
    }
}
