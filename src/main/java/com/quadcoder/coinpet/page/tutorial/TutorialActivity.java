package com.quadcoder.coinpet.page.tutorial;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.network.NetworkManager;

public class TutorialActivity extends FragmentActivity {

    FragmentManager mFM;
    Fragment[] list = { new TutorialFirstFragment(), new TutorialSecondFragment(), new TutorialThirdFragment() };
    static final String TAG = "TutorialActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mFM = getSupportFragmentManager();

        if (savedInstanceState == null) {
            TutorialFirstFragment f = new TutorialFirstFragment();
            FragmentTransaction ft = mFM.beginTransaction();
            ft.add(R.id.container, f);
            ft.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelRequests(TutorialActivity.this);
    }

    void onNextClicked() {
        int count = mFM.getBackStackEntryCount();
        Log.d(TAG, ""+count);
        if (count < list.length) {
            Fragment f = list[count+1];
            Log.d(TAG, "Next : "+list[count].getClass().getName());
            FragmentTransaction ft = mFM.beginTransaction();
            ft.replace(R.id.container, f);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            mFM.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
