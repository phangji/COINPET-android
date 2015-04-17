package com.quadcoder.coinpet.page.signup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quadcoder.coinpet.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SignupFirstFragment extends Fragment {


    public SignupFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_first, container, false);

        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SignupActivity)getActivity()).onNextClicked();
            }
        });
    
        return rootView;
    }


}
