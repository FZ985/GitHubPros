package com.kmt.pro.web;

/**
 * Create by JFZ
 * date: 2020-07-07 11:51
 **/
public interface HtmlCall {

    boolean canGoBack();

    void goBack();

    void finishThis();

    void hideShareButton();

    void load(String method);

}
