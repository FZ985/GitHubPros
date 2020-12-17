package com.kmt.pro.chart.mine;

import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2020-05-21 17:30
 **/
public class MinePieFill implements Serializable {

    public int startColor, endColor;

    public MinePieFill(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }
}
