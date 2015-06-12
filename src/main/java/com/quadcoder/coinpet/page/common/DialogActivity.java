package com.quadcoder.coinpet.page.common;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadcoder.coinpet.R;

public class DialogActivity extends Activity {

    TextView tvLarge;
    TextView tvDesp;
    ImageView imgvIcon;
    Button btn0;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        tvLarge = (TextView)findViewById(R.id.tvLarge);
        tvDesp = (TextView)findViewById(R.id.tvDesp);
        imgvIcon = (ImageView)findViewById(R.id.imgvIcon);
        btn0 = (Button)findViewById(R.id.btn0);
        btn1 = (Button)findViewById(R.id.btn1);

        setResources();
    }

    public static final String DIALOG_TYPE = "type";
    public static final String TEXT = "text";
    public static final int RIGHT = 0;
    public static final int WRONG = 1;
    public static final int TIMEOVER = 2;
    public static final int COINOVER = 3;

    public static final String RESULT_NEXT_QUIZ = "nextQuiz";

    private int mType;
    private void setResources() {
        mType = getIntent().getIntExtra(DIALOG_TYPE, RIGHT);
        String desp = getIntent().getStringExtra(TEXT);
        switch (mType) {
            case RIGHT :
                imgvIcon.setImageResource(R.drawable.d_o);
                tvLarge.setText(R.string.dialog_right);
                tvDesp.setText(desp);
                setTwoBtnListener();
                break;

            case WRONG :
                imgvIcon.setImageResource(R.drawable.d_x);
                tvLarge.setText(R.string.dialog_wrong);
                tvDesp.setText(desp);
                setTwoBtnListener();
                break;

            case TIMEOVER :
                imgvIcon.setImageResource(R.drawable.d_timeover);
                tvLarge.setText(R.string.dialog_time_over);
                tvDesp.setText(desp);
                setTwoBtnListener();
                break;

            case COINOVER :
                imgvIcon.setVisibility(View.GONE);
                btn1.setVisibility(View.GONE);
                tvLarge.setText(R.string.dialog_qcoin_over);
                tvDesp.setText(R.string.try_tmr);

                btn0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.putExtra(RESULT_NEXT_QUIZ, false);
                        setResult(Activity.RESULT_OK, i);
                    }
                });
                break;
        }
    }

    void setTwoBtnListener() {
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(RESULT_NEXT_QUIZ, true);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(RESULT_NEXT_QUIZ, false);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }
}
