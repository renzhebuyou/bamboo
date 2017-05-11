package com.cashbus.android.bamboo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by zenghui on 15/11/29.
 */
public class LinearGradientLayout extends LinearLayout {
    public LinearGradientLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public LinearGradientLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public LinearGradientLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }
    int[] linearGradientColors;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getWidth() <= 0 || linearGradientColors == null){
            return;
        }
        LinearGradient linearGradient = new LinearGradient(getWidth()/2, 0, getWidth()/2, getHeight(), linearGradientColors, null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(linearGradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(),paint);

    }


    public void setLinearGradientColor(int[] linearGradientColors) {
        this.linearGradientColors = linearGradientColors;
        invalidate();
    }
}
