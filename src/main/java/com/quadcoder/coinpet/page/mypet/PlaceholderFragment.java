package com.quadcoder.coinpet.page.mypet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Goal;
import com.quadcoder.coinpet.page.common.GoalSettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceholderFragment extends Fragment {


    public PlaceholderFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(MyPetActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    TextView tvGoal;
    ImageButton imgbtnEdit;
    TextView tvGoalMoney;
    TextView tvNowMoney;
    TextView tvHowMore;
    ProgressBar pbarMore;
    TextView tvGoalDate;
    TextView tvUntilGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_pet, container, false);
        tvGoal = (TextView)rootView.findViewById(R.id.tvGoal);
        imgbtnEdit = (ImageButton)rootView.findViewById(R.id.imgbtnEdit);
        tvGoalMoney = (TextView)rootView.findViewById(R.id.tvGoalMoney);
        tvNowMoney = (TextView)rootView.findViewById(R.id.tvNowMoney);
        tvHowMore = (TextView)rootView.findViewById(R.id.tvHowMore);
        pbarMore = (ProgressBar)rootView.findViewById(R.id.pbarMore);
        tvGoalDate = (TextView)rootView.findViewById(R.id.tvGoalDate);
        tvUntilGoal = (TextView)rootView.findViewById(R.id.tvUntilGoal);


        imgbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GoalSettingActivity.class));
            }
        });

        if( PropertyManager.getInstance().mGoal == null) {
            NetworkManager.getInstance().getCurrentGoal(getActivity(), new NetworkManager.OnNetworkResultListener<Goal>() {
                @Override
                public void onResult(Goal res) {
                    PropertyManager.getInstance().mGoal = res;
                    setGoalData();
                }

                @Override
                public void onFail(Goal res) {

                }
            });
        } else {
            setGoalData();
        }

        return rootView;
    }

    private void setGoalData() {
        Goal goal = PropertyManager.getInstance().mGoal;
        tvGoal.setText(goal.content);
        tvGoalMoney.setText("목표 금액   " + goal.goal_cost + "원");
        tvNowMoney.setText("현재 금액   " + goal.now_cost + "원");
        tvHowMore.setText("목표까지 " + (goal.goal_cost - goal.now_cost) + "원 남았어요");
        pbarMore.setMax(goal.goal_cost);
        pbarMore.setProgress(goal.now_cost);
        tvGoalDate.setText("D-" + goal.plus);
        tvUntilGoal.setText(goal.goal_date.substring(0, 10));
        if(goal.now_cost < goal.goal_cost) {
            imgbtnEdit.setVisibility(View.INVISIBLE);
        }
    }


}
