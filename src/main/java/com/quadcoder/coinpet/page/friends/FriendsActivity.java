package com.quadcoder.coinpet.page.friends;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import com.quadcoder.coinpet.page.common.Utils;

import java.util.ArrayList;

public class FriendsActivity extends ActionBarActivity {

    FrameLayout mainFrame;
    ArrayList<Friend> friendList;
    ImageView[] imageList;

    int[] friendsResId = { R.drawable.f_mm, R.drawable.f_kuku, R.drawable.f_tt, R.drawable.f_dd, R.drawable.f_kk };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);

        imageList = new ImageView[5];
        imageList[0] = (ImageView) findViewById(R.id.imgvMM);
        imageList[1] = (ImageView) findViewById(R.id.imgvKuKu);
        imageList[2] = (ImageView) findViewById(R.id.imgvTT);
        imageList[3] = (ImageView) findViewById(R.id.imgvDD);
        imageList[4] = (ImageView) findViewById(R.id.imgvKK);

        FrameLayout[] frameTouchList = { (FrameLayout)findViewById(R.id.frameMM), (FrameLayout)findViewById(R.id.frameKuKu),
                (FrameLayout)findViewById(R.id.frameTT), (FrameLayout)findViewById(R.id.frameDD), (FrameLayout)findViewById(R.id.frameKK)};

        setClickListeners(frameTouchList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLayouts();
    }

    final AudioEffect[] effectList = {new AudioEffect(AudioEffect.FREIND_MAMA), new AudioEffect(AudioEffect.FREIND_KUKU),
            new AudioEffect(AudioEffect.FREIND_CHCH), new AudioEffect(AudioEffect.FREIND_DD), new AudioEffect(AudioEffect.FREIND_KOKO)};

    private void setLayouts() {
        friendList = DBManager.getInstance().getFriendList();
        for(int i=0; i<5; i++) {
            Friend friend = friendList.get(i);
            if(friend.isSaved) {
                imageList[i].setImageResource(friendsResId[i]);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean isBoxup;
    View upBox;
    private void setClickListeners(FrameLayout[] frameTouchList) {

        for(int i=0; i<5; i++) {
            ImageView fView = imageList[i];
            final int idx = i;
            frameTouchList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( !isBoxup ) {
                        effectList[idx].play();
                        upBox = getLayoutInflater().inflate(R.layout.friend_box_layout, null);
                        Utils.getInstance().overrideFonts(FriendsActivity.this, upBox);
                        //set view
                        TextView tvTitle = (TextView) upBox.findViewById(R.id.tvTitle);
                        TextView tvDesp = (TextView) upBox.findViewById(R.id.tvDesp);
                        TextView tvCond = (TextView) upBox.findViewById(R.id.tvCondition);
                        ImageView imgvPet = (ImageView) upBox.findViewById(R.id.imgvPet);
                        ImageView imgvCancle = (ImageView) upBox.findViewById(R.id.imgvCancle);

                        Friend friend = friendList.get(idx);
                        friend.resId = friendsResId[idx];   // for safety
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
}
