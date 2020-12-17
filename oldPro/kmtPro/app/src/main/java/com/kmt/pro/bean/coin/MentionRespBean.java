package com.kmt.pro.bean.coin;


import com.kmt.pro.utils.Check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-04-15 15:28
 **/
public class MentionRespBean implements Serializable {


    public Symbol symbol;

    private List<SymbolRespBean> tab;

    public List<SymbolRespBean> getTab() {
        if (tab == null) {
            tab = new ArrayList<>();
        }
        return tab;
    }

    public static class Symbol implements Serializable {
        public String user_id;
        private String symbol_num;
        public String tokenSymbol;
        private String serviceCharge;
        public String rate;

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setSymbol_num(String symbol_num) {
            this.symbol_num = symbol_num;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getSymbol_num() {
            return Check.getStringNumber(symbol_num);
        }

        public String getServiceCharge() {
            return Check.getStringNumber(serviceCharge);
        }
    }
}
