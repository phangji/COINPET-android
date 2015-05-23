package com.quadcoder.coinpet.audio;

import android.media.MediaPlayer;

import com.quadcoder.coinpet.MyApplication;
import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;

/**
 * Created by Phangji on 5/15/15.
 */
public class AudioEffect {

    enum PlayState {
        IDLE, INITIIALIZED, STARTED, STOPPED, END
        // PREPARED, PAUSED
    }

    PlayState mState;
    MediaPlayer mPlayer;

    // Audio Resources
    public static final int CARTOON_BOING = R.raw.cartoon_boing;


    public AudioEffect(int effect) {
        mState = PlayState.IDLE;
        mPlayer = MediaPlayer.create(MyApplication.getContext(), effect);
    }

    public void play() {
        mState = PlayState.INITIIALIZED;

        if(PropertyManager.getInstance().isSound())
        if(mState == PlayState.END || mState == PlayState.INITIIALIZED) {
            mPlayer.start();
            mState = PlayState.STARTED;
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mState = PlayState.END;
                }
            });
        }
    }

    public void stop() {
        if(mState == PlayState.STARTED) {
            mPlayer.stop();
            mState = PlayState.STOPPED;
        }
    }
}
