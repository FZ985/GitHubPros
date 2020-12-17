package com.kmt.pro.bean.coin;

import com.kmt.pro.utils.Check;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-15 13:13
 **/
public class CoinRecordBean implements Serializable {

    public String blockHash;
    public String tokenDecimal;
    private String value;
    public String created_at;
    public int status = -1;

    public String getValue() {
        return Check.getStringNumber(value);
    }
}
