package com.quadcoder.coinpet.page.common;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Phangji on 4/1/15.
 */

public class MyApplication extends Application {

    private static Context mContext;
    private Handler mHandler;
    Handler.Callback callback = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(callback != null)
                    callback.handleMessage(msg);
            }
        };
    }

    public static Context getContext(){
        return mContext;
    }

    public Handler getHandler() {
        return mHandler;
    }

}
