package com.quadcoder.coinpet.page.freinds;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.audio.AudioEffect;
import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.model.Friend;

import java.util.ArrayList;

public class FriendsActivity extends ActionBarActivity {

    ImageView imgvMM;
    ImageView imgvTT;
    ImageView imgvKuKu;
    ImageView imgvDD;
    ImageView imgvKoKo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setLayouts();
        setClickListeners();
    }

    final AudioEffect kukuAudio = new AudioEffect(AudioEffect.FREIND_KUKU);
    final AudioEffect chchAudio = new AudioEffect(AudioEffect.FREIND_CHCH);
    final AudioEffect ddAudio = new AudioEffect(AudioEffect.FREIND_DD);
    final AudioEffect kokoAudio = new AudioEffect(AudioEffect.FREIND_KOKO);
    final AudioEffect mamaAudio = new AudioEffect(AudioEffect.FREIND_MAMA);

    private void setLayouts() {
        imgvMM = (ImageView) findViewById(R.id.imgvMM);
        imgvTT = (ImageView) findViewById(R.id.imgvTT);
        imgvKuKu = (ImageView) findViewById(R.id.imgvKuKu);
        imgvDD = (ImageView) findViewById(R.id.imgvDD);
        imgvKoKo = (ImageView) findViewById(R.id.imgvKK);

        ArrayList<Friend> friendList = DBManager.getInstance().getFriendList();
    }

    private void setClickListeners() {
        imgvMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kukuAudio.play();
            }
        });

        imgvTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chchAudio.play();
            }
        });

        imgvKuKu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mamaAudio.play();
            }
        });

        imgvDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddAudio.play();
            }
        });

        imgvKoKo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kokoAudio.play();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
