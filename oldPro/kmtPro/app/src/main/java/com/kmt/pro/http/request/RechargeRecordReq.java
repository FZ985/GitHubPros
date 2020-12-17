package com.kmt.pro.http.request;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-15 12:44
 **/
public class RechargeRecordReq extends UserIdReq implements Serializable {
    private String symbol_id;
    private int page;
    public RechargeRecordReq(int page, String symbol_id) {
        super();
        this.page = page;
        this.symbol_id = symbol_id;
    }
}
