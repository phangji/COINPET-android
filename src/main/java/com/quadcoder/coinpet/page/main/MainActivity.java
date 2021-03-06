package com.quadcoder.coinpet.page.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.audio.AudioEffect;
import com.quadcoder.coinpet.bluetooth.BluetoothManager;
import com.quadcoder.coinpet.bluetooth.BTConstants;
import com.quadcoder.coinpet.bluetooth.BluetoothUtil;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.logger.LogWrapper;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Res;
import com.quadcoder.coinpet.page.common.DialogActivity;
import com.quadcoder.coinpet.page.common.GoalSettingActivity;
import com.quadcoder.coinpet.page.friends.FriendsActivity;
import com.quadcoder.coinpet.page.mypet.MyPetActivity;
import com.quadcoder.coinpet.page.quest.QuestActivity;
import com.quadcoder.coinpet.page.quest.watcher.Parsing;
import com.quadcoder.coinpet.page.quest.watcher.QuestWatcher;
import com.quadcoder.coinpet.page.quiz.QuizActivity;
import com.quadcoder.coinpet.page.setting.SettingActivity;
import com.quadcoder.coinpet.page.tutorial.TutorialActivity;

import java.util.ArrayList;


public class MainActivity extends Activity {

    static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_CODE_EVENT = 100;
    public static final int REQUEST_CODE_QUIZ = 200;

    BluetoothDevice mDevice;
    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBtAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothManager mChatService = null;

    TextView tvNowMoney;

    static final int REQUEST_CODE_GOAL_SETTING_ACTIVITY = 10;

