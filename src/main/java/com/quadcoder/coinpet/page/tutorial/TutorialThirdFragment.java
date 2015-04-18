package com.quadcoder.coinpet.page.tutorial;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.bluetooth.BluetoothService;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.page.signup.SignupActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialThirdFragment extends Fragment {


    public TutorialThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial_third, container, false);

        makePnPsg();
        BluetoothService.getInstance(getActivity(), new Handler()).write(pnMsg.getBytes());


        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignupActivity.class));
                getActivity().finish();
            }
        });

        return rootView;
    }

    String pnMsg = null;

    void makePnPsg() {
        final char[] registerPn = new char[20];
        registerPn[0] = 'S';
        registerPn[1] = 0x01;
        registerPn[2] = 16;
        registerPn[19] = 'E';
        char[] pn = PropertyManager.getInstance().getPn().toCharArray();
        for(int i=3; i<19; i++) {
            registerPn[i] = pn[i-3];
        }
        pnMsg = new String(registerPn);
        Log.d("registerPn", pnMsg);
    }


}
