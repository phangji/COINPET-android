package com.quadcoder.coinpet.page.tutorial;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quadcoder.coinpet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialFirstFragment extends Fragment {


    public TutorialFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial_first, container, false);



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
