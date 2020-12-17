package com.kmt.pro.http.request;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-15 14:47
 **/
public class BlockHashReq implements Serializable {

    private String blockHash;

    public BlockHashReq(String blockHash) {
        this.blockHash = blockHash;
    }
}
