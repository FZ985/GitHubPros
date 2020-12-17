package com.kmt.pro.ui.dialog.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.callback.AccountIncomeChooseCall;

/**
 * Create by JFZ
 * date: 2020-07-24 15:26
 **/
public class IncomeScreenPop extends PopupWindow {

    private View rootView;
    private AccountIncomeChooseCall call;

    private TextView pop_all;
    private TextView pop_exchange;
    private TextView pop_trade;
    private TextView pop_checkIn;
    private TextView pop_gongixanshouyi;
    private TextView pop_red;

    public IncomeScreenPop(Context context, AccountIncomeChooseCall call) {
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.pop_income_screen, null);
        setContentView(rootView);
        this.call = call;
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.pop_anim_alpha);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        this.setBackgroundDrawable(dw);
        initData();
    }

    private void initData() {
        pop_all = rootView.findViewById(R.id.pop_all);
        pop_exchange = rootView.findViewById(R.id.pop_exchange);
        pop_trade = rootView.findViewById(R.id.pop_trade);
        pop_checkIn = rootView.findViewById(R.id.pop_checkIn);
        pop_gongixanshouyi = rootView.findViewById(R.id.pop_gongixanshouyi);
        pop_red = rootView.findViewById(R.id.pop_red);

        pop_all.setOnClickListener(v -> {
            if (call != null) call.onChooseType(this, "0");
        });
        pop_exchange.setOnClickListener(v -> {
            if (call != null) call.onChooseType(this, "3");
        });
        pop_trade.setOnClickListener(v -> {
            if (call != null) call.onChooseType(this, "2");
        });
        pop_checkIn.setOnClickListener(v -> {
            if (call != null) call.onChooseType(this, "1");
        });
        pop_gongixanshouyi.setOnClickListener(v -> {
            if (call != null) call.onChooseType(this, "8");
        });
        pop_red.setOnClickListener(v -> {
            if (call != null) call.onChooseType(this, "5");
        });
    }
}
