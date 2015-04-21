package com.quadcoder.coinpet.page.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.NetworkModel;
import com.quadcoder.coinpet.network.response.Res;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TransparentActivity extends ActionBarActivity {

    EditText etName;
    EditText etGoalMoney;
    EditText etPeriod;
    ImageButton prev;
    ImageButton next;

    public static final String RESULT_GOAL_SET = "result_goal_set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

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

//                Toast.makeText(TransparentActivity.this, goal_date, Toast.LENGTH_SHORT).show();
//                NetworkModel.getInstance().setGoal(TransparentActivity.this, 0,
//                        etName.getText().toString(), goal_date, Integer.parseInt(etGoalMoney.getText().toString()), 0, new NetworkModel.OnNetworkResultListener<Res>() {
//                            @Override
//                            public void onResult(Res res) {
//                                Intent resultIntent = new Intent();
//                                resultIntent.putExtra(RESULT_GOAL_SET,true);
//                                setResult(Activity.RESULT_OK, resultIntent);
//                            }
//
//                            @Override
//                            public void onFail(Res res) {
//
//                            }
//                        });

                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_GOAL_SET,true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transparent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