    @Override
    protected void onStart() {
        super.onStart();

        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BTConstants.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
            setupChatService();
        }
        startAnimation();
    }

    public static final String TAG = "MainActivity";

    /** Set up targets to receive log data */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }

    boolean isRegistered = false;

    public void discovery(){
        mBtAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mChatService != null) {
            mChatService.stop();
        }

        if(isRegistered)
            unregisterReceiver(mReceiver);
        NetworkManager.getInstance().cancelRequests(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.


        int nowLevel = PropertyManager.getInstance().getNowLevel();
        int nowPoint = PropertyManager.getInstance().getNowPoint();

        pbarExp.setMax(nowLevel * GAP_LEVELUP);
        pbarExp.setProgress(nowPoint);
        tvLevel.setText("Lv " + nowLevel);
        tvExpText.setText(nowPoint + "/" + nowLevel * GAP_LEVELUP);

        if(PropertyManager.getInstance().mGoal == null) {
            startActivityForResult(new Intent(MainActivity.this, GoalSettingActivity.class), REQUEST_CODE_GOAL_SETTING_ACTIVITY);
        }

        goalUiShow(PropertyManager.getInstance().isShowGoalUi());   //

//        devPointUpTest();
    }

    Runnable pointupRunnable;

    private void devPointUpTest() {
        pointupRunnable = new Runnable() {
            @Override
            public void run() {
                effectPointUp(40);
                mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.postDelayed(pointupRunnable, 5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(pointupRunnable);
    }

    void startAnimation() {
        ((AnimationDrawable)imgvPet.getDrawable()).start();

        Animation animCloud1 = AnimationUtils.loadAnimation(this, R.anim.cloud1_anim);
        imgvCloud1.startAnimation(animCloud1);
        Animation animCloud2 = AnimationUtils.loadAnimation(this, R.anim.cloud2_anim);
        imgvCloud2.startAnimation(animCloud2);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        imgvMail.startAnimation(shake);
    }

    ImageView imgvPet;
    ImageView imgvCloud1;
    ImageView imgvCloud2;
    ImageView imgvMailBg;
    ImageView imgvMail;
    ProgressBar pbarExp;
    TextView tvExpText;
    FrameLayout frameTalk;
    FrameLayout frameQuest;
    ImageView imgvLevelup;
    TextView tvTalk;
    ImageView imgvHeart;
    TextView tvLevel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //블루투스 연결 권장 다이얼로그 호출 결과
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChatService();
                    mChatService.setState(BTConstants.STATE_BT_ENABLED);
                    connectBt();
                    PropertyManager.getInstance().setBtRequested(true);
                    Log.d("phangji bt", "bt enabled result ok");

                } else if (requestCode == RESULT_CANCELED) {
                    PropertyManager.getInstance().setBtRequested(false);
                    Log.d("phangji bt", "bt enabled canceled");
                } else {    //블루투스를 켜시겠습니까? 아니요. OR 취소
//                    mChatService.setState(BluetoothManager.STATE_NONE);
                    PropertyManager.getInstance().setBtRequested(false);
                    Log.d("phangji bt", "BT not enabled");
                }
                break;
            case REQUEST_CODE_GOAL_SETTING_ACTIVITY:
                if(resultCode == Activity.RESULT_OK) {
//                    boolean isDoneFirstQuest = data.getBooleanExtra(GoalSettingActivity.RESULT_GOAL_SET, false);
                    mChatService.write(BluetoothUtil.getInstance().setGoalAndLock(PropertyManager.getInstance().mGoal.goal_cost));
                    goalUiShow(false);
                    PropertyManager.getInstance().setShowGoalUi(false);
                }
                break;
            case REQUEST_CODE_EVENT:
                if(resultCode == Activity.RESULT_OK) {
                    ArrayList<Integer> pointListUp = (ArrayList<Integer>)(data.getSerializableExtra(QuestActivity.INTENT_POINT_UP));
                    Log.d("main phangji", pointListUp.get(0).intValue() + " 값 ");
                    for(Integer val : pointListUp) {
                        final int point = val.intValue();
                        int idx = pointListUp.indexOf(val);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                effectPointUp(point);
                            }
                        }, idx * 3000);
                    }
                }
                break;
            case REQUEST_CODE_QUIZ:
                if(resultCode == Activity.RESULT_OK) {
                    ArrayList<Integer> moneyList = (ArrayList<Integer>)(data.getSerializableExtra(QuizActivity.INTENT_MONEY));
                    Log.d("main phangji", moneyList.get(0).intValue() + " 값 ");
                    for(Integer val : moneyList) {
                        final int money = val.intValue();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                moneyUp(money);
                            }
                        });
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void moneyUp(int money) {
        int sum = Integer.parseInt(tvNowMoney.getText().toString()) + money;

        QuestWatcher.getInstance().listenAction(Parsing.Type.SAVING, Parsing.Method.ANYTIME);

        tvNowMoney.setText("" + sum);

        PropertyManager.getInstance().mGoal.now_cost = sum;
        PropertyManager.getInstance().moneyUp(money);

        NetworkManager.getInstance().sendCoin(MainActivity.this, money, new NetworkManager.OnNetworkResultListener<Res>() {
            @Override
            public void onResult(Res res) {
            }

            @Override
            public void onFail(Res res) {
            }
        });

        checkGoalDone();
    }

    void checkGoalDone() {
        if(PropertyManager.getInstance().mGoal.goal_cost <= PropertyManager.getInstance().getNowMoney()) {
            showGoalFinishDialog();
        }
    }

    private void showGoalFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("목표 달성!!");
        builder.setMessage("목표를 달성했습니다. 잠금을 해제하시겠습니까?");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mChatService.write(BluetoothUtil.getInstance().unlock());

                // 목표 설정 퀘스트 다시 보이기
                goalUiShow(true);
                PropertyManager.getInstance().setShowGoalUi(true);
                PropertyManager.getInstance().setNowMoney(0);
                tvNowMoney.setText("0");
            }
        });

        builder.setNegativeButton(R.string.next_time, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    void connectBt() {
        if(mChatService.getState() == BTConstants.STATE_BT_ENABLED) {
            mDevice = mChatService.searchPaired();

            if( mDevice != null) {
                mChatService.connect(mDevice);
            } else {  //페어링된 적이 없다면,
                discovery();
            }

        }
    }

    void setupChatService() {
            mChatService = BluetoothManager.getInstance();
            mChatService.setBtHandler(mHandler);
    }

    void goalUiShow(boolean show) {
        if( show ) {
            imgvMail.setVisibility(View.VISIBLE);
            frameTalk.setVisibility(View.GONE);
//            imgvMailBg.setVisibility(View.VISIBLE);
        } else {
            imgvMail.setVisibility(View.GONE);
            frameTalk.setVisibility(View.VISIBLE);

        }
        imgvMailBg.setVisibility(View.GONE);
    }

    void setMainLayout() {

        tvNowMoney = (TextView)findViewById(R.id.tvNowMoney);
        imgvCloud1 = (ImageView)findViewById(R.id.imgvCloud1);
        imgvCloud2 = (ImageView)findViewById(R.id.imgvCloud2);
        imgvPet = (ImageView)findViewById(R.id.imgvPet);
        imgvMailBg = (ImageView)findViewById(R.id.imgvMailBg);
        imgvMail = (ImageView)findViewById(R.id.imgvMail);
        pbarExp = (ProgressBar)findViewById(R.id.pbarExp);
        tvExpText = (TextView)findViewById(R.id.tvExpText);
        frameTalk = (FrameLayout)findViewById(R.id.frameTalk);
        frameQuest = (FrameLayout) findViewById(R.id.frameQuest);
        tvTalk = (TextView)findViewById(R.id.tvTalk);
        imgvHeart = (ImageView)findViewById(R.id.imgvHeart);
        imgvLevelup = (ImageView)findViewById(R.id.imgvLevelup);
        tvLevel = (TextView)findViewById(R.id.tvLevel);
        Typeface font = Typeface.createFromAsset(getAssets(), com.quadcoder.coinpet.page.common.Constants.FONT_NORMAL);
        tvTalk.setTypeface(font);

        final AudioEffect boingAudio = new AudioEffect(AudioEffect.CARTOON_BOING);
        final AudioEffect hahahaAudio = new AudioEffect(AudioEffect.HAHAHA);
        final AudioEffect hmmmAudio = new AudioEffect(AudioEffect.HMMM);

        //펫 이미지 탭 했을 때
        imgvPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgvPet.setImageResource(R.drawable.pet_happy_anim);
                ((AnimationDrawable)imgvPet.getDrawable()).start();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgvPet.setImageResource(R.drawable.pet_default_anim);
                        ((AnimationDrawable) imgvPet.getDrawable()).start();
                    }
                }, 1000);


                int random = (int)(Math.random() * 10) % 3;
                switch (random) {
                    case 0:
                        boingAudio.play();
                        break;
                    case 1:
                        hahahaAudio.play();
                        break;
                    case 2:
                        hmmmAudio.play();
                        break;
                }
            }
        });

        //퀘스트 아이콘 탭했을 때
        imgvMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GoalSettingActivity.class);
                startActivity(i);
            }
        });

        // MyPet
        ImageView mainBtn = (ImageView)findViewById(R.id.imgvMyPet);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyPetActivity.class));
            }
        });

        // Quiz
        mainBtn = (ImageView)findViewById(R.id.imgvCashbook);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = PropertyManager.getInstance().getqCoin();
                if(count < 1) {
                    Intent i = new Intent(MainActivity.this, DialogActivity.class);
                    i.putExtra(DialogActivity.DIALOG_TYPE, DialogActivity.COINOVER);
                    startActivity(i);
                } else {
                    startActivityForResult(new Intent(MainActivity.this, QuizActivity.class), REQUEST_CODE_QUIZ);
                }

            }
        });

        // Quest
        mainBtn = (ImageView)findViewById(R.id.imgvQuest);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, QuestActivity.class), REQUEST_CODE_EVENT);
            }
        });

        // Friends
        mainBtn = (ImageView)findViewById(R.id.imgvReward);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            }
        });

        // Sync
        mainBtn = (ImageView)findViewById(R.id.imgvSync);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //임시로 Tutorial 실행
                startActivity(new Intent(MainActivity.this, TutorialActivity.class));
