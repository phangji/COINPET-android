package com.quadcoder.coinpet.page.quest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.SystemQuest;

/**
 * Created by Phangji on 6/2/15.
 */
public class QuestItemView extends FrameLayout {

    public QuestItemView(Context context) {
        super(context);
        init();
    }

    Quest mItem;
    ImageView imgvIcon;
    TextView tvTitle;
    TextView tvDesp;
    TextView tvPoint;
    Button btnState;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.quest_item_layout, this);
        imgvIcon = (ImageView) findViewById(R.id.imgvIcon);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesp = (TextView) findViewById(R.id.tvDesp);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        btnState = (Button) findViewById(R.id.btnState);

        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onButtonListen(QuestItemView.this, mItem);
            }
        });
    }

    void setQuestItem(Quest item) {
        mItem = item;

        if( item instanceof SystemQuest) {
            setSystemQuest( (SystemQuest)item );
        } else {
            setParentQuest( (ParentQuest)item);
        }
    }

    interface OnButtonListener {
        void onButtonListen(View v, Quest item);
    }

    private OnButtonListener mListener;

    public void setOnButtonListener(OnButtonListener listener) {
        mListener = listener;
    }

    void setSystemQuest(SystemQuest item) {
        imgvIcon.setImageResource(R.drawable.system_quest);
        tvTitle.setText(item.content);
//      TODO:  tvDesp
        tvPoint.setText(item.point + "");

        switch(item.state) {
            case Quest.DOING:
                btnState.setBackgroundResource(R.color.grey);
//                btnState.setClickable(false);
                btnState.setText(R.string.btn_doing);
                break;
            case Quest.FINISHED:
                btnState.setBackgroundResource(R.color.purple);
//                btnState.setClickable(true);
                btnState.setText(R.string.btn_finished);
                break;
        }
    }

    void setParentQuest(ParentQuest item) {
        imgvIcon.setImageResource(R.drawable.mom_quest);
        tvTitle.setText(item.content);
        tvPoint.setText(item.point + "");

        switch(item.state) {
            case Quest.DOING:
                btnState.setBackgroundResource(R.color.purple);
//                btnState.setClickable(false);
                btnState.setText(R.string.btn_tocheck);
                break;
            case Quest.RETRYING:
                btnState.setBackgroundResource(R.color.purple);
//                btnState.setClickable(true);
                btnState.setText(R.string.btn_retry);
                if(item.comment != null) {
                    tvDesp.setText("\"" + item.comment + "\"");
                }

                break;
            case Quest.WAITING:
                btnState.setBackgroundResource(R.color.grey);
//                btnState.setClickable(false);
                btnState.setText(R.string.btn_checking);
                break;
            case Quest.FINISHED:
                btnState.setBackgroundResource(R.color.purple);
//                btnState.setClickable(true);
                btnState.setText(R.string.btn_finished);
                break;
        }
    }

    Quest getQuestItem() {
        return mItem;
    }
}
