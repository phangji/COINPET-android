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

    /**
     * Audio Resources
     */

    public static final int CARTOON_BOING = R.raw.cartoon_boing;
    public static final int LEVEL_UP = R.raw.levelup;
    public static final int POINT_UP = R.raw.pointup;
    public static final int HAHAHA = R.raw.hahaha;
    public static final int HMMM = R.raw.hmmm;
    public static final int GOOD_JOB = R.raw.goodjob;
    public static final int FREIND_KUKU = R.raw.f_kuku;
    public static final int FREIND_DD = R.raw.f_dd;
    public static final int FREIND_KOKO = R.raw.f_koko;
    public static final int FREIND_CHCH = R.raw.f_chch;
    public static final int FREIND_MAMA = R.raw.f_mama;

    public static final int STORY1 = R.raw.audio_story1;
    public static final int STORY2 = R.raw.audio_story2;
    public static final int STORY3 = R.raw.audio_story3;
    public static final int STORY4 = R.raw.audio_story4;
    public static final int STORY5 = R.raw.audio_story5;

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
