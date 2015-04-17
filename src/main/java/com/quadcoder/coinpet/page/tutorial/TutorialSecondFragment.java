package com.quadcoder.coinpet.page.tutorial;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quadcoder.coinpet.R;

public class TutorialSecondFragment extends Fragment {

    public TutorialSecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial_second, container, false);

//        TextView txt = (TextView) rootView.findViewById(R.id.tvGuide);
//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "rixmelangcholly_pro_medium.otf");
//        txt.setTypeface(font);

        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TutorialActivity)getActivity()).onNextClicked();
            }
        });

        return rootView;
    }
}
