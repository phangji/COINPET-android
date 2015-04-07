package com.quadcoder.coinpet;

import android.app.Application;
import android.content.Context;

/**
 * Created by Phangji on 4/1/15.
 */

public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext(){
        return sContext;
    }
}
