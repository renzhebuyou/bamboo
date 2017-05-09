package com.cashbus.android.bamboo.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cashbus.android.bamboo.utils.Common;

/**
 * Created by zenghui on 2017/5/5.
 */

public class BasicActivity extends AppCompatActivity {
    Context mContext;
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (Common.screem_width == 0) {
            WindowManager wm = this.getWindowManager();
            Common.screem_width = wm.getDefaultDisplay().getWidth();
            Common.screem_height = wm.getDefaultDisplay().getHeight();
        }
        mContext = this;
    }
}
