package com.quadcoder.coinpet.page.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.MainActivity;
import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Goal;
import com.quadcoder.coinpet.network.response.Res;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GoalSettingActivity extends Activity {

    EditText etName;
    EditText etGoalMoney;
    EditText etPeriod;
    ImageButton prev;
    ImageButton next;

    public static final String RESULT_GOAL_SET = "result_goal_set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        Typeface font = Typeface.createFromAsset(getAssets(), Constants.FONT_NORMAL);
        ((TextView)findViewById(R.id.textView3)).setTypeface(font);
        ((TextView)findViewById(R.id.textView5)).setTypeface(font);
        ((TextView)findViewById(R.id.textView4)).setTypeface(font);

        etName = (EditText)findViewById(R.id.etName);
        etName.setTypeface(font);

        etGoalMoney = (EditText)findViewById(R.id.etGoalMoney);
        etGoalMoney.setTypeface(font);

        etPeriod = (EditText)findViewById(R.id.etPeriod);
        etPeriod.setTypeface(font);

        Button btn = (Button)findViewById(R.id.btnNext);
        btn.setTypeface(font);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int plus = Integer.parseInt(etPeriod.getText().toString());

                DateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, plus);
                String goal_date = myFormat.format(new Date(c.getTimeInMillis()));


                final Goal goal = new Goal();
                goal.content = etName.getText().toString();
                if(etGoalMoney.getText().toString().length() != 0)
                    goal.goal_cost = Integer.parseInt(etGoalMoney.getText().toString());
                goal.method = 0;
                goal.now_cost = 0;
                goal.plus = plus;
                goal.goal_date = goal_date;

                if (goal.goal_cost != 0 && goal.content.length() != 0 && goal.plus > 0) {

                    NetworkManager.getInstance().setGoal(GoalSettingActivity.this, 0,
                            etName.getText().toString(), goal_date, Integer.parseInt(etGoalMoney.getText().toString()), 0, new NetworkManager.OnNetworkResultListener<Res>() {
                                @Override
                                public void onResult(Res res) {

                                    PropertyManager.getInstance().mGoal = goal;

                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra(RESULT_GOAL_SET, true);
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                }

                                @Override
                                public void onFail(Res res) {

                                }
                            });
                } else {
                    showDialog();
                }

            }
        });

        prev = (ImageButton)findViewById(R.id.imgbtnPrev);
        next = (ImageButton)findViewById(R.id.imgbtnNext);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(etPeriod.getText().toString());
                num--;
                etPeriod.setText(num + "");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(etPeriod.getText().toString());
                num++;
                etPeriod.setText(num + "");
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GoalSettingActivity.this);
        builder.setTitle("목표 설정하기");
        builder.setMessage("빈칸을 채워서 목표를 작성해주세요.");
        builder.setPositiveButton("응", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();


    }
}
