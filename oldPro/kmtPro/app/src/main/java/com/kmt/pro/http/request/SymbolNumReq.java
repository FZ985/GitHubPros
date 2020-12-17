package com.kmt.pro.http.request;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-21 12:02
 **/
public class SymbolNumReq extends UserIdReq implements Serializable {
    private String symbol_id;

    public SymbolNumReq(String symbol_id) {
        super();
        this.symbol_id = symbol_id;
    }
}
