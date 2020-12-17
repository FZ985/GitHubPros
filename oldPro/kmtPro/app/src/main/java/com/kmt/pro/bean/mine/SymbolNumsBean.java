package com.kmt.pro.bean.mine;

import android.text.TextUtils;

import com.kmt.pro.utils.Check;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-21 12:06
 **/
public class SymbolNumsBean implements Serializable {

    private String tokenSymbol;
    private String symbol_num;
    private String symbol_id;

    public String getSymbolNum() {
        return Check.getStringNumber(symbol_num);
    }

    public String getTokenSymbol() {
        if (TextUtils.isEmpty(tokenSymbol)) {
            tokenSymbol = "";
        }
        return tokenSymbol;
    }
}
