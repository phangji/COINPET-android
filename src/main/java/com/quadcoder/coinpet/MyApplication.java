package com.quadcoder.coinpet;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Phangji on 4/1/15.
 */

public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sAudioManager = (AudioManager)sContext.getSystemService(AUDIO_SERVICE);
    }

    public static Context getContext(){
        return sContext;
    }

    private static AudioManager sAudioManager;

    public static AudioManager getAudioManager() {
//        if(sAudioManager == null) {
//            sAudioManager = (AudioManager)getContext().getSystemService(AUDIO_SERVICE);
//        }
        return sAudioManager;
    }
}
