package com.quadcoder.coinpet.page.signup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.network.NetworkManager;

public class SignupActivity extends FragmentActivity {

    FragmentManager mFM;
    Fragment[] list = { new SignupFirstFragment(), new SignupFormFragment() };
    static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFM = getSupportFragmentManager();

        if (savedInstanceState == null) {
            SignupFirstFragment f = new SignupFirstFragment();
            FragmentTransaction ft = mFM.beginTransaction();
            ft.add(R.id.container, f);
            ft.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelRequests(SignupActivity.this);
    }

    void onNextClicked() {
        int count = mFM.getBackStackEntryCount();
        Log.d(TAG, "" + count);
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
