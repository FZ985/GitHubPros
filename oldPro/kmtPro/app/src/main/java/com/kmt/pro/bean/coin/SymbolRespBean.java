package com.kmt.pro.bean.coin;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-04-15 12:49
 **/
public class SymbolRespBean implements Serializable {
    public String address;
    public String user_id;
    public String symbol_type;
    public String explain;
    public String tab;

    public String network_id;

    //本地逻辑变量
    public boolean check;
}
