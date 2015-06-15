package com.quadcoder.coinpet.page.signup;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.Quiz;
import com.quadcoder.coinpet.model.SystemQuest;
import com.quadcoder.coinpet.network.response.UpdatedData;
import com.quadcoder.coinpet.page.main.MainActivity;
import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Res;
import com.quadcoder.coinpet.page.common.Constants;
import com.quadcoder.coinpet.page.common.Utils;

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
                            checkUpdatedData();
                        }

                        @Override
                        public void onFail(Res res) {
                            Toast.makeText(getActivity(), "error: " + res.error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Utils.getInstance().showYesDialog(getActivity(), "친구 맺기", "빈칸을 채워서 코인펫과 친구를 맺어보세요.");
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

    private void checkUpdatedData() {
        int pkQuiz = PropertyManager.getInstance().getPkQuiz();
        int pkQuest = PropertyManager.getInstance().getPkPQuest();
        int pkParentQuest = PropertyManager.getInstance().getPkPQuest();

        Log.d("getUpdatedData", "pkQuiz: " + pkQuiz + "\tpkQuest: " + pkQuest + "\tpkParentQuest: " + pkParentQuest);
        NetworkManager.getInstance().getUpdatedData(getActivity(), pkQuiz, pkQuest, new NetworkManager.OnNetworkResultListener<UpdatedData>() {
            @Override
            public void onResult(UpdatedData res) {

                if (res.needUpdate) {
                    if (res.systemQuiz.size() != 0) {
                        for (Quiz record : res.systemQuiz) {
                            DBManager.getInstance().insertQuiz(record); // STATE_YET
                        }
                        Quiz last = res.systemQuiz.get(res.systemQuiz.size() - 1);
                        PropertyManager.getInstance().setPkQuiz(last.pk_std_quiz);
                    }
                    if (res.systemQuest.size() != 0) {
                        for (SystemQuest record : res.systemQuest) {
                            SystemQuest newOne = record;
                            newOne.state = Quest.DOING;
                            DBManager.getInstance().insertSystemQuest(newOne);
                        }
                        SystemQuest last = res.systemQuest.get(res.systemQuest.size() - 1);
                        PropertyManager.getInstance().setPkQuest(last.pk_std_que);

                        checkAndMakeActiveQuest();
                    }
                    if (res.parentsQuest.size() != 0) {
                        for (ParentQuest record : res.parentsQuest) {
                            if (record.state == Quest.DOING)
                                DBManager.getInstance().insertParentQuest(record);
                            else
                                DBManager.getInstance().updateParentQuest(record);
                        }
                        ParentQuest last = res.parentsQuest.get(res.parentsQuest.size() - 1);
                        PropertyManager.getInstance().setPkPQuest(last.pk_parents_quest);
                    }
                }

            }

            @Override
            public void onFail(UpdatedData res) {
                goToNext();
            }
        });
    }

    void goToNext() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    void checkAndMakeActiveQuest() {
        int count = DBManager.getInstance().getActiveSystemCount();
        while( (3 - count) > 0) {
            SystemQuest newOne = DBManager.getInstance().createNewActiveSystemQuest();
            count++;

            NetworkManager.getInstance().postQuest(getActivity(), newOne.pk_std_que, Quest.DOING, new NetworkManager.OnNetworkResultListener<Res>() {
                @Override
                public void onResult(Res res) {

                }

                @Override
                public void onFail(Res res) {

                }
            });
        }
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
