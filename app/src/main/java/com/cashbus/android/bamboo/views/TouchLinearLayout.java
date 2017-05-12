package com.cashbus.android.bamboo.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.cashbus.android.bamboo.interfaces.HandleDialogListener;


/**
 * Created by cashbus on 6/24/16.
 */
public class TouchLinearLayout extends LinearLayout {
    public TouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    long touchTime = 0;

    public boolean touchAble = true, showPressed = false, ontouchArea = true, isStop = false;

    public boolean isTouchAble() {
        return touchAble;
    }

    public void setTouchAble(boolean touchAble) {
        this.touchAble = touchAble;
    }

    public TouchLinearLayout(Context context) {
        super(context);
    }

    public TouchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    HandleDialogListener handleDialogListener;

    public void setHandleDialogListener(HandleDialogListener handleDialogListener) {
        this.handleDialogListener = handleDialogListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (!isTouchAble()) {
            return super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            touchTime = System.currentTimeMillis();
            showPressed = false;
            ontouchArea = true;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (touchTime != 0 && !isStop) {
//                        try {
//                            Thread.sleep(10);
//                            if ((System.currentTimeMillis() - touchTime > 10) & ontouchArea) {
//                                handler.sendEmptyMessage(1);
//                                break;
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
            return true;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            if (showPressed)
        } else {
            ontouchArea = false;
            if (System.currentTimeMillis() - touchTime <= 400 && ev.getAction() == MotionEvent.ACTION_UP) {

                if (handleDialogListener != null){
                    handleDialogListener.handle();
                }

            }
//                setBackgroundResource(R.color.profile_item_press);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        handler.sendEmptyMessage(2);
//                    }
//                }).start();
//            } else
            touchTime = 0;
        }
        return super.dispatchTouchEvent(ev);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showPressed = true;
                touchTime = 0;
            } else {
            }

        }
    };
}
