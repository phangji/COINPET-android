package com.quadcoder.coinpet.page.setting;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;

public class SettingActivity extends Activity {

    ToggleButton tbtnSound;
    boolean isChanged;
    FrameLayout layClose;

    static AudioManager mAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        layClose = (FrameLayout)findViewById(R.id.layClose);
        tbtnSound = (ToggleButton)findViewById(R.id.tbtnSound);
        tbtnSound.setChecked(PropertyManager.getInstance().isSound());

        tbtnSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChanged = true;
            }
        });

        Log.d("phangji", "onCreate");

        layClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("phangji", "onResume");
        mAM = (AudioManager)getApplicationContext().getSystemService(AUDIO_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("phangji", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("phangji", "onStop");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("phangji", "onPause");

        boolean isSound = tbtnSound.isChecked();
        PropertyManager.getInstance().setSound(isSound);
    }

    @Override
    protected void onDestroy() {    //액티비티가 다시 실행되거나 되게 시간 지나서 stop-->destroy 후 create
        Log.d("phangji", "onDestroy");
        super.onDestroy();
    }
}
