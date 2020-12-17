package com.kmt.pro.bean.coin;

import com.kmt.pro.utils.Check;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-07-23 15:07
 **/
public class RecordDetailBean implements Serializable {

    public String id;
    public String user_id;
    public String symbol_id;
    private String value;
    public String blockHash;
    public String from;
    public String to;
    public String contractAddress;
    public String tokenSymbol;
    public String tokenDecimal;
    public String gas;
    public String confirmations;
    public String created_at;
    public String updated_at;
    public String serviceCharge;
    public int status;

    public String getValue() {
        return Check.getStringNumber(value);
    }
}
