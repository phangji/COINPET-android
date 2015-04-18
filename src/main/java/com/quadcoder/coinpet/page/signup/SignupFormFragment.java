package com.quadcoder.coinpet.page.signup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.NetworkModel;
import com.quadcoder.coinpet.network.response.Res;

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

    EditText etName;
    RadioGroup group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_form, container, false);

        etAge = (EditText)rootView.findViewById(R.id.etAge);
        etName = (EditText)rootView.findViewById(R.id.etName);
        prev = (ImageButton)rootView.findViewById(R.id.imgbtnPrev);
        next = (ImageButton)rootView.findViewById(R.id.imgbtnNext);
        group = (RadioGroup)rootView.findViewById(R.id.group);

        Button btn = (Button)rootView.findViewById(R.id.btnNext);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pn = PropertyManager.getInstance().getPn();
                String name = etName.getText().toString();
                String gender = "남";
                if (group.getCheckedRadioButtonId() == R.id.radioFemale) {
                    gender = "여";
                }

                int age = Integer.parseInt(etAge.getText().toString());

                NetworkModel.getInstance().signup(getActivity(), pn, name, gender, age, new NetworkModel.OnNetworkResultListener<Res>() {
                    @Override
                    public void onResult(Res res) {
                        PropertyManager.getInstance().setToken(res.Authorization);

                        NetworkModel.getInstance().setGoal(getActivity(), 1, "롯데월드 가서 츄러스! ", "2015-05-21", 20000, 0, new NetworkModel.OnNetworkResultListener<Res>() {
                            @Override
                            public void onResult(Res res) {
                                getActivity().finish();
                            }

                            @Override
                            public void onFail(Res res) {

                            }
                        });

                    }

                    @Override
                    public void onFail(Res res) {
                        Toast.makeText(getActivity(), "error: " + res.error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


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
