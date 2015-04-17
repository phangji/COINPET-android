package com.quadcoder.coinpet.page.signup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.quadcoder.coinpet.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SignupFormFragment extends Fragment {


    public SignupFormFragment() {
        // Required empty public constructor
    }
    ImageButton prev;
    ImageButton next;
    EditText etAge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_form, container, false);
        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        etAge = (EditText)rootView.findViewById(R.id.etAge);
        prev = (ImageButton)rootView.findViewById(R.id.imgbtnPrev);
        next = (ImageButton)rootView.findViewById(R.id.imgbtnNext);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(etAge.getText().toString());
                num--;
                etAge.setText(num + "");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(etAge.getText().toString());
                num++;
                etAge.setText(num + "");
            }
        });

        return rootView;
    }


}
