package com.quadcoder.coinpet.page.signup;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.page.common.Constants;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SignupFirstFragment extends Fragment {


    public SignupFirstFragment() {
        // Required empty public constructor
    }

    boolean isClicked;
    ImageView imgvPet;
    ImageView imgvBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_first, container, false);

        imgvPet = (ImageView)rootView.findViewById(R.id.imgvPet);
        imgvBox = (ImageView)rootView.findViewById(R.id.imgvBox);

        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_NORMAL);
        btn.setTypeface(font);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClicked) {
                    ((SignupActivity)getActivity()).onNextClicked();
                }
                else {
                    imgvPet.setImageResource(R.drawable.pet_right);
                    imgvBox.setImageResource(R.drawable.talk2);
                    isClicked = true;
                }
            }
        });

        return rootView;
    }
}
