package com.quadcoder.coinpet.page.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.quadcoder.coinpet.page.main.MainActivity;
import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.Quiz;
import com.quadcoder.coinpet.model.SystemQuest;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Goal;
import com.quadcoder.coinpet.network.response.UpdatedData;
import com.quadcoder.coinpet.page.tutorial.TutorialActivity;

public class SplashActivity extends Activity {

    private static final int DELAY_TIME = 800;
    Handler mHandler = new Handler();
    AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        devSetInitialData();

        ImageView imgvPet = (ImageView)findViewById(R.id.imgvPet);
        anim = (AnimationDrawable)imgvPet.getDrawable();
        anim.start();

        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ( mWifi.isConnected() || connManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || connManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ) {

            keepRunning();

            // 출석 체크
            PropertyManager.getInstance().plusAccessCount();

        }
        else {
            showDialog();
        }

//        goToNext();
    }

    private void keepRunning() {
        if(PropertyManager.getInstance().getToken() != null)
            checkUpdatedData();
        else {
            startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
            finish();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("인터넷 연결");
        builder.setMessage("인터넷을 연결해주세요!");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    private void getGoalData() {
        NetworkManager.getInstance().getCurrentGoal(this, new NetworkManager.OnNetworkResultListener<Goal>() {
            @Override
            public void onResult(Goal res) {
                PropertyManager.getInstance().mGoal = res;
                goToNext();
            }

            @Override
            public void onFail(Goal res) {
                goToNext();
            }
        });
    }

    private void goToNext() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                startActivity(new Intent(SplashActivity.this, StoryActivity.class));
                finish();
            }
        }, DELAY_TIME);
    }

    private void devSetInitialData() {
        PropertyManager.getInstance().setNowPoint(0);
        PropertyManager.getInstance().setNowLevel(1);
        PropertyManager.getInstance().setPkQuiz(0);
        PropertyManager.getInstance().setPkQuest(0);
        PropertyManager.getInstance().setPkPQuest(0);
        PropertyManager.getInstance().setqCoin(3);
    }

    private void checkUpdatedData() {
        int pkQuiz = PropertyManager.getInstance().getPkQuiz();
        int pkQuest = PropertyManager.getInstance().getPkPQuest();
        int pkParentQuest = PropertyManager.getInstance().getPkPQuest();

        Log.d("getUpdatedData", "pkQuiz: " + pkQuiz + "\tpkQuest: " + pkQuest + "\tpkParentQuest: " + pkParentQuest);
        NetworkManager.getInstance().getUpdatedData(this, pkQuiz, pkQuest, new NetworkManager.OnNetworkResultListener<UpdatedData>() {
            @Override
            public void onResult(UpdatedData res) {

                getGoalData();

                if (res.needUpdate) {
                    if (res.systemQuiz.size() != 0) {
                        for (Quiz record : res.systemQuiz) {
                            DBManager.getInstance().insertQuiz(record); // STATE_YET
                        }
                        Quiz last = res.systemQuiz.get(res.systemQuiz.size() - 1);
                        PropertyManager.getInstance().setPkQuiz(last.pk_std_quiz);
                    }
                    if (res.systemQuest.size() != 0) {
                        for (SystemQuest record : res.systemQuest) {
                            SystemQuest newOne = record;
                            newOne.state = Quest.DOING;
                            DBManager.getInstance().insertSystemQuest(newOne);
                        }
                        SystemQuest last = res.systemQuest.get(res.systemQuest.size() - 1);
                        PropertyManager.getInstance().setPkQuest(last.pk_std_que);

                        checkAndMakeActiveQuest();
                    }
                    if (res.parentsQuest.size() != 0) {
                        for (ParentQuest record : res.parentsQuest) {
                            if (record.state == Quest.DOING)
                                DBManager.getInstance().insertParentQuest(record);
                            else
                                DBManager.getInstance().updateParentQuest(record);
                        }
                        ParentQuest last = res.parentsQuest.get(res.parentsQuest.size() - 1);
                        PropertyManager.getInstance().setPkPQuest(last.pk_parents_quest);
                    }
                }

            }

            @Override
            public void onFail(UpdatedData res) {
                goToNext();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelRequests(this);
    }

    void checkAndMakeActiveQuest() {
        int count = DBManager.getInstance().getActiveSystemCount();
         while( (3 - count) > 0) {
             DBManager.getInstance().createNewActiveSystemQuest();
             count++;
         }
    }
}
