package com.quadcoder.coinpet.page.quest;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.audio.AudioEffect;
import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.Quiz;
import com.quadcoder.coinpet.model.SystemQuest;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Res;

import java.util.ArrayList;

public class QuestActivity extends ActionBarActivity {

    ListView mListView;
    QuestItemAdapter mAdapter;
    Handler mHandler;
    private static final int TIME_REMOVE_ITEM = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        mListView = (ListView) findViewById(R.id.listView);

        levelupAudio = new AudioEffect(AudioEffect.GOOD_JOB);

        mHandler = new Handler();

        mAdapter = new QuestItemAdapter();

        mAdapter.setOnAdapterClickListener(new QuestItemAdapter.OnAdapterClickListener() {
            @Override
            public void onAdapterClick(View v, Quest item) {
                Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_SHORT).show();

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Object o = mAdapter.getItem(position);
                if (o instanceof SystemQuest) {
                    switch (((SystemQuest) o).state) {
                        case Quest.FINISHED:
                            Toast.makeText(QuestActivity.this, "시스템 퀘스트 보상을 받았습니다.", Toast.LENGTH_SHORT).show();

                            NetworkManager.getInstance().updateSystemQuest(QuestActivity.this, ((SystemQuest) o).pk_std_que, Quest.FINISHED, new NetworkManager.OnNetworkResultListener<Res>() {
                                @Override
                                public void onResult(Res res) {
                                    //상태 업데이트, 디비, 리스트에서 사라짐
                                    ((SystemQuest) o).state = Quest.DELETED;
                                    DBManager.getInstance().updateActiveSystemQuestState((SystemQuest) o);

                                    finishQuest(position);
                                }

                                @Override
                                public void onFail(Res res) {

                                }
                            });
                            break;
                    }
                } else {
                    switch (((ParentQuest) o).state) {
                        case Quest.FINISHED:
                            Toast.makeText(QuestActivity.this, "부모 퀘스트 보상을 받았습니다.", Toast.LENGTH_SHORT).show();
                            //상태 업데이트, 디비, 리스트에서 사라짐
                            ((ParentQuest) o).state = Quest.DELETED;
                            DBManager.getInstance().updateParentQuestState((ParentQuest) o);

                            //finish quest
                            finishQuest(position);


                            break;
                        case Quest.DOING:
                        case Quest.RETRYING:
                            //검사받기 요청
                            NetworkManager.getInstance().updateParentQuest(QuestActivity.this, ((ParentQuest) o).pk_parents_quest, Quest.WAITING, new NetworkManager.OnNetworkResultListener<Res>() {
                                @Override
                                public void onResult(Res res) {
                                    //상태 업데이트, 디비, 리스트 수정
                                    ((ParentQuest) o).state = Quest.WAITING;
                                    DBManager.getInstance().updateParentQuestState((ParentQuest) o);
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFail(Res res) {

                                }
                            });
                            break;
                    }
                }
            }
        });

        ArrayList<Quiz> quizs = DBManager.getInstance().getQuizList();
        for(Quiz record : quizs) {
            Log.d("quiz phangji", record.toString());
        }
    }

    void makeData() {
        mAdapter = new QuestItemAdapter();
        mAdapter.addParentAll(DBManager.getInstance().getParentQuestList());
        mAdapter.addSystemAll(DBManager.getInstance().getSystemQuestList());
        mListView.setAdapter(mAdapter);
    }

    AudioEffect levelupAudio;
    void finishQuest(final int position) {
        levelupAudio.play();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.removeItem(position);
            }
        }, TIME_REMOVE_ITEM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quest, menu);
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
