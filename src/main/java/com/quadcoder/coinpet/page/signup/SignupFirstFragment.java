package com.quadcoder.coinpet.page.signup;


import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
    public void onResume() {
        super.onResume();
        startAnimation();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_first, container, false);

        imgvPet = (ImageView)rootView.findViewById(R.id.imgvPet);
        imgvBox = (ImageView)rootView.findViewById(R.id.imgvBox);

        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_NORMAL);
        btn.setTypeface(font);

        imgvCloud1 = (ImageView)rootView.findViewById(R.id.imgvCloud1);
        imgvCloud2 = (ImageView)rootView.findViewById(R.id.imgvCloud2);

        // TODO : Text로 변경
        final Integer idx = new Integer(0);
        final int[] resources = { R.drawable.story1, R.drawable.story2, R.drawable.story3, R.drawable.story4, R.drawable.story5, R.drawable.talk1, R.drawable.talk2 };

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idx.intValue() == 6) {
                    ((SignupActivity)getActivity()).onNextClicked();
                }
                else {
                    imgvPet.setImageResource(R.drawable.pet_right);
                    imgvBox.setImageResource(resources[idx.intValue()]);
                    isClicked = true;
                }
            }
        });

        return rootView;
    }
    ImageView imgvCloud1;
    ImageView imgvCloud2;

    void startAnimation() {

        Animation animCloud1 = AnimationUtils.loadAnimation(getActivity(), R.anim.cloud1_anim);
        imgvCloud1.startAnimation(animCloud1);
        Animation animCloud2 = AnimationUtils.loadAnimation(getActivity(), R.anim.cloud2_anim);
        imgvCloud2.startAnimation(animCloud2);

    }
}
