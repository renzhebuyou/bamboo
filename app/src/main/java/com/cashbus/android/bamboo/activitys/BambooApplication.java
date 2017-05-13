package com.cashbus.android.bamboo.activitys;

import android.app.Application;
import android.content.Context;

/**
 * Created by zenghui on 2017/5/5.
 */

public class BambooApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getInstance(){
        return context;
    };
}
