package com.cashbus.android.bamboo.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by zenghui on 7/8/16.
 */
public class ExpandAnimation extends Animation {
    private final int mStartHeight;
    private final int mDeltaHeight;
    private View mContent;
    public ExpandAnimation(int startHeight, int endHeight, View mContent) {
        mStartHeight = startHeight;
        mDeltaHeight = endHeight - startHeight;
        this.mContent = mContent;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        android.view.ViewGroup.LayoutParams lp = mContent.getLayoutParams();
        lp.height = (int) (mStartHeight + mDeltaHeight * interpolatedTime);
        mContent.setLayoutParams(lp);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
