package com.cashbus.android.bamboo.interfaces;

import com.cashbus.android.bamboo.views.ObservableScrollView;

/**
 * Created by zenghui on 2016/9/19.
 */
public interface ScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

}
