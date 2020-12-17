package com.kmt.pro.bean.coin;

import com.kmt.pro.callback.impl.LetterInterface;
import com.kmt.pro.utils.chinese2pinyin.PinYin;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-22 17:07
 **/
public class RechargeSearchBean extends LetterInterface implements Serializable {
    public String symbol_id;
    public String symbol_icon;
    public String tokenName;
    public String tokenSymbol;
    public String serviceCharge;
    public String rate;

    @Override
    public String getFullName() {
        return PinYin.getPinYin(tokenSymbol);
    }


}
