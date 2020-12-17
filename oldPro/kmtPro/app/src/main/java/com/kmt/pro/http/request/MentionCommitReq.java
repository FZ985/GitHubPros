package com.kmt.pro.http.request;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-15 16:49
 **/
public class MentionCommitReq extends UserIdReq implements Serializable {

    private String symbol_id;
    private String symbol_num;
    private String serviceCharge;
    private String to_address;
    private String network_id;
    private String code;

    public MentionCommitReq(String code, String symbol_id, String symbol_num, String serviceCharge, String to_address, String network_id) {
        super();
        this.code = code;
        this.symbol_id = symbol_id;
        this.symbol_num = symbol_num;
        this.serviceCharge = serviceCharge;
        this.to_address = to_address;
        this.network_id = network_id;
    }
}
