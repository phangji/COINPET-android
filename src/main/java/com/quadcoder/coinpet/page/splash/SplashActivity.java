package com.quadcoder.coinpet.page.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.quadcoder.coinpet.MainActivity;
import com.quadcoder.coinpet.MyApplication;
import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.page.tutorial.TutorialActivity;

public class SplashActivity extends Activity {

    private static final int DELAY_TIME = 1800;
    Handler mHandler = new Handler();
    AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imgvPet = (ImageView)findViewById(R.id.imgvPet);
        anim = (AnimationDrawable)imgvPet.getDrawable();
        anim.start();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, DELAY_TIME);
    }
}
