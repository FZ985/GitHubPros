package com.kmt.pro.http.request;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-15 12:44
 **/
public class SymbolIdReq extends UserIdReq implements Serializable {
    private String symbol_id;

    public SymbolIdReq(String symbol_id) {
        super();
        this.symbol_id = symbol_id;
    }
}
