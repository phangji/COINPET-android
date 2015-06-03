package com.quadcoder.coinpet.page.freinds;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.audio.AudioEffect;
import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.model.Friend;

import java.util.ArrayList;

public class FriendsActivity extends ActionBarActivity {

    FrameLayout mainFrame;
    ImageView imgvMM;
    ImageView imgvTT;
    ImageView imgvKuKu;
    ImageView imgvDD;
    ImageView imgvKoKo;
    ArrayList<Friend> friendList;
    ArrayList<ImageView> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        imgvMM = (ImageView) findViewById(R.id.imgvMM);
        imgvTT = (ImageView) findViewById(R.id.imgvTT);
        imgvKuKu = (ImageView) findViewById(R.id.imgvKuKu);
        imgvDD = (ImageView) findViewById(R.id.imgvDD);
        imgvKoKo = (ImageView) findViewById(R.id.imgvKK);

        imageList = new ArrayList<>(5);
        imageList.add(imgvMM);  imageList.add(imgvKuKu);  imageList.add(imgvTT);  imageList.add(imgvDD);  imageList.add(imgvKoKo);

        setClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setLayouts();
    }

    final AudioEffect kukuAudio = new AudioEffect(AudioEffect.FREIND_KUKU);
    final AudioEffect chchAudio = new AudioEffect(AudioEffect.FREIND_CHCH);
    final AudioEffect ddAudio = new AudioEffect(AudioEffect.FREIND_DD);
    final AudioEffect kokoAudio = new AudioEffect(AudioEffect.FREIND_KOKO);
    final AudioEffect mamaAudio = new AudioEffect(AudioEffect.FREIND_MAMA);

    final AudioEffect[] effectList = {mamaAudio, kukuAudio, chchAudio, ddAudio, kokoAudio};

    private void setLayouts() {
        friendList = DBManager.getInstance().getFriendList();
        for(int i=0; i<5; i++) {
            Friend friend = friendList.get(i);
            if(friend.isSaved) {
                imageList.get(i).setImageResource(friend.resId);
            }
        }
    }

    boolean isBoxup;
    View upBox;
    private void setClickListeners() {

        for(int i=0; i<5; i++) {
            ImageView fView = imageList.get(i);
            final int idx = i;
            fView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( !isBoxup ) {
                        effectList[idx].play();
                        upBox = getLayoutInflater().inflate(R.layout.friend_box_layout, null);

                        //set view
                        TextView tvTitle = (TextView) upBox.findViewById(R.id.tvTitle);
                        TextView tvDesp = (TextView) upBox.findViewById(R.id.tvDesp);
                        TextView tvCond = (TextView) upBox.findViewById(R.id.tvCondition);
                        ImageView imgvPet = (ImageView) upBox.findViewById(R.id.imgvPet);
                        ImageView imgvCancle = (ImageView) upBox.findViewById(R.id.imgvCancle);

                        Friend friend = friendList.get(idx);
                        tvTitle.setText(friend.name);
                        tvDesp.setText(friend.description);
                            tvCond.setText(friend.condition);
                        imgvPet.setImageResource(friend.resId);
                        imgvCancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewGroup parent = (ViewGroup) upBox.getParent();
                                parent.removeView(upBox);
                                isBoxup = false;
                            }

                        });
                        mainFrame.addView(upBox);
                        isBoxup = true;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(isBoxup) {
            ViewGroup parent = (ViewGroup) upBox.getParent();
            parent.removeView(upBox);
            isBoxup = false;
        } else {
            super.onBackPressed();
        }
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
