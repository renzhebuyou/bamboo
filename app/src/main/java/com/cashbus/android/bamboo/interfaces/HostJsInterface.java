package com.cashbus.android.bamboo.interfaces;

import android.webkit.JavascriptInterface;

import retrofit2.http.PUT;

/**
 * Created by zenghui on 2017/5/6.
 */

public interface HostJsInterface {
    @JavascriptInterface
    void setToolbarStatus(String jsonString);

    @JavascriptInterface
    void alert(String string);

    @JavascriptInterface
    void credentialError();
}