//                mChatService.write(BluetoothUtil.getInstance().requestMoneySync());
            }
        });

        // Setting
        mainBtn = (ImageView)findViewById(R.id.imgvSetting);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        findViewById(R.id.btnQuestWatcher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestWatcher.getInstance().listenAction(Parsing.Type.PARENT_QUEST, Parsing.Method.SUCCESS);
                QuestWatcher.getInstance().listenAction(Parsing.Type.STD_QUEST, Parsing.Method.SUCCESS);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMainLayout();

        Log.d("phangji bt", "onCreat isBtRequested" + PropertyManager.getInstance().isBtReqested());
        if(PropertyManager.getInstance().isBtReqested())
            setBtEnvironment();
    }



    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    //선택한 디바이스를 받아오면
                    if(device.getName() != null &&device.getName().equals(mChatService.SERVICE_NAME)){
                        Toast.makeText(MainActivity.this, device.getName() + " discovered", Toast.LENGTH_SHORT).show();
                        mDevice = device;
                        mBtAdapter.cancelDiscovery();
                        mChatService.setState(BTConstants.STATE_DISCOVERING);
                        mChatService.connect(mDevice);
                        isRegistered = true;
                    }
            }
        }
    };

    /**
     *  Bluetooth 환경 설정
     */

    public void setBtEnvironment() {
        //Bluetooth 환경 설정
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {
            Toast.makeText(MainActivity.this, "블루투스를 지원하지 않는 휴대폰입니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else if(!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else if(mChatService == null) {
            setupChatService();
            if (mChatService.getState() == BTConstants.STATE_CONNECTED) {
                if (mChatService != null) {
                    mChatService.connectDevice();
                }
            } else {
                mChatService.setState(BTConstants.STATE_BT_ENABLED);
                connectBt();
            }
        } else {
//            mChatService.setState(BTConstants.STATE_BT_ENABLED);
            if (mChatService != null) {
                mChatService.connectDevice();
            }
        }
    }

    /**
     * The Handler that gets information back from the BluetoothChatManager
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BTConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BTConstants.STATE_CONNECTED:
                            Toast.makeText(MainActivity.this, TAG + "/ state connected", Toast.LENGTH_SHORT).show();
                            break;
                        case BTConstants.STATE_CONNECTING:
                            Toast.makeText(MainActivity.this, TAG + "/ state connecting", Toast.LENGTH_SHORT).show();
                            break;
                        case BTConstants.STATE_LISTEN:
                        case BTConstants.STATE_NONE:
                            Toast.makeText(MainActivity.this, TAG + "/ state none", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case BTConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(MainActivity.this, TAG + " / Me : " + writeMessage, Toast.LENGTH_SHORT).show();
                    break;
                case BTConstants.MESSAGE_READ:
                        ArrayList<Byte> readBuffer = (ArrayList<Byte>) msg.obj;
                        Toast.makeText(MainActivity.this, "Tutorial / Device : " + readBuffer.toString(), Toast.LENGTH_SHORT).show();
                        int length = msg.arg1;
                        android.util.Log.d(TAG, "MESSAGE_READ " + length + " bytes read");
                        byte opcode = readBuffer.get(1);

                        if(opcode == BluetoothUtil.Opcode.READ_MONEY) {
                            int[] num = new int[3];
                            for(int i=3; i<=5; i++) {
                                num[i-3] = readBuffer.get(i);
                                if(num[i-3] < 0) {
                                    num[i-3] += 256;
                                }
                            }
                            final int money = num[0] * 256 * 256 + num[1] * 256 + num[2];
                            moneyUp(money);
                        }

                        if(opcode == BluetoothUtil.Opcode.READ_MONEY_SYNC) {
                            int[] num = new int[3];
                            for(int i=3; i<=5; i++) {
                                num[i-3] = readBuffer.get(i);
                                if(num[i-3] < 0) {
                                    num[i-3] += 256;
                                }
                            }
                            final int money = num[0] * 256 * 256 + num[1] * 256 + num[2];
                            moneyUp(money);
                        }

                    break;
                case BTConstants.MESSAGE_DEVICE_NAME:
                    String deviceName = msg.getData().getString(BTConstants.DEVICE_NAME);
                    Toast.makeText(MainActivity.this, "Connected to " + deviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BTConstants.MESSAGE_TOAST:
//                    Toast.makeText(MainActivity.this, msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void effectLevelUp() {
        final AudioEffect levelupAudio = new AudioEffect(AudioEffect.LEVEL_UP);
        levelupAudio.play();
        imgvPet.setImageResource(R.drawable.pet_happy_anim);
        ((AnimationDrawable)imgvPet.getDrawable()).start();

        // levelup effect
        imgvPet.setImageResource(R.drawable.pet_default_anim);
        ((AnimationDrawable) imgvPet.getDrawable()).start();


        imgvLevelup.setVisibility(View.VISIBLE);
        imgvLevelup.setImageResource(R.drawable.syntax_level_up);
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.mail_bg_anim);
        imgvLevelup.startAnimation(shake);
        mHandler.postDelayed(new Runnable() {   //edited
            @Override
            public void run() {
                imgvLevelup.setVisibility(View.INVISIBLE);
                imgvLevelup.setImageDrawable(null);
            }
        }, 3000);
    }

    static final int GAP_LEVELUP = 100;

    private void effectPointUp(int point) {
        final AudioEffect pointupAudio = new AudioEffect(AudioEffect.POINT_UP);
        pointupAudio.play();

        final int nowMax = pbarExp.getMax();
        int nowExp = pbarExp.getProgress();

        final int sum = nowExp + point;

        if( sum >= nowMax ) {
            //level up
            PropertyManager.getInstance().levelUp();
            PropertyManager.getInstance().setNowPoint(sum - nowMax);

            pbarExp.setMax(nowMax + GAP_LEVELUP);
            pbarExp.setProgress(sum - nowMax);
            tvLevel.setText("Lv " + PropertyManager.getInstance().getNowLevel());
            tvExpText.setText((sum - nowMax) + "/" + (nowMax + GAP_LEVELUP));

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    effectLevelUp();
                }
            }, 1000);

        } else {
            nowExp += point;
            pbarExp.setProgress(nowExp);
            tvExpText.setText(nowExp + "/" + nowMax);
            PropertyManager.getInstance().pointUp(point);
        }
    }
}
