package com.kmt.pro.event;

/**
 * Create by JFZ
 * date: 2020-08-05 16:06
 **/
public class ConfimOrderEvent {
    private int type = -1;

    public ConfimOrderEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
