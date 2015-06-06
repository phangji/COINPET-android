package com.quadcoder.coinpet.page.signup;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.quadcoder.coinpet.MainActivity;
import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Res;
import com.quadcoder.coinpet.page.common.Constants;
import com.quadcoder.coinpet.page.story.StoryActivity;

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
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_NORMAL);

        imgvCloud1 = (ImageView)rootView.findViewById(R.id.imgvCloud1);
        imgvCloud2 = (ImageView)rootView.findViewById(R.id.imgvCloud2);

        etAge = (EditText)rootView.findViewById(R.id.etAge);
        etName = (EditText)rootView.findViewById(R.id.etName);
        prev = (ImageButton)rootView.findViewById(R.id.imgbtnPrev);
        next = (ImageButton)rootView.findViewById(R.id.imgbtnNext);
        group = (RadioGroup)rootView.findViewById(R.id.group);

        etAge.setTypeface(font);
        etName.setTypeface(font);

        RadioButton radiobtn = (RadioButton)rootView.findViewById(R.id.radioMale);
        radiobtn.setTypeface(font);
        radiobtn = (RadioButton)rootView.findViewById(R.id.radioFemale);
        radiobtn.setTypeface(font);


        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setTypeface(font);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pn = PropertyManager.getInstance().getPn();
                String name = etName.getText().toString();
                String gender = "남";
                if (group.getCheckedRadioButtonId() == R.id.radioFemale) {
                    gender = "여";
                }

                int age = 0;
                if(etAge.getText().toString().length() != 0)
                    age = Integer.parseInt(etAge.getText().toString());

                if(name.length() != 0 && age != 0) {
                    NetworkManager.getInstance().signup(getActivity(), pn, name, gender, age, new NetworkManager.OnNetworkResultListener<Res>() {
                        @Override
                        public void onResult(Res res) {
                            PropertyManager.getInstance().setToken(res.Authorization);
                            startActivity(new Intent(getActivity(), StoryActivity.class));
                            getActivity().finish();
                        }

                        @Override
                        public void onFail(Res res) {
                            Toast.makeText(getActivity(), "error: " + res.error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    showDialog();
                }
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

    @Override
    public void onResume() {
        super.onResume();
        startAnimation();
    }

    ImageView imgvCloud1;
    ImageView imgvCloud2;

    void startAnimation() {

        Animation animCloud1 = AnimationUtils.loadAnimation(getActivity(), R.anim.cloud1_anim);
        imgvCloud1.startAnimation(animCloud1);
        Animation animCloud2 = AnimationUtils.loadAnimation(getActivity(), R.anim.cloud2_anim);
        imgvCloud2.startAnimation(animCloud2);

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("친구 맺기");
        builder.setMessage("빈칸을 채워서 코인펫과 친구를 맺어보세요.");
        builder.setPositiveButton("응", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }


}
