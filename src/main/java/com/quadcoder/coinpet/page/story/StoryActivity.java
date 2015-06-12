package com.quadcoder.coinpet.page.story;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.audio.AudioEffect;
import com.quadcoder.coinpet.page.common.Utils;
import com.quadcoder.coinpet.page.signup.SignupActivity;

public class StoryActivity extends Activity {


    ImageView imgvStory;
    TextView tvStory;

    int pageIndex = 0;


    int imgStory[] = {R.drawable.story_bg1, R.drawable.story_bg2, R.drawable.story_bg3,
            R.drawable.story_bg4, R.drawable.story_bg5};

    int textStory[] = {R.string.story_text1, R.string.story_text2, R.string.story_text3,
            R.string.story_text4, R.string.story_text5};

    int audioStory[] = {AudioEffect.STORY1, AudioEffect.STORY2, AudioEffect.STORY3,
            AudioEffect.STORY4, AudioEffect.STORY5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View main = getLayoutInflater().inflate(R.layout.activity_story, null);
        setContentView(main);

        imgvStory = (ImageView)findViewById(R.id.imgvStory);
        tvStory = (TextView)findViewById(R.id.tvStory);

        Utils.getInstance().overrideFonts(this, main);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageIndex++;
                goToNextStory(pageIndex);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        nowPlaying.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(nowPlaying != null)
            nowPlaying.stop();
    }

    AudioEffect nowPlaying = new AudioEffect(AudioEffect.STORY1);

    private void goToNextStory(int index) {
        if(index == 5) {
            startActivity(new Intent(StoryActivity.this, SignupActivity.class));
            finish();
        } else {
            imgvStory.setImageResource(imgStory[index]);
            tvStory.setText(textStory[index]);
            nowPlaying.stop();
            nowPlaying = new AudioEffect(audioStory[index]);
            nowPlaying.play();
        }

    }
}
